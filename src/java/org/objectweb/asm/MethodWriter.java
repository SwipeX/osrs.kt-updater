/**
 * ASM: a very small and fast Java bytecode manipulation framework
 * Copyright (c) 2000-2011 INRIA, France Telecom
 * All rights reserved.
 * <p>
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * 3. Neither the name of the copyright holders nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * <p>
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.objectweb.asm;

import org.objectweb.asm.tree.*;

/**
 * A {@link MethodVisitor} that generates methods in bytecode form. Each visit
 * method of this class appends the bytecode corresponding to the visited
 * instruction to a byte vector, in the order these methods are called.
 *
 * @author Eric Bruneton
 * @author Eugene Kuleshov
 */
class MethodWriter extends MethodVisitor {

    /**
     * Pseudo access flag used to denote constructors.
     */
    static final int ACC_CONSTRUCTOR = 0x80000;

    /**
     * Frame has exactly the same locals as the previous stack map frame and
     * number of stack items is zero.
     */
    static final int SAME_FRAME = 0; // to 63 (0-3f)

    /**
     * Frame has exactly the same locals as the previous stack map frame and
     * number of stack items is 1
     */
    static final int SAME_LOCALS_1_STACK_ITEM_FRAME = 64; // to 127 (40-7f)

    /**
     * Reserved for future use
     */
    static final int RESERVED = 128;

    /**
     * Frame has exactly the same locals as the previous stack map frame and
     * number of stack items is 1. Offset is bigger then 63;
     */
    static final int SAME_LOCALS_1_STACK_ITEM_FRAME_EXTENDED = 247; // f7

    /**
     * Frame where current locals are the same as the locals in the previous
     * frame, except that the k last locals are absent. The value of k is given
     * by the formula 251-frame_type.
     */
    static final int CHOP_FRAME = 248; // to 250 (f8-fA)

    /**
     * Frame has exactly the same locals as the previous stack map frame and
     * number of stack items is zero. Offset is bigger then 63;
     */
    static final int SAME_FRAME_EXTENDED = 251; // fb

    /**
     * Frame where current locals are the same as the locals in the previous
     * frame, except that k additional locals are defined. The value of k is
     * given by the formula frame_type-251.
     */
    static final int APPEND_FRAME = 252; // to 254 // fc-fe

    /**
     * Full frame
     */
    static final int FULL_FRAME = 255; // ff

    /**
     * Indicates that the stack map frames must be recomputed from scratch. In
     * this case the maximum stack size and number of local variables is also
     * recomputed from scratch.
     *
     * @see #compute
     */
    private static final int FRAMES = 0;

    /**
     * Indicates that the maximum stack size and number of local variables must
     * be automatically computed.
     *
     * @see #compute
     */
    private static final int MAXS = 1;

    /**
     * Indicates that nothing must be automatically computed.
     *
     * @see #compute
     */
    private static final int NOTHING = 2;

    /**
     * The class writer to which this method must be added.
     */
    final org.objectweb.asm.ClassWriter cw;

    /**
     * Access flags of this method.
     */
    private int access;

    /**
     * The index of the constant pool item that contains the name of this
     * method.
     */
    private final int name;

    /**
     * The index of the constant pool item that contains the descriptor of this
     * method.
     */
    private final int desc;

    /**
     * The descriptor of this method.
     */
    private final String descriptor;

    /**
     * The signature of this method.
     */
    String signature;

    /**
     * If not zero, indicates that the code of this method must be copied from
     * the ClassReader associated to this writer in <code>cw.cr</code>. More
     * precisely, this field gives the index of the first byte to copied from
     * <code>cw.cr.b</code>.
     */
    int classReaderOffset;

    /**
     * If not zero, indicates that the code of this method must be copied from
     * the ClassReader associated to this writer in <code>cw.cr</code>. More
     * precisely, this field gives the number of bytes to copied from
     * <code>cw.cr.b</code>.
     */
    int classReaderLength;

    /**
     * Number of exceptions that can be thrown by this method.
     */
    int exceptionCount;

    /**
     * The exceptions that can be thrown by this method. More precisely, this
     * array contains the indexes of the constant pool items that contain the
     * internal names of these exception classes.
     */
    int[] exceptions;

    /**
     * The annotation default attribute of this method. May be <tt>null</tt>.
     */
    private org.objectweb.asm.ByteVector annd;

    /**
     * The runtime visible annotations of this method. May be <tt>null</tt>.
     */
    private AnnotationWriter anns;

    /**
     * The runtime invisible annotations of this method. May be <tt>null</tt>.
     */
    private AnnotationWriter ianns;

    /**
     * The runtime visible type annotations of this method. May be <tt>null</tt>
     * .
     */
    private AnnotationWriter tanns;

    /**
     * The runtime invisible type annotations of this method. May be
     * <tt>null</tt>.
     */
    private AnnotationWriter itanns;

    /**
     * The runtime visible parameter annotations of this method. May be
     * <tt>null</tt>.
     */
    private AnnotationWriter[] panns;

    /**
     * The runtime invisible parameter annotations of this method. May be
     * <tt>null</tt>.
     */
    private AnnotationWriter[] ipanns;

    /**
     * The number of synthetic parameters of this method.
     */
    private int synthetics;

    /**
     * The non standard attributes of the method.
     */
    private org.objectweb.asm.Attribute attrs;

    /**
     * The bytecode of this method.
     */
    private org.objectweb.asm.ByteVector code = new org.objectweb.asm.ByteVector();

    /**
     * Maximum stack size of this method.
     */
    private int maxStack;

    /**
     * Maximum number of local variables for this method.
     */
    private int maxLocals;

    /**
     * Number of local variables in the current stack map frame.
     */
    private int currentLocals;

    /**
     * Number of stack map frames in the StackMapTable attribute.
     */
    private int frameCount;

    /**
     * The StackMapTable attribute.
     */
    private org.objectweb.asm.ByteVector stackMap;

    /**
     * The offset of the last frame that was written in the StackMapTable
     * attribute.
     */
    private int previousFrameOffset;

    /**
     * The last frame that was written in the StackMapTable attribute.
     *
     * @see #frame
     */
    private int[] previousFrame;

    /**
     * The current stack map frame. The first element contains the offset of the
     * instruction to which the frame corresponds, the second element is the
     * number of locals and the third one is the number of stack elements. The
     * local variables start at index 3 and are followed by the operand stack
     * values. In summary frame[0] = offset, frame[1] = nLocal, frame[2] =
     * nStack, frame[3] = nLocal. All types are encoded as integers, with the
     * same format as the one used in {@link org.objectweb.asm.Label}, but limited to BASE types.
     */
    private int[] frame;

    /**
     * Number of elements in the exception handler list.
     */
    private int handlerCount;

    /**
     * The first element in the exception handler list.
     */
    private org.objectweb.asm.Handler firstHandler;

    /**
     * The last element in the exception handler list.
     */
    private org.objectweb.asm.Handler lastHandler;

    /**
     * Number of entries in the MethodParameters attribute.
     */
    private int methodParametersCount;

    /**
     * The MethodParameters attribute.
     */
    private org.objectweb.asm.ByteVector methodParameters;

    /**
     * Number of entries in the LocalVariableTable attribute.
     */
    private int localVarCount;

    /**
     * The LocalVariableTable attribute.
     */
    private org.objectweb.asm.ByteVector localVar;

    /**
     * Number of entries in the LocalVariableTypeTable attribute.
     */
    private int localVarTypeCount;

    /**
     * The LocalVariableTypeTable attribute.
     */
    private org.objectweb.asm.ByteVector localVarType;

    /**
     * Number of entries in the LineNumberTable attribute.
     */
    private int lineNumberCount;

    /**
     * The LineNumberTable attribute.
     */
    private org.objectweb.asm.ByteVector lineNumber;

    /**
     * The start offset of the last visited instruction.
     */
    private int lastCodeOffset;

    /**
     * The runtime visible type annotations of the code. May be <tt>null</tt>.
     */
    private AnnotationWriter ctanns;

    /**
     * The runtime invisible type annotations of the code. May be <tt>null</tt>.
     */
    private AnnotationWriter ictanns;

    /**
     * The non standard attributes of the method's code.
     */
    private org.objectweb.asm.Attribute cattrs;

    /**
     * Indicates if some jump instructions are too small and need to be resized.
     */
    private boolean resize;

    /**
     * The number of subroutines in this method.
     */
    private int subroutines;

    // ------------------------------------------------------------------------

    /*
     * Fields for the control flow graph analysis algorithm (used to compute the
     * maximum stack size). A control flow graph contains one node per "basic
     * block", and one edge per "jump" from one basic block to another. Each
     * node (i.e., each basic block) is represented by the Label object that
     * corresponds to the first instruction of this basic block. Each node also
     * stores the list of its successors in the graph, as a linked list of Edge
     * objects.
     */

    /**
     * Indicates what must be automatically computed.
     *
     * @see #FRAMES
     * @see #MAXS
     * @see #NOTHING
     */
    private final int compute;

    /**
     * A list of labels. This list is the list of basic blocks in the method,
     * i.e. a list of Label objects linked to each other by their
     * {@link org.objectweb.asm.Label#successor} field, in the order they are visited by
     * {@link MethodVisitor#visitLabel}, and starting with the first basic
     * block.
     */
    private org.objectweb.asm.Label labels;

    /**
     * The previous basic block.
     */
    private org.objectweb.asm.Label previousBlock;

    /**
     * The current basic block.
     */
    private org.objectweb.asm.Label currentBlock;

    /**
     * The (relative) stack size after the last visited instruction. This size
     * is relative to the beginning of the current basic block, i.e., the true
     * stack size after the last visited instruction is equal to the
     * {@link org.objectweb.asm.Label#inputStackTop beginStackSize} of the current basic block
     * plus <tt>stackSize</tt>.
     */
    private int stackSize;

    /**
     * The (relative) maximum stack size after the last visited instruction.
     * This size is relative to the beginning of the current basic block, i.e.,
     * the true maximum stack size after the last visited instruction is equal
     * to the {@link org.objectweb.asm.Label#inputStackTop beginStackSize} of the current basic
     * block plus <tt>stackSize</tt>.
     */
    private int maxStackSize;

    // ------------------------------------------------------------------------
    // Constructor
    // ------------------------------------------------------------------------

    /**
     * Constructs a new {@link MethodWriter}.
     *
     * @param cw
     *            the class writer in which the method must be added.
     * @param access
     *            the method's access flags (see {@link org.objectweb.asm.Opcodes}).
     * @param name
     *            the method's name.
     * @param desc
     *            the method's descriptor (see {@link org.objectweb.asm.Type}).
     * @param signature
     *            the method's signature. May be <tt>null</tt>.
     * @param exceptions
     *            the internal names of the method's exceptions. May be
     *            <tt>null</tt>.
     * @param computeMaxs
     *            <tt>true</tt> if the maximum stack size and number of local
     *            variables must be automatically computed.
     * @param computeFrames
     *            <tt>true</tt> if the stack map tables must be recomputed from
     *            scratch.
     */
    MethodWriter(final org.objectweb.asm.ClassWriter cw, final int access, final String name,
                 final String desc, final String signature,
                 final String[] exceptions, final boolean computeMaxs,
                 final boolean computeFrames) {
        if (cw.firstMethod == null) {
            cw.firstMethod = this;
        } else {
            cw.lastMethod.mv = this;
        }
        cw.lastMethod = this;
        this.cw = cw;
        this.access = access;
        if ("<init>".equals(name)) {
            this.access |= ACC_CONSTRUCTOR;
        }
        this.name = cw.newUTF8(name);
        this.desc = cw.newUTF8(desc);
        this.descriptor = desc;
        if (org.objectweb.asm.ClassReader.SIGNATURES) {
            this.signature = signature;
        }
        if (exceptions != null && exceptions.length > 0) {
            exceptionCount = exceptions.length;
            this.exceptions = new int[exceptionCount];
            for (int i = 0; i < exceptionCount; ++i) {
                this.exceptions[i] = cw.newClass(exceptions[i]);
            }
        }
        this.compute = computeFrames ? FRAMES : (computeMaxs ? MAXS : NOTHING);
        if (computeMaxs || computeFrames) {
            // updates maxLocals
            int size = org.objectweb.asm.Type.getArgumentsAndReturnSizes(descriptor) >> 2;
            if ((access & org.objectweb.asm.Opcodes.ACC_STATIC) != 0) {
                --size;
            }
            maxLocals = size;
            currentLocals = size;
            // creates and visits the label for the first basic block
            labels = new org.objectweb.asm.Label();
            labels.status |= org.objectweb.asm.Label.PUSHED;
            visitLabel(labels);
        }
    }

    // ------------------------------------------------------------------------
    // Implementation of the MethodVisitor abstract class
    // ------------------------------------------------------------------------

    @Override
    public void visitParameter(ParameterNode pn) {
        if (methodParameters == null) {
            methodParameters = new org.objectweb.asm.ByteVector();
        }
        ++methodParametersCount;
        methodParameters.putShort((pn.name == null) ? 0 : cw.newUTF8(pn.name)).putShort(pn.access);
    }

    @Override
    public AnnotationVisitor visitAnnotationDefault() {
        if (!org.objectweb.asm.ClassReader.ANNOTATIONS) {
            return null;
        }
        annd = new org.objectweb.asm.ByteVector();
        return new AnnotationWriter(cw, false, annd, null, 0);
    }

    @Override
    public AnnotationVisitor visitAnnotation(AnnotationNode an, final boolean visible) {
        if (!org.objectweb.asm.ClassReader.ANNOTATIONS) {
            return null;
        }
        org.objectweb.asm.ByteVector bv = new org.objectweb.asm.ByteVector();
        // write type, and reserve space for values count
        bv.putShort(cw.newUTF8(an.desc)).putShort(0);
        AnnotationWriter aw = new AnnotationWriter(cw, true, bv, bv, 2);
        if (visible) {
            aw.next = anns;
            anns = aw;
        } else {
            aw.next = ianns;
            ianns = aw;
        }
        return aw;
    }

    @Override
    public AnnotationVisitor visitTypeAnnotation(TypeAnnotationNode tan, final boolean visible) {
        if (!org.objectweb.asm.ClassReader.ANNOTATIONS) {
            return null;
        }
        org.objectweb.asm.ByteVector bv = new org.objectweb.asm.ByteVector();
        // write target_type and target_info
        AnnotationWriter.putTarget(tan.typeRef, tan.typePath, bv);
        // write type, and reserve space for values count
        bv.putShort(cw.newUTF8(tan.desc)).putShort(0);
        AnnotationWriter aw = new AnnotationWriter(cw, true, bv, bv,
                bv.length - 2);
        if (visible) {
            aw.next = tanns;
            tanns = aw;
        } else {
            aw.next = itanns;
            itanns = aw;
        }
        return aw;
    }

    @Override
    public AnnotationVisitor visitParameterAnnotation(ParameterAnnotationNode pan, final boolean visible) {
        if (!org.objectweb.asm.ClassReader.ANNOTATIONS) {
            return null;
        }
        org.objectweb.asm.ByteVector bv = new org.objectweb.asm.ByteVector();
        if ("Ljava/lang/Synthetic;".equals(desc)) {
            // workaround for a bug in javac with synthetic parameters
            // see ClassReader.readParameterAnnotations
            synthetics = Math.max(synthetics, pan.parameter + 1);
            return new AnnotationWriter(cw, false, bv, null, 0);
        }
        // write type, and reserve space for values count
        bv.putShort(cw.newUTF8(pan.desc)).putShort(0);
        AnnotationWriter aw = new AnnotationWriter(cw, true, bv, bv, 2);
        if (visible) {
            if (panns == null) {
                panns = new AnnotationWriter[org.objectweb.asm.Type.getArgumentTypes(descriptor).length];
            }
            aw.next = panns[pan.parameter];
            panns[pan.parameter] = aw;
        } else {
            if (ipanns == null) {
                ipanns = new AnnotationWriter[org.objectweb.asm.Type.getArgumentTypes(descriptor).length];
            }
            aw.next = ipanns[pan.parameter];
            ipanns[pan.parameter] = aw;
        }
        return aw;
    }

    @Override
    public void visitAttribute(final org.objectweb.asm.Attribute attr) {
        if (attr.isCodeAttribute()) {
            attr.next = cattrs;
            cattrs = attr;
        } else {
            attr.next = attrs;
            attrs = attr;
        }
    }

    @Override
    public void visitCode() {
    }

    @Override
    public void visitFrame(FrameNode node) {
        if (!org.objectweb.asm.ClassReader.FRAMES || compute == FRAMES) {
            return;
        }
        int nLocal = node.local != null ? node.local.size() : 0;
        int nStack = node.stack != null ? node.stack.size() : 0;
        if (node.type == org.objectweb.asm.Opcodes.F_NEW) {
            if (previousFrame == null) {
                visitImplicitFirstFrame();
            }
            currentLocals = nLocal;
            int frameIndex = startFrame(code.length, nLocal, nStack);
            for (int i = 0; i < nLocal; ++i) {
                if (node.local.get(i) instanceof String) {
                    frame[frameIndex++] = org.objectweb.asm.Frame.OBJECT | cw.addType((String) node.local.get(i));
                } else if (node.local.get(i) instanceof Integer) {
                    frame[frameIndex++] = (Integer) node.local.get(i);
                } else {
                    frame[frameIndex++] = org.objectweb.asm.Frame.UNINITIALIZED | cw.addUninitializedType("",
                            ((org.objectweb.asm.Label) node.local.get(i)).position);
                }
            }
            for (int i = 0; i < nStack; ++i) {
                if (node.stack.get(i) instanceof String) {
                    frame[frameIndex++] = org.objectweb.asm.Frame.OBJECT | cw.addType((String) node.stack.get(i));
                } else if (node.stack.get(i) instanceof Integer) {
                    frame[frameIndex++] = (Integer) node.stack.get(i);
                } else {
                    frame[frameIndex++] = org.objectweb.asm.Frame.UNINITIALIZED | cw.addUninitializedType("",
                            ((org.objectweb.asm.Label) node.stack.get(i)).position);
                }
            }
            endFrame();
        } else {
            int delta;
            if (stackMap == null) {
                stackMap = new org.objectweb.asm.ByteVector();
                delta = code.length;
            } else {
                delta = code.length - previousFrameOffset - 1;
                if (delta < 0) {
                    if (node.type == org.objectweb.asm.Opcodes.F_SAME) {
                        return;
                    } else {
                        throw new IllegalStateException();
                    }
                }
            }
            switch (node.type) {
                case org.objectweb.asm.Opcodes.F_FULL:
                    currentLocals = nLocal;
                    stackMap.putByte(FULL_FRAME).putShort(delta).putShort(nLocal);
                    for (int i = 0; i < nLocal; ++i) {
                        writeFrameType(node.local.get(i));
                    }
                    stackMap.putShort(nStack);
                    for (int i = 0; i < nStack; ++i) {
                        writeFrameType(node.stack.get(i));
                    }
                    break;
                case org.objectweb.asm.Opcodes.F_APPEND:
                    currentLocals += nLocal;
                    stackMap.putByte(SAME_FRAME_EXTENDED + nLocal).putShort(delta);
                    for (int i = 0; i < nLocal; ++i) {
                        writeFrameType(node.local.get(i));
                    }
                    break;
                case org.objectweb.asm.Opcodes.F_CHOP:
                    currentLocals -= nLocal;
                    stackMap.putByte(SAME_FRAME_EXTENDED - nLocal).putShort(delta);
                    break;
                case org.objectweb.asm.Opcodes.F_SAME:
                    if (delta < 64) {
                        stackMap.putByte(delta);
                    } else {
                        stackMap.putByte(SAME_FRAME_EXTENDED).putShort(delta);
                    }
                    break;
                case org.objectweb.asm.Opcodes.F_SAME1:
                    if (delta < 64) {
                        stackMap.putByte(SAME_LOCALS_1_STACK_ITEM_FRAME + delta);
                    } else {
                        stackMap.putByte(SAME_LOCALS_1_STACK_ITEM_FRAME_EXTENDED).putShort(delta);
                    }
                    writeFrameType(node.stack.get(0));
                    break;
            }

            previousFrameOffset = code.length;
            ++frameCount;
        }

        maxStack = Math.max(maxStack, nStack);
        maxLocals = Math.max(maxLocals, currentLocals);
    }

    @Override
    public void visitInsn(InsnNode in) {
        int opcode = in.opcode();
        lastCodeOffset = code.length;
        // adds the instruction to the bytecode of the method
        code.putByte(opcode);
        // update currentBlock
        // Label currentBlock = this.currentBlock;
        if (currentBlock != null) {
            if (compute == FRAMES) {
                currentBlock.frame.execute(opcode, 0, null, null);
            } else {
                // updates current and max stack sizes
                int size = stackSize + org.objectweb.asm.Frame.SIZE[opcode];
                if (size > maxStackSize) {
                    maxStackSize = size;
                }
                stackSize = size;
            }
            // if opcode == ATHROW or xRETURN, ends current block (no successor)
            if ((opcode >= org.objectweb.asm.Opcodes.IRETURN && opcode <= org.objectweb.asm.Opcodes.RETURN)
                    || opcode == org.objectweb.asm.Opcodes.ATHROW) {
                noSuccessor();
            }
        }
    }

    @Override
    public void visitIntInsn(IntInsnNode iin) {
        int opcode = iin.opcode();
        lastCodeOffset = code.length;
        // Label currentBlock = this.currentBlock;
        if (currentBlock != null) {
            if (compute == FRAMES) {
                currentBlock.frame.execute(opcode, iin.operand, null, null);
            } else if (opcode != org.objectweb.asm.Opcodes.NEWARRAY) {
                // updates current and max stack sizes only for NEWARRAY
                // (stack size variation = 0 for BIPUSH or SIPUSH)
                int size = stackSize + 1;
                if (size > maxStackSize) {
                    maxStackSize = size;
                }
                stackSize = size;
            }
        }
        // adds the instruction to the bytecode of the method
        if (opcode == org.objectweb.asm.Opcodes.SIPUSH) {
            code.put12(opcode, iin.operand);
        } else { // BIPUSH or NEWARRAY
            code.put11(opcode, iin.operand);
        }
    }

    @Override
    public void visitVarInsn(VarInsnNode vin) {
        int opcode = vin.opcode();
        lastCodeOffset = code.length;
        // Label currentBlock = this.currentBlock;
        if (currentBlock != null) {
            if (compute == FRAMES) {
                currentBlock.frame.execute(opcode, vin.var, null, null);
            } else {
                // updates current and max stack sizes
                if (opcode == org.objectweb.asm.Opcodes.RET) {
                    // no stack change, but end of current block (no successor)
                    currentBlock.status |= org.objectweb.asm.Label.RET;
                    // save 'stackSize' here for future use
                    // (see {@link #findSubroutineSuccessors})
                    currentBlock.inputStackTop = stackSize;
                    noSuccessor();
                } else { // xLOAD or xSTORE
                    int size = stackSize + org.objectweb.asm.Frame.SIZE[opcode];
                    if (size > maxStackSize) {
                        maxStackSize = size;
                    }
                    stackSize = size;
                }
            }
        }
        if (compute != NOTHING) {
            // updates max locals
            int n;
            if (opcode == org.objectweb.asm.Opcodes.LLOAD || opcode == org.objectweb.asm.Opcodes.DLOAD
                    || opcode == org.objectweb.asm.Opcodes.LSTORE || opcode == org.objectweb.asm.Opcodes.DSTORE) {
                n = vin.var + 2;
            } else {
                n = vin.var + 1;
            }
            if (n > maxLocals) {
                maxLocals = n;
            }
        }
        // adds the instruction to the bytecode of the method
        if (vin.var < 4 && opcode != org.objectweb.asm.Opcodes.RET) {
            int opt;
            if (opcode < org.objectweb.asm.Opcodes.ISTORE) {
                /* ILOAD_0 */
                opt = 26 + ((opcode - org.objectweb.asm.Opcodes.ILOAD) << 2) + vin.var;
            } else {
                /* ISTORE_0 */
                opt = 59 + ((opcode - org.objectweb.asm.Opcodes.ISTORE) << 2) + vin.var;
            }
            code.putByte(opt);
        } else if (vin.var >= 256) {
            code.putByte(196 /* WIDE */).put12(opcode, vin.var);
        } else {
            code.put11(opcode, vin.var);
        }
        if (opcode >= org.objectweb.asm.Opcodes.ISTORE && compute == FRAMES && handlerCount > 0) {
            visitLabel(new org.objectweb.asm.Label());
        }
    }

    @Override
    public void visitTypeInsn(TypeInsnNode tin) {
        int opcode = tin.opcode();
        lastCodeOffset = code.length;
        org.objectweb.asm.Item i = cw.newClassItem(tin.desc);
        // Label currentBlock = this.currentBlock;
        if (currentBlock != null) {
            if (compute == FRAMES) {
                currentBlock.frame.execute(opcode, code.length, cw, i);
            } else if (opcode == org.objectweb.asm.Opcodes.NEW) {
                // updates current and max stack sizes only if opcode == NEW
                // (no stack change for ANEWARRAY, CHECKCAST, INSTANCEOF)
                int size = stackSize + 1;
                if (size > maxStackSize) {
                    maxStackSize = size;
                }
                stackSize = size;
            }
        }
        // adds the instruction to the bytecode of the method
        code.put12(opcode, i.index);
    }

    @Override
    public void visitFieldInsn(FieldInsnNode fin) {
        int opcode = fin.opcode();
        lastCodeOffset = code.length;
        org.objectweb.asm.Item i = cw.newFieldItem(fin.owner, fin.name, fin.desc);
        // Label currentBlock = this.currentBlock;
        if (currentBlock != null) {
            if (compute == FRAMES) {
                currentBlock.frame.execute(opcode, 0, cw, i);
            } else {
                int size;
                // computes the stack size variation
                char c = fin.desc.charAt(0);
                switch (opcode) {
                    case org.objectweb.asm.Opcodes.GETSTATIC:
                        size = stackSize + (c == 'D' || c == 'J' ? 2 : 1);
                        break;
                    case org.objectweb.asm.Opcodes.PUTSTATIC:
                        size = stackSize + (c == 'D' || c == 'J' ? -2 : -1);
                        break;
                    case org.objectweb.asm.Opcodes.GETFIELD:
                        size = stackSize + (c == 'D' || c == 'J' ? 1 : 0);
                        break;
                    // case Constants.PUTFIELD:
                    default:
                        size = stackSize + (c == 'D' || c == 'J' ? -3 : -2);
                        break;
                }
                // updates current and max stack sizes
                if (size > maxStackSize) {
                    maxStackSize = size;
                }
                stackSize = size;
            }
        }
        // adds the instruction to the bytecode of the method
        code.put12(opcode, i.index);
    }

    @Override
    public void visitMethodInsn(MethodInsnNode min) {
        int opcode = min.opcode();
        lastCodeOffset = code.length;
        org.objectweb.asm.Item i = cw.newMethodItem(min.owner, min.name, min.desc, min.itf);
        int argSize = i.intVal;
        // Label currentBlock = this.currentBlock;
        if (currentBlock != null) {
            if (compute == FRAMES) {
                currentBlock.frame.execute(opcode, 0, cw, i);
            } else {
                /*
                 * computes the stack size variation. In order not to recompute
                 * several times this variation for the same Item, we use the
                 * intVal field of this item to store this variation, once it
                 * has been computed. More precisely this intVal field stores
                 * the sizes of the arguments and of the return value
                 * corresponding to desc.
                 */
                if (argSize == 0) {
                    // the above sizes have not been computed yet,
                    // so we compute them...
                    argSize = org.objectweb.asm.Type.getArgumentsAndReturnSizes(min.desc);
                    // ... and we save them in order
                    // not to recompute them in the future
                    i.intVal = argSize;
                }
                int size;
                if (opcode == org.objectweb.asm.Opcodes.INVOKESTATIC) {
                    size = stackSize - (argSize >> 2) + (argSize & 0x03) + 1;
                } else {
                    size = stackSize - (argSize >> 2) + (argSize & 0x03);
                }
                // updates current and max stack sizes
                if (size > maxStackSize) {
                    maxStackSize = size;
                }
                stackSize = size;
            }
        }
        // adds the instruction to the bytecode of the method
        if (opcode == org.objectweb.asm.Opcodes.INVOKEINTERFACE) {
            if (argSize == 0) {
                argSize = org.objectweb.asm.Type.getArgumentsAndReturnSizes(min.desc);
                i.intVal = argSize;
            }
            code.put12(org.objectweb.asm.Opcodes.INVOKEINTERFACE, i.index).put11(argSize >> 2, 0);
        } else {
            code.put12(opcode, i.index);
        }
    }

    @Override
    public void visitInvokeDynamicInsn(InvokeDynamicInsnNode idin) {
        lastCodeOffset = code.length;
        org.objectweb.asm.Item i = cw.newInvokeDynamicItem(idin.name, idin.desc, idin.bsm, idin.bsmArgs);
        int argSize = i.intVal;
        // Label currentBlock = this.currentBlock;
        if (currentBlock != null) {
            if (compute == FRAMES) {
                currentBlock.frame.execute(org.objectweb.asm.Opcodes.INVOKEDYNAMIC, 0, cw, i);
            } else {
                /*
                 * computes the stack size variation. In order not to recompute
                 * several times this variation for the same Item, we use the
                 * intVal field of this item to store this variation, once it
                 * has been computed. More precisely this intVal field stores
                 * the sizes of the arguments and of the return value
                 * corresponding to desc.
                 */
                if (argSize == 0) {
                    // the above sizes have not been computed yet,
                    // so we compute them...
                    argSize = org.objectweb.asm.Type.getArgumentsAndReturnSizes(idin.desc);
                    // ... and we save them in order
                    // not to recompute them in the future
                    i.intVal = argSize;
                }
                int size = stackSize - (argSize >> 2) + (argSize & 0x03) + 1;
                // updates current and max stack sizes
                if (size > maxStackSize) {
                    maxStackSize = size;
                }
                stackSize = size;
            }
        }
        // adds the instruction to the bytecode of the method
        code.put12(org.objectweb.asm.Opcodes.INVOKEDYNAMIC, i.index);
        code.putShort(0);
    }

    @Override
    public void visitJumpInsn(JumpInsnNode jin) {
        int opcode = jin.opcode();
        org.objectweb.asm.Label label = jin.label.getLabel();
        lastCodeOffset = code.length;
        org.objectweb.asm.Label nextInsn = null;
        // Label currentBlock = this.currentBlock;
        if (currentBlock != null) {
            if (compute == FRAMES) {
                currentBlock.frame.execute(opcode, 0, null, null);
                // 'label' is the target of a jump instruction
                label.getFirst().status |= org.objectweb.asm.Label.TARGET;
                // adds 'label' as a successor of this basic block
                addSuccessor(org.objectweb.asm.Edge.NORMAL, label);
                if (opcode != org.objectweb.asm.Opcodes.GOTO) {
                    // creates a Label for the next basic block
                    nextInsn = new org.objectweb.asm.Label();
                }
            } else {
                if (opcode == org.objectweb.asm.Opcodes.JSR) {
                    if ((label.status & org.objectweb.asm.Label.SUBROUTINE) == 0) {
                        label.status |= org.objectweb.asm.Label.SUBROUTINE;
                        ++subroutines;
                    }
                    currentBlock.status |= org.objectweb.asm.Label.JSR;
                    addSuccessor(stackSize + 1, label);
                    // creates a Label for the next basic block
                    nextInsn = new org.objectweb.asm.Label();
                    /*
                     * note that, by construction in this method, a JSR block
                     * has at least two successors in the control flow graph:
                     * the first one leads the next instruction after the JSR,
                     * while the second one leads to the JSR target.
                     */
                } else {
                    // updates current stack size (max stack size unchanged
                    // because stack size variation always negative in this
                    // case)
                    stackSize += org.objectweb.asm.Frame.SIZE[opcode];
                    addSuccessor(stackSize, label);
                }
            }
        }
        // adds the instruction to the bytecode of the method
        if ((label.status & org.objectweb.asm.Label.RESOLVED) != 0
                && label.position - code.length < Short.MIN_VALUE) {
            /*
             * case of a backward jump with an offset < -32768. In this case we
             * automatically replace GOTO with GOTO_W, JSR with JSR_W and IFxxx
             * <l> with IFNOTxxx <l'> GOTO_W <l>, where IFNOTxxx is the
             * "opposite" opcode of IFxxx (i.e., IFNE for IFEQ) and where <l'>
             * designates the instruction just after the GOTO_W.
             */
            if (opcode == org.objectweb.asm.Opcodes.GOTO) {
                code.putByte(200); // GOTO_W
            } else if (opcode == org.objectweb.asm.Opcodes.JSR) {
                code.putByte(201); // JSR_W
            } else {
                // if the IF instruction is transformed into IFNOT GOTO_W the
                // next instruction becomes the target of the IFNOT instruction
                if (nextInsn != null) {
                    nextInsn.status |= org.objectweb.asm.Label.TARGET;
                }
                code.putByte(opcode <= 166 ? ((opcode + 1) ^ 1) - 1
                        : opcode ^ 1);
                code.putShort(8); // jump offset
                code.putByte(200); // GOTO_W
            }
            label.put(this, code, code.length - 1, true);
        } else {
            /*
             * case of a backward jump with an offset >= -32768, or of a forward
             * jump with, of course, an unknown offset. In these cases we store
             * the offset in 2 bytes (which will be increased in
             * resizeInstructions, if needed).
             */
            code.putByte(opcode);
            label.put(this, code, code.length - 1, false);
        }
        if (currentBlock != null) {
            if (nextInsn != null) {
                // if the jump instruction is not a GOTO, the next instruction
                // is also a successor of this instruction. Calling visitLabel
                // adds the label of this next instruction as a successor of the
                // current block, and starts a new basic block
                visitLabel(nextInsn);
            }
            if (opcode == org.objectweb.asm.Opcodes.GOTO) {
                noSuccessor();
            }
        }
    }

    @Override
    public void visitLabel(final org.objectweb.asm.Label label) {
        // resolves previous forward references to label, if any
        resize |= label.resolve(this, code.length, code.data);
        // updates currentBlock
        if ((label.status & org.objectweb.asm.Label.DEBUG) != 0) {
            return;
        }
        if (compute == FRAMES) {
            if (currentBlock != null) {
                if (label.position == currentBlock.position) {
                    // successive labels, do not start a new basic block
                    currentBlock.status |= (label.status & org.objectweb.asm.Label.TARGET);
                    label.frame = currentBlock.frame;
                    return;
                }
                // ends current block (with one new successor)
                addSuccessor(org.objectweb.asm.Edge.NORMAL, label);
            }
            // begins a new current block
            currentBlock = label;
            if (label.frame == null) {
                label.frame = new org.objectweb.asm.Frame();
                label.frame.owner = label;
            }
            // updates the basic block list
            if (previousBlock != null) {
                if (label.position == previousBlock.position) {
                    previousBlock.status |= (label.status & org.objectweb.asm.Label.TARGET);
                    label.frame = previousBlock.frame;
                    currentBlock = previousBlock;
                    return;
                }
                previousBlock.successor = label;
            }
            previousBlock = label;
        } else if (compute == MAXS) {
            if (currentBlock != null) {
                // ends current block (with one new successor)
                currentBlock.outputStackMax = maxStackSize;
                addSuccessor(stackSize, label);
            }
            // begins a new current block
            currentBlock = label;
            // resets the relative current and max stack sizes
            stackSize = 0;
            maxStackSize = 0;
            // updates the basic block list
            if (previousBlock != null) {
                previousBlock.successor = label;
            }
            previousBlock = label;
        }
    }

    @Override
    public void visitLdcInsn(LdcInsnNode ldc) {
        lastCodeOffset = code.length;
        org.objectweb.asm.Item i = cw.newConstItem(ldc.cst);
        // Label currentBlock = this.currentBlock;
        if (currentBlock != null) {
            if (compute == FRAMES) {
                currentBlock.frame.execute(org.objectweb.asm.Opcodes.LDC, 0, cw, i);
            } else {
                int size;
                // computes the stack size variation
                if (i.type == org.objectweb.asm.ClassWriter.LONG || i.type == org.objectweb.asm.ClassWriter.DOUBLE) {
                    size = stackSize + 2;
                } else {
                    size = stackSize + 1;
                }
                // updates current and max stack sizes
                if (size > maxStackSize) {
                    maxStackSize = size;
                }
                stackSize = size;
            }
        }
        // adds the instruction to the bytecode of the method
        int index = i.index;
        if (i.type == org.objectweb.asm.ClassWriter.LONG || i.type == org.objectweb.asm.ClassWriter.DOUBLE) {
            code.put12(20 /* LDC2_W */, index);
        } else if (index >= 256) {
            code.put12(19 /* LDC_W */, index);
        } else {
            code.put11(org.objectweb.asm.Opcodes.LDC, index);
        }
    }

    @Override
    public void visitIincInsn(IincInsnNode iin) {
        lastCodeOffset = code.length;
        if (currentBlock != null) {
            if (compute == FRAMES) {
                currentBlock.frame.execute(org.objectweb.asm.Opcodes.IINC, iin.var, null, null);
            }
        }
        if (compute != NOTHING) {
            // updates max locals
            int n = iin.var + 1;
            if (n > maxLocals) {
                maxLocals = n;
            }
        }
        // adds the instruction to the bytecode of the method
        if ((iin.var > 255) || (iin.incr > 127) || (iin.incr < -128)) {
            code.putByte(196 /* WIDE */).put12(org.objectweb.asm.Opcodes.IINC, iin.var).putShort(iin.incr);
        } else {
            code.putByte(org.objectweb.asm.Opcodes.IINC).put11(iin.var, iin.incr);
        }
    }

    @Override
    public void visitTableSwitchInsn(TableSwitchInsnNode tsin) {
        lastCodeOffset = code.length;
        // adds the instruction to the bytecode of the method
        int source = code.length;
        code.putByte(org.objectweb.asm.Opcodes.TABLESWITCH);
        code.putByteArray(null, 0, (4 - code.length % 4) % 4);
        tsin.dflt.getLabel().put(this, code, source, true);
        code.putInt(tsin.min).putInt(tsin.max);
        org.objectweb.asm.Label[] labels = new org.objectweb.asm.Label[tsin.labels.size()];
        for (int i = 0; i < labels.length; ++i) {
            labels[i] = tsin.labels.get(i).getLabel();
            labels[i].put(this, code, source, true);
        }
        // updates currentBlock
        visitSwitchInsn(tsin.dflt.getLabel(), labels);
    }

    @Override
    public void visitLookupSwitchInsn(LookupSwitchInsnNode lsin) {
        lastCodeOffset = code.length;
        // adds the instruction to the bytecode of the method
        int source = code.length;
        code.putByte(org.objectweb.asm.Opcodes.LOOKUPSWITCH);
        code.putByteArray(null, 0, (4 - code.length % 4) % 4);
        lsin.dflt.getLabel().put(this, code, source, true);
        code.putInt(lsin.labels.size());
        org.objectweb.asm.Label[] labels = new org.objectweb.asm.Label[lsin.labels.size()];
        for (int i = 0; i < labels.length; ++i) {
            code.putInt(lsin.keys.get(i));
            labels[i] = lsin.labels.get(i).getLabel();
            labels[i].put(this, code, source, true);
        }
        // updates currentBlock
        visitSwitchInsn(lsin.dflt.getLabel(), labels);
    }

    private void visitSwitchInsn(final org.objectweb.asm.Label dflt, final org.objectweb.asm.Label[] labels) {
        // Label currentBlock = this.currentBlock;
        if (currentBlock != null) {
            if (compute == FRAMES) {
                currentBlock.frame.execute(org.objectweb.asm.Opcodes.LOOKUPSWITCH, 0, null, null);
                // adds current block successors
                addSuccessor(org.objectweb.asm.Edge.NORMAL, dflt);
                dflt.getFirst().status |= org.objectweb.asm.Label.TARGET;
                for (int i = 0; i < labels.length; ++i) {
                    addSuccessor(org.objectweb.asm.Edge.NORMAL, labels[i]);
                    labels[i].getFirst().status |= org.objectweb.asm.Label.TARGET;
                }
            } else {
                // updates current stack size (max stack size unchanged)
                --stackSize;
                // adds current block successors
                addSuccessor(stackSize, dflt);
                for (int i = 0; i < labels.length; ++i) {
                    addSuccessor(stackSize, labels[i]);
                }
            }
            // ends current block
            noSuccessor();
        }
    }

    @Override
    public void visitMultiANewArrayInsn(MultiANewArrayInsnNode manain) {
        lastCodeOffset = code.length;
        org.objectweb.asm.Item i = cw.newClassItem(manain.desc);
        // Label currentBlock = this.currentBlock;
        if (currentBlock != null) {
            if (compute == FRAMES) {
                currentBlock.frame.execute(org.objectweb.asm.Opcodes.MULTIANEWARRAY, manain.dims, cw, i);
            } else {
                // updates current stack size (max stack size unchanged because
                // stack size variation always negative or null)
                stackSize += 1 - manain.dims;
            }
        }
        // adds the instruction to the bytecode of the method
        code.put12(org.objectweb.asm.Opcodes.MULTIANEWARRAY, i.index).putByte(manain.dims);
    }

    @Override
    public AnnotationVisitor visitInsnAnnotation(TypeAnnotationNode tan, boolean visible) {
        if (!org.objectweb.asm.ClassReader.ANNOTATIONS) {
            return null;
        }
        org.objectweb.asm.ByteVector bv = new org.objectweb.asm.ByteVector();
        // write target_type and target_info
        int typeRef = tan.typeRef;
        typeRef = (typeRef & 0xFF0000FF) | (lastCodeOffset << 8);
        AnnotationWriter.putTarget(typeRef, tan.typePath, bv);
        // write type, and reserve space for values count
        bv.putShort(cw.newUTF8(tan.desc)).putShort(0);
        AnnotationWriter aw = new AnnotationWriter(cw, true, bv, bv, bv.length - 2);
        if (visible) {
            aw.next = ctanns;
            ctanns = aw;
        } else {
            aw.next = ictanns;
            ictanns = aw;
        }
        return aw;
    }

    @Override
    public void visitTryCatchBlock(TryCatchBlockNode tcbn) {
        ++handlerCount;
        org.objectweb.asm.Handler h = new org.objectweb.asm.Handler();
        h.start = tcbn.start.getLabel();
        h.end = tcbn.end.getLabel();
        h.handler = tcbn.handler.getLabel();
        h.desc = tcbn.type;
        h.type = tcbn.type != null ? cw.newClass(tcbn.type) : 0;
        if (lastHandler == null) {
            firstHandler = h;
        } else {
            lastHandler.next = h;
        }
        lastHandler = h;
    }

    @Override
    public AnnotationVisitor visitTryCatchAnnotation(TypeAnnotationNode tan, boolean visible) {
        if (!org.objectweb.asm.ClassReader.ANNOTATIONS) {
            return null;
        }
        org.objectweb.asm.ByteVector bv = new org.objectweb.asm.ByteVector();
        // write target_type and target_info
        AnnotationWriter.putTarget(tan.typeRef, tan.typePath, bv);
        // write type, and reserve space for values count
        bv.putShort(cw.newUTF8(tan.desc)).putShort(0);
        AnnotationWriter aw = new AnnotationWriter(cw, true, bv, bv, bv.length - 2);
        if (visible) {
            aw.next = ctanns;
            ctanns = aw;
        } else {
            aw.next = ictanns;
            ictanns = aw;
        }
        return aw;
    }

    @Override
    public void visitLocalVariable(LocalVariableNode lvn) {
        if (signature != null) {
            if (localVarType == null) {
                localVarType = new org.objectweb.asm.ByteVector();
            }
            ++localVarTypeCount;
            localVarType.putShort(lvn.start.getLabel().position)
                    .putShort(lvn.end.getLabel().position - lvn.start.getLabel().position)
                    .putShort(cw.newUTF8(lvn.name)).putShort(cw.newUTF8(signature)).putShort(lvn.index);
        }
        if (localVar == null) {
            localVar = new org.objectweb.asm.ByteVector();
        }
        ++localVarCount;
        localVar.putShort(lvn.start.getLabel().position)
                .putShort(lvn.end.getLabel().position - lvn.start.getLabel().position)
                .putShort(cw.newUTF8(lvn.name)).putShort(cw.newUTF8(lvn.desc)).putShort(lvn.index);
        if (compute != NOTHING) {
            // updates max locals
            char c = lvn.desc.charAt(0);
            int n = lvn.index + (c == 'J' || c == 'D' ? 2 : 1);
            if (n > maxLocals) {
                maxLocals = n;
            }
        }
    }

    @Override
    public AnnotationVisitor visitLocalVariableAnnotation(LocalVariableAnnotationNode lvan, boolean visible) {
        if (!org.objectweb.asm.ClassReader.ANNOTATIONS) {
            return null;
        }
        org.objectweb.asm.ByteVector bv = new org.objectweb.asm.ByteVector();
        // write target_type and target_info
        bv.putByte(lvan.typeRef >>> 24).putShort(lvan.start.size());
        for (int i = 0; i < lvan.start.size(); ++i) {
            bv.putShort(lvan.start.get(i).getLabel().position)
                    .putShort(lvan.end.get(i).getLabel().position - lvan.start.get(i).getLabel().position)
                    .putShort(lvan.index.get(i));
        }
        if (lvan.typePath == null) {
            bv.putByte(0);
        } else {
            int length = lvan.typePath.b[lvan.typePath.offset] * 2 + 1;
            bv.putByteArray(lvan.typePath.b, lvan.typePath.offset, length);
        }
        // write type, and reserve space for values count
        bv.putShort(cw.newUTF8(lvan.desc)).putShort(0);
        AnnotationWriter aw = new AnnotationWriter(cw, true, bv, bv, bv.length - 2);
        if (visible) {
            aw.next = ctanns;
            ctanns = aw;
        } else {
            aw.next = ictanns;
            ictanns = aw;
        }
        return aw;
    }

    @Override
    public void visitLineNumber(LineNumberNode lnn) {
        if (lineNumber == null) {
            lineNumber = new org.objectweb.asm.ByteVector();
        }
        ++lineNumberCount;
        lineNumber.putShort(lnn.start.getLabel().position);
        lineNumber.putShort(lnn.line);
    }

    @Override
    public void visitMaxs(final int maxStack, final int maxLocals) {
        if (resize) {
            // replaces the temporary jump opcodes introduced by Label.resolve.
            if (org.objectweb.asm.ClassReader.RESIZE) {
                resizeInstructions();
            } else {
                throw new RuntimeException("Method code too large!");
            }
        }
        if (org.objectweb.asm.ClassReader.FRAMES && compute == FRAMES) {
            // completes the control flow graph with exception handler blocks
            org.objectweb.asm.Handler handler = firstHandler;
            while (handler != null) {
                org.objectweb.asm.Label l = handler.start.getFirst();
                org.objectweb.asm.Label h = handler.handler.getFirst();
                org.objectweb.asm.Label e = handler.end.getFirst();
                // computes the kind of the edges to 'h'
                String t = handler.desc == null ? "java/lang/Throwable"
                        : handler.desc;
                int kind = org.objectweb.asm.Frame.OBJECT | cw.addType(t);
                // h is an exception handler
                h.status |= org.objectweb.asm.Label.TARGET;
                // adds 'h' as a successor of labels between 'start' and 'end'
                while (l != e) {
                    // creates an edge to 'h'
                    org.objectweb.asm.Edge b = new org.objectweb.asm.Edge();
                    b.info = kind;
                    b.successor = h;
                    // adds it to the successors of 'l'
                    b.next = l.successors;
                    l.successors = b;
                    // goes to the next label
                    l = l.successor;
                }
                handler = handler.next;
            }

            // creates and visits the first (implicit) frame
            org.objectweb.asm.Frame f = labels.frame;
            org.objectweb.asm.Type[] args = org.objectweb.asm.Type.getArgumentTypes(descriptor);
            f.initInputFrame(cw, access, args, this.maxLocals);
            visitFrame(f);

            /*
             * fix point algorithm: mark the first basic block as 'changed'
             * (i.e. put it in the 'changed' list) and, while there are changed
             * basic blocks, choose one, mark it as unchanged, and update its
             * successors (which can be changed in the process).
             */
            int max = 0;
            org.objectweb.asm.Label changed = labels;
            while (changed != null) {
                // removes a basic block from the list of changed basic blocks
                org.objectweb.asm.Label l = changed;
                changed = changed.next;
                l.next = null;
                f = l.frame;
                // a reachable jump target must be stored in the stack map
                if ((l.status & org.objectweb.asm.Label.TARGET) != 0) {
                    l.status |= org.objectweb.asm.Label.STORE;
                }
                // all visited labels are reachable, by definition
                l.status |= org.objectweb.asm.Label.REACHABLE;
                // updates the (absolute) maximum stack size
                int blockMax = f.inputStack.length + l.outputStackMax;
                if (blockMax > max) {
                    max = blockMax;
                }
                // updates the successors of the current basic block
                org.objectweb.asm.Edge e = l.successors;
                while (e != null) {
                    org.objectweb.asm.Label n = e.successor.getFirst();
                    boolean change = f.merge(cw, n.frame, e.info);
                    if (change && n.next == null) {
                        // if n has changed and is not already in the 'changed'
                        // list, adds it to this list
                        n.next = changed;
                        changed = n;
                    }
                    e = e.next;
                }
            }

            // visits all the frames that must be stored in the stack map
            org.objectweb.asm.Label l = labels;
            while (l != null) {
                f = l.frame;
                if ((l.status & org.objectweb.asm.Label.STORE) != 0) {
                    visitFrame(f);
                }
                if ((l.status & org.objectweb.asm.Label.REACHABLE) == 0) {
                    // finds start and end of dead basic block
                    org.objectweb.asm.Label k = l.successor;
                    int start = l.position;
                    int end = (k == null ? code.length : k.position) - 1;
                    // if non empty basic block
                    if (end >= start) {
                        max = Math.max(max, 1);
                        // replaces instructions with NOP ... NOP ATHROW
                        for (int i = start; i < end; ++i) {
                            code.data[i] = org.objectweb.asm.Opcodes.NOP;
                        }
                        code.data[end] = (byte) org.objectweb.asm.Opcodes.ATHROW;
                        // emits a frame for this unreachable block
                        int frameIndex = startFrame(start, 0, 1);
                        frame[frameIndex] = org.objectweb.asm.Frame.OBJECT
                                | cw.addType("java/lang/Throwable");
                        endFrame();
                        // removes the start-end range from the exception
                        // handlers
                        firstHandler = org.objectweb.asm.Handler.remove(firstHandler, l, k);
                    }
                }
                l = l.successor;
            }

            handler = firstHandler;
            handlerCount = 0;
            while (handler != null) {
                handlerCount += 1;
                handler = handler.next;
            }

            this.maxStack = max;
        } else if (compute == MAXS) {
            // completes the control flow graph with exception handler blocks
            org.objectweb.asm.Handler handler = firstHandler;
            while (handler != null) {
                org.objectweb.asm.Label l = handler.start;
                org.objectweb.asm.Label h = handler.handler;
                org.objectweb.asm.Label e = handler.end;
                // adds 'h' as a successor of labels between 'start' and 'end'
                while (l != e) {
                    // creates an edge to 'h'
                    org.objectweb.asm.Edge b = new org.objectweb.asm.Edge();
                    b.info = org.objectweb.asm.Edge.EXCEPTION;
                    b.successor = h;
                    // adds it to the successors of 'l'
                    if ((l.status & org.objectweb.asm.Label.JSR) == 0) {
                        b.next = l.successors;
                        l.successors = b;
                    } else {
                        // if l is a JSR block, adds b after the first two edges
                        // to preserve the hypothesis about JSR block successors
                        // order (see {@link #visitJumpInsn})
                        b.next = l.successors.next.next;
                        l.successors.next.next = b;
                    }
                    // goes to the next label
                    l = l.successor;
                }
                handler = handler.next;
            }

            if (subroutines > 0) {
                // completes the control flow graph with the RET successors
                /*
                 * first step: finds the subroutines. This step determines, for
                 * each basic block, to which subroutine(s) it belongs.
                 */
                // finds the basic blocks that belong to the "main" subroutine
                int id = 0;
                labels.visitSubroutine(null, 1, subroutines);
                // finds the basic blocks that belong to the real subroutines
                org.objectweb.asm.Label l = labels;
                while (l != null) {
                    if ((l.status & org.objectweb.asm.Label.JSR) != 0) {
                        // the subroutine is defined by l's TARGET, not by l
                        org.objectweb.asm.Label subroutine = l.successors.next.successor;
                        // if this subroutine has not been visited yet...
                        if ((subroutine.status & org.objectweb.asm.Label.VISITED) == 0) {
                            // ...assigns it a new id and finds its basic blocks
                            id += 1;
                            subroutine.visitSubroutine(null, (id / 32L) << 32
                                    | (1L << (id % 32)), subroutines);
                        }
                    }
                    l = l.successor;
                }
                // second step: finds the successors of RET blocks
                l = labels;
                while (l != null) {
                    if ((l.status & org.objectweb.asm.Label.JSR) != 0) {
                        org.objectweb.asm.Label L = labels;
                        while (L != null) {
                            L.status &= ~org.objectweb.asm.Label.VISITED2;
                            L = L.successor;
                        }
                        // the subroutine is defined by l's TARGET, not by l
                        org.objectweb.asm.Label subroutine = l.successors.next.successor;
                        subroutine.visitSubroutine(l, 0, subroutines);
                    }
                    l = l.successor;
                }
            }

            /*
             * control flow analysis algorithm: while the block stack is not
             * empty, pop a block from this stack, update the max stack size,
             * compute the true (non relative) begin stack size of the
             * successors of this block, and push these successors onto the
             * stack (unless they have already been pushed onto the stack).
             * Note: by hypothesis, the {@link Label#inputStackTop} of the
             * blocks in the block stack are the true (non relative) beginning
             * stack sizes of these blocks.
             */
            int max = 0;
            org.objectweb.asm.Label stack = labels;
            while (stack != null) {
                // pops a block from the stack
                org.objectweb.asm.Label l = stack;
                stack = stack.next;
                // computes the true (non relative) max stack size of this block
                int start = l.inputStackTop;
                int blockMax = start + l.outputStackMax;
                // updates the global max stack size
                if (blockMax > max) {
                    max = blockMax;
                }
                // analyzes the successors of the block
                org.objectweb.asm.Edge b = l.successors;
                if ((l.status & org.objectweb.asm.Label.JSR) != 0) {
                    // ignores the first edge of JSR blocks (virtual successor)
                    b = b.next;
                }
                while (b != null) {
                    l = b.successor;
                    // if this successor has not already been pushed...
                    if ((l.status & org.objectweb.asm.Label.PUSHED) == 0) {
                        // computes its true beginning stack size...
                        l.inputStackTop = b.info == org.objectweb.asm.Edge.EXCEPTION ? 1 : start
                                + b.info;
                        // ...and pushes it onto the stack
                        l.status |= org.objectweb.asm.Label.PUSHED;
                        l.next = stack;
                        stack = l;
                    }
                    b = b.next;
                }
            }
            this.maxStack = Math.max(maxStack, max);
        } else {
            this.maxStack = maxStack;
            this.maxLocals = maxLocals;
        }
    }

    @Override
    public void visitEnd() {
    }

    // ------------------------------------------------------------------------
    // Utility methods: control flow analysis algorithm
    // ------------------------------------------------------------------------

    /**
     * Adds a successor to the {@link #currentBlock currentBlock} block.
     *
     * @param info
     *            information about the control flow edge to be added.
     * @param successor
     *            the successor block to be added to the current block.
     */
    private void addSuccessor(final int info, final org.objectweb.asm.Label successor) {
        // creates and initializes an Edge object...
        org.objectweb.asm.Edge b = new org.objectweb.asm.Edge();
        b.info = info;
        b.successor = successor;
        // ...and adds it to the successor list of the currentBlock block
        b.next = currentBlock.successors;
        currentBlock.successors = b;
    }

    /**
     * Ends the current basic block. This method must be used in the case where
     * the current basic block does not have any successor.
     */
    private void noSuccessor() {
        if (compute == FRAMES) {
            org.objectweb.asm.Label l = new org.objectweb.asm.Label();
            l.frame = new org.objectweb.asm.Frame();
            l.frame.owner = l;
            l.resolve(this, code.length, code.data);
            previousBlock.successor = l;
            previousBlock = l;
        } else {
            currentBlock.outputStackMax = maxStackSize;
        }
        currentBlock = null;
    }

    // ------------------------------------------------------------------------
    // Utility methods: stack map frames
    // ------------------------------------------------------------------------

    /**
     * Visits a frame that has been computed from scratch.
     *
     * @param f
     *            the frame that must be visited.
     */
    private void visitFrame(final org.objectweb.asm.Frame f) {
        int i, t;
        int nTop = 0;
        int nLocal = 0;
        int nStack = 0;
        int[] locals = f.inputLocals;
        int[] stacks = f.inputStack;
        // computes the number of locals (ignores TOP types that are just after
        // a LONG or a DOUBLE, and all trailing TOP types)
        for (i = 0; i < locals.length; ++i) {
            t = locals[i];
            if (t == org.objectweb.asm.Frame.TOP) {
                ++nTop;
            } else {
                nLocal += nTop + 1;
                nTop = 0;
            }
            if (t == org.objectweb.asm.Frame.LONG || t == org.objectweb.asm.Frame.DOUBLE) {
                ++i;
            }
        }
        // computes the stack size (ignores TOP types that are just after
        // a LONG or a DOUBLE)
        for (i = 0; i < stacks.length; ++i) {
            t = stacks[i];
            ++nStack;
            if (t == org.objectweb.asm.Frame.LONG || t == org.objectweb.asm.Frame.DOUBLE) {
                ++i;
            }
        }
        // visits the frame and its content
        int frameIndex = startFrame(f.owner.position, nLocal, nStack);
        for (i = 0; nLocal > 0; ++i, --nLocal) {
            t = locals[i];
            frame[frameIndex++] = t;
            if (t == org.objectweb.asm.Frame.LONG || t == org.objectweb.asm.Frame.DOUBLE) {
                ++i;
            }
        }
        for (i = 0; i < stacks.length; ++i) {
            t = stacks[i];
            frame[frameIndex++] = t;
            if (t == org.objectweb.asm.Frame.LONG || t == org.objectweb.asm.Frame.DOUBLE) {
                ++i;
            }
        }
        endFrame();
    }

    /**
     * Visit the implicit first frame of this method.
     */
    private void visitImplicitFirstFrame() {
        // There can be at most descriptor.length() + 1 locals
        int frameIndex = startFrame(0, descriptor.length() + 1, 0);
        if ((access & org.objectweb.asm.Opcodes.ACC_STATIC) == 0) {
            if ((access & ACC_CONSTRUCTOR) == 0) {
                frame[frameIndex++] = org.objectweb.asm.Frame.OBJECT | cw.addType(cw.thisName);
            } else {
                frame[frameIndex++] = 6; // Opcodes.UNINITIALIZED_THIS;
            }
        }
        int i = 1;
        loop:
        while (true) {
            int j = i;
            switch (descriptor.charAt(i++)) {
                case 'Z':
                case 'C':
                case 'B':
                case 'S':
                case 'I':
                    frame[frameIndex++] = 1; // Opcodes.INTEGER;
                    break;
                case 'F':
                    frame[frameIndex++] = 2; // Opcodes.FLOAT;
                    break;
                case 'J':
                    frame[frameIndex++] = 4; // Opcodes.LONG;
                    break;
                case 'D':
                    frame[frameIndex++] = 3; // Opcodes.DOUBLE;
                    break;
                case '[':
                    while (descriptor.charAt(i) == '[') {
                        ++i;
                    }
                    if (descriptor.charAt(i) == 'L') {
                        ++i;
                        while (descriptor.charAt(i) != ';') {
                            ++i;
                        }
                    }
                    frame[frameIndex++] = org.objectweb.asm.Frame.OBJECT
                            | cw.addType(descriptor.substring(j, ++i));
                    break;
                case 'L':
                    while (descriptor.charAt(i) != ';') {
                        ++i;
                    }
                    frame[frameIndex++] = org.objectweb.asm.Frame.OBJECT
                            | cw.addType(descriptor.substring(j + 1, i++));
                    break;
                default:
                    break loop;
            }
        }
        frame[1] = frameIndex - 3;
        endFrame();
    }

    /**
     * Starts the visit of a stack map frame.
     *
     * @param offset
     *            the offset of the instruction to which the frame corresponds.
     * @param nLocal
     *            the number of local variables in the frame.
     * @param nStack
     *            the number of stack elements in the frame.
     * @return the index of the next element to be written in this frame.
     */
    private int startFrame(final int offset, final int nLocal, final int nStack) {
        int n = 3 + nLocal + nStack;
        if (frame == null || frame.length < n) {
            frame = new int[n];
        }
        frame[0] = offset;
        frame[1] = nLocal;
        frame[2] = nStack;
        return 3;
    }

    /**
     * Checks if the visit of the current frame {@link #frame} is finished, and
     * if yes, write it in the StackMapTable attribute.
     */
    private void endFrame() {
        if (previousFrame != null) { // do not write the first frame
            if (stackMap == null) {
                stackMap = new org.objectweb.asm.ByteVector();
            }
            writeFrame();
            ++frameCount;
        }
        previousFrame = frame;
        frame = null;
    }

    /**
     * Compress and writes the current frame {@link #frame} in the StackMapTable
     * attribute.
     */
    private void writeFrame() {
        int clocalsSize = frame[1];
        int cstackSize = frame[2];
        if ((cw.version & 0xFFFF) < org.objectweb.asm.Opcodes.V1_6) {
            stackMap.putShort(frame[0]).putShort(clocalsSize);
            writeFrameTypes(3, 3 + clocalsSize);
            stackMap.putShort(cstackSize);
            writeFrameTypes(3 + clocalsSize, 3 + clocalsSize + cstackSize);
            return;
        }
        int localsSize = previousFrame[1];
        int type = FULL_FRAME;
        int k = 0;
        int delta;
        if (frameCount == 0) {
            delta = frame[0];
        } else {
            delta = frame[0] - previousFrame[0] - 1;
        }
        if (cstackSize == 0) {
            k = clocalsSize - localsSize;
            switch (k) {
                case -3:
                case -2:
                case -1:
                    type = CHOP_FRAME;
                    localsSize = clocalsSize;
                    break;
                case 0:
                    type = delta < 64 ? SAME_FRAME : SAME_FRAME_EXTENDED;
                    break;
                case 1:
                case 2:
                case 3:
                    type = APPEND_FRAME;
                    break;
            }
        } else if (clocalsSize == localsSize && cstackSize == 1) {
            type = delta < 63 ? SAME_LOCALS_1_STACK_ITEM_FRAME
                    : SAME_LOCALS_1_STACK_ITEM_FRAME_EXTENDED;
        }
        if (type != FULL_FRAME) {
            // verify if locals are the same
            int l = 3;
            for (int j = 0; j < localsSize; j++) {
                if (frame[l] != previousFrame[l]) {
                    type = FULL_FRAME;
                    break;
                }
                l++;
            }
        }
        switch (type) {
            case SAME_FRAME:
                stackMap.putByte(delta);
                break;
            case SAME_LOCALS_1_STACK_ITEM_FRAME:
                stackMap.putByte(SAME_LOCALS_1_STACK_ITEM_FRAME + delta);
                writeFrameTypes(3 + clocalsSize, 4 + clocalsSize);
                break;
            case SAME_LOCALS_1_STACK_ITEM_FRAME_EXTENDED:
                stackMap.putByte(SAME_LOCALS_1_STACK_ITEM_FRAME_EXTENDED).putShort(
                        delta);
                writeFrameTypes(3 + clocalsSize, 4 + clocalsSize);
                break;
            case SAME_FRAME_EXTENDED:
                stackMap.putByte(SAME_FRAME_EXTENDED).putShort(delta);
                break;
            case CHOP_FRAME:
                stackMap.putByte(SAME_FRAME_EXTENDED + k).putShort(delta);
                break;
            case APPEND_FRAME:
                stackMap.putByte(SAME_FRAME_EXTENDED + k).putShort(delta);
                writeFrameTypes(3 + localsSize, 3 + clocalsSize);
                break;
            // case FULL_FRAME:
            default:
                stackMap.putByte(FULL_FRAME).putShort(delta).putShort(clocalsSize);
                writeFrameTypes(3, 3 + clocalsSize);
                stackMap.putShort(cstackSize);
                writeFrameTypes(3 + clocalsSize, 3 + clocalsSize + cstackSize);
        }
    }

    /**
     * Writes some types of the current frame {@link #frame} into the
     * StackMapTableAttribute. This method converts types from the format used
     * in {@link org.objectweb.asm.Label} to the format used in StackMapTable attributes. In
     * particular, it converts type table indexes to constant pool indexes.
     *
     * @param start
     *            index of the first type in {@link #frame} to write.
     * @param end
     *            index of last type in {@link #frame} to write (exclusive).
     */
    private void writeFrameTypes(final int start, final int end) {
        for (int i = start; i < end; ++i) {
            int t = frame[i];
            int d = t & org.objectweb.asm.Frame.DIM;
            if (d == 0) {
                int v = t & org.objectweb.asm.Frame.BASE_VALUE;
                switch (t & org.objectweb.asm.Frame.BASE_KIND) {
                    case org.objectweb.asm.Frame.OBJECT:
                        stackMap.putByte(7).putShort(
                                cw.newClass(cw.typeTable[v].strVal1));
                        break;
                    case org.objectweb.asm.Frame.UNINITIALIZED:
                        stackMap.putByte(8).putShort(cw.typeTable[v].intVal);
                        break;
                    default:
                        stackMap.putByte(v);
                }
            } else {
                StringBuffer buf = new StringBuffer();
                d >>= 28;
                while (d-- > 0) {
                    buf.append('[');
                }
                if ((t & org.objectweb.asm.Frame.BASE_KIND) == org.objectweb.asm.Frame.OBJECT) {
                    buf.append('L');
                    buf.append(cw.typeTable[t & org.objectweb.asm.Frame.BASE_VALUE].strVal1);
                    buf.append(';');
                } else {
                    switch (t & 0xF) {
                        case 1:
                            buf.append('I');
                            break;
                        case 2:
                            buf.append('F');
                            break;
                        case 3:
                            buf.append('D');
                            break;
                        case 9:
                            buf.append('Z');
                            break;
                        case 10:
                            buf.append('B');
                            break;
                        case 11:
                            buf.append('C');
                            break;
                        case 12:
                            buf.append('S');
                            break;
                        default:
                            buf.append('J');
                    }
                }
                stackMap.putByte(7).putShort(cw.newClass(buf.toString()));
            }
        }
    }

    private void writeFrameType(final Object type) {
        if (type instanceof String) {
            stackMap.putByte(7).putShort(cw.newClass((String) type));
        } else if (type instanceof Integer) {
            stackMap.putByte(((Integer) type).intValue());
        } else {
            stackMap.putByte(8).putShort(((org.objectweb.asm.Label) type).position);
        }
    }

    // ------------------------------------------------------------------------
    // Utility methods: dump bytecode array
    // ------------------------------------------------------------------------

    /**
     * Returns the size of the bytecode of this method.
     *
     * @return the size of the bytecode of this method.
     */
    final int getSize() {
        if (classReaderOffset != 0) {
            return 6 + classReaderLength;
        }
        int size = 8;
        if (code.length > 0) {
            if (code.length > 65536) {
                throw new RuntimeException("Method code too large!");
            }
            cw.newUTF8("Code");
            size += 18 + code.length + 8 * handlerCount;
            if (localVar != null) {
                cw.newUTF8("LocalVariableTable");
                size += 8 + localVar.length;
            }
            if (localVarType != null) {
                cw.newUTF8("LocalVariableTypeTable");
                size += 8 + localVarType.length;
            }
            if (lineNumber != null) {
                cw.newUTF8("LineNumberTable");
                size += 8 + lineNumber.length;
            }
            if (stackMap != null) {
                boolean zip = (cw.version & 0xFFFF) >= org.objectweb.asm.Opcodes.V1_6;
                cw.newUTF8(zip ? "StackMapTable" : "StackMap");
                size += 8 + stackMap.length;
            }
            if (org.objectweb.asm.ClassReader.ANNOTATIONS && ctanns != null) {
                cw.newUTF8("RuntimeVisibleTypeAnnotations");
                size += 8 + ctanns.getSize();
            }
            if (org.objectweb.asm.ClassReader.ANNOTATIONS && ictanns != null) {
                cw.newUTF8("RuntimeInvisibleTypeAnnotations");
                size += 8 + ictanns.getSize();
            }
            if (cattrs != null) {
                size += cattrs.getSize(cw, code.data, code.length, maxStack,
                        maxLocals);
            }
        }
        if (exceptionCount > 0) {
            cw.newUTF8("Exceptions");
            size += 8 + 2 * exceptionCount;
        }
        if ((access & org.objectweb.asm.Opcodes.ACC_SYNTHETIC) != 0) {
            if ((cw.version & 0xFFFF) < org.objectweb.asm.Opcodes.V1_5
                    || (access & org.objectweb.asm.ClassWriter.ACC_SYNTHETIC_ATTRIBUTE) != 0) {
                cw.newUTF8("Synthetic");
                size += 6;
            }
        }
        if ((access & org.objectweb.asm.Opcodes.ACC_DEPRECATED) != 0) {
            cw.newUTF8("Deprecated");
            size += 6;
        }
        if (org.objectweb.asm.ClassReader.SIGNATURES && signature != null) {
            cw.newUTF8("Signature");
            cw.newUTF8(signature);
            size += 8;
        }
        if (methodParameters != null) {
            cw.newUTF8("MethodParameters");
            size += 7 + methodParameters.length;
        }
        if (org.objectweb.asm.ClassReader.ANNOTATIONS && annd != null) {
            cw.newUTF8("AnnotationDefault");
            size += 6 + annd.length;
        }
        if (org.objectweb.asm.ClassReader.ANNOTATIONS && anns != null) {
            cw.newUTF8("RuntimeVisibleAnnotations");
            size += 8 + anns.getSize();
        }
        if (org.objectweb.asm.ClassReader.ANNOTATIONS && ianns != null) {
            cw.newUTF8("RuntimeInvisibleAnnotations");
            size += 8 + ianns.getSize();
        }
        if (org.objectweb.asm.ClassReader.ANNOTATIONS && tanns != null) {
            cw.newUTF8("RuntimeVisibleTypeAnnotations");
            size += 8 + tanns.getSize();
        }
        if (org.objectweb.asm.ClassReader.ANNOTATIONS && itanns != null) {
            cw.newUTF8("RuntimeInvisibleTypeAnnotations");
            size += 8 + itanns.getSize();
        }
        if (org.objectweb.asm.ClassReader.ANNOTATIONS && panns != null) {
            cw.newUTF8("RuntimeVisibleParameterAnnotations");
            size += 7 + 2 * (panns.length - synthetics);
            for (int i = panns.length - 1; i >= synthetics; --i) {
                size += panns[i] == null ? 0 : panns[i].getSize();
            }
        }
        if (org.objectweb.asm.ClassReader.ANNOTATIONS && ipanns != null) {
            cw.newUTF8("RuntimeInvisibleParameterAnnotations");
            size += 7 + 2 * (ipanns.length - synthetics);
            for (int i = ipanns.length - 1; i >= synthetics; --i) {
                size += ipanns[i] == null ? 0 : ipanns[i].getSize();
            }
        }
        if (attrs != null) {
            size += attrs.getSize(cw, null, 0, -1, -1);
        }
        return size;
    }

    /**
     * Puts the bytecode of this method in the given byte vector.
     *
     * @param out
     *            the byte vector into which the bytecode of this method must be
     *            copied.
     */
    final void put(final org.objectweb.asm.ByteVector out) {
        final int FACTOR = org.objectweb.asm.ClassWriter.TO_ACC_SYNTHETIC;
        int mask = ACC_CONSTRUCTOR | org.objectweb.asm.Opcodes.ACC_DEPRECATED
                | org.objectweb.asm.ClassWriter.ACC_SYNTHETIC_ATTRIBUTE
                | ((access & org.objectweb.asm.ClassWriter.ACC_SYNTHETIC_ATTRIBUTE) / FACTOR);
        out.putShort(access & ~mask).putShort(name).putShort(desc);
        if (classReaderOffset != 0) {
            out.putByteArray(cw.cr.b, classReaderOffset, classReaderLength);
            return;
        }
        int attributeCount = 0;
        if (code.length > 0) {
            ++attributeCount;
        }
        if (exceptionCount > 0) {
            ++attributeCount;
        }
        if ((access & org.objectweb.asm.Opcodes.ACC_SYNTHETIC) != 0) {
            if ((cw.version & 0xFFFF) < org.objectweb.asm.Opcodes.V1_5
                    || (access & org.objectweb.asm.ClassWriter.ACC_SYNTHETIC_ATTRIBUTE) != 0) {
                ++attributeCount;
            }
        }
        if ((access & org.objectweb.asm.Opcodes.ACC_DEPRECATED) != 0) {
            ++attributeCount;
        }
        if (org.objectweb.asm.ClassReader.SIGNATURES && signature != null) {
            ++attributeCount;
        }
        if (methodParameters != null) {
            ++attributeCount;
        }
        if (org.objectweb.asm.ClassReader.ANNOTATIONS && annd != null) {
            ++attributeCount;
        }
        if (org.objectweb.asm.ClassReader.ANNOTATIONS && anns != null) {
            ++attributeCount;
        }
        if (org.objectweb.asm.ClassReader.ANNOTATIONS && ianns != null) {
            ++attributeCount;
        }
        if (org.objectweb.asm.ClassReader.ANNOTATIONS && tanns != null) {
            ++attributeCount;
        }
        if (org.objectweb.asm.ClassReader.ANNOTATIONS && itanns != null) {
            ++attributeCount;
        }
        if (org.objectweb.asm.ClassReader.ANNOTATIONS && panns != null) {
            ++attributeCount;
        }
        if (org.objectweb.asm.ClassReader.ANNOTATIONS && ipanns != null) {
            ++attributeCount;
        }
        if (attrs != null) {
            attributeCount += attrs.getCount();
        }
        out.putShort(attributeCount);
        if (code.length > 0) {
            int size = 12 + code.length + 8 * handlerCount;
            if (localVar != null) {
                size += 8 + localVar.length;
            }
            if (localVarType != null) {
                size += 8 + localVarType.length;
            }
            if (lineNumber != null) {
                size += 8 + lineNumber.length;
            }
            if (stackMap != null) {
                size += 8 + stackMap.length;
            }
            if (org.objectweb.asm.ClassReader.ANNOTATIONS && ctanns != null) {
                size += 8 + ctanns.getSize();
            }
            if (org.objectweb.asm.ClassReader.ANNOTATIONS && ictanns != null) {
                size += 8 + ictanns.getSize();
            }
            if (cattrs != null) {
                size += cattrs.getSize(cw, code.data, code.length, maxStack,
                        maxLocals);
            }
            out.putShort(cw.newUTF8("Code")).putInt(size);
            out.putShort(maxStack).putShort(maxLocals);
            out.putInt(code.length).putByteArray(code.data, 0, code.length);
            out.putShort(handlerCount);
            if (handlerCount > 0) {
                org.objectweb.asm.Handler h = firstHandler;
                while (h != null) {
                    out.putShort(h.start.position).putShort(h.end.position)
                            .putShort(h.handler.position).putShort(h.type);
                    h = h.next;
                }
            }
            attributeCount = 0;
            if (localVar != null) {
                ++attributeCount;
            }
            if (localVarType != null) {
                ++attributeCount;
            }
            if (lineNumber != null) {
                ++attributeCount;
            }
            if (stackMap != null) {
                ++attributeCount;
            }
            if (org.objectweb.asm.ClassReader.ANNOTATIONS && ctanns != null) {
                ++attributeCount;
            }
            if (org.objectweb.asm.ClassReader.ANNOTATIONS && ictanns != null) {
                ++attributeCount;
            }
            if (cattrs != null) {
                attributeCount += cattrs.getCount();
            }
            out.putShort(attributeCount);
            if (localVar != null) {
                out.putShort(cw.newUTF8("LocalVariableTable"));
                out.putInt(localVar.length + 2).putShort(localVarCount);
                out.putByteArray(localVar.data, 0, localVar.length);
            }
            if (localVarType != null) {
                out.putShort(cw.newUTF8("LocalVariableTypeTable"));
                out.putInt(localVarType.length + 2).putShort(localVarTypeCount);
                out.putByteArray(localVarType.data, 0, localVarType.length);
            }
            if (lineNumber != null) {
                out.putShort(cw.newUTF8("LineNumberTable"));
                out.putInt(lineNumber.length + 2).putShort(lineNumberCount);
                out.putByteArray(lineNumber.data, 0, lineNumber.length);
            }
            if (stackMap != null) {
                boolean zip = (cw.version & 0xFFFF) >= org.objectweb.asm.Opcodes.V1_6;
                out.putShort(cw.newUTF8(zip ? "StackMapTable" : "StackMap"));
                out.putInt(stackMap.length + 2).putShort(frameCount);
                out.putByteArray(stackMap.data, 0, stackMap.length);
            }
            if (org.objectweb.asm.ClassReader.ANNOTATIONS && ctanns != null) {
                out.putShort(cw.newUTF8("RuntimeVisibleTypeAnnotations"));
                ctanns.put(out);
            }
            if (org.objectweb.asm.ClassReader.ANNOTATIONS && ictanns != null) {
                out.putShort(cw.newUTF8("RuntimeInvisibleTypeAnnotations"));
                ictanns.put(out);
            }
            if (cattrs != null) {
                cattrs.put(cw, code.data, code.length, maxLocals, maxStack, out);
            }
        }
        if (exceptionCount > 0) {
            out.putShort(cw.newUTF8("Exceptions")).putInt(
                    2 * exceptionCount + 2);
            out.putShort(exceptionCount);
            for (int i = 0; i < exceptionCount; ++i) {
                out.putShort(exceptions[i]);
            }
        }
        if ((access & org.objectweb.asm.Opcodes.ACC_SYNTHETIC) != 0) {
            if ((cw.version & 0xFFFF) < org.objectweb.asm.Opcodes.V1_5
                    || (access & org.objectweb.asm.ClassWriter.ACC_SYNTHETIC_ATTRIBUTE) != 0) {
                out.putShort(cw.newUTF8("Synthetic")).putInt(0);
            }
        }
        if ((access & org.objectweb.asm.Opcodes.ACC_DEPRECATED) != 0) {
            out.putShort(cw.newUTF8("Deprecated")).putInt(0);
        }
        if (org.objectweb.asm.ClassReader.SIGNATURES && signature != null) {
            out.putShort(cw.newUTF8("Signature")).putInt(2)
                    .putShort(cw.newUTF8(signature));
        }
        if (methodParameters != null) {
            out.putShort(cw.newUTF8("MethodParameters"));
            out.putInt(methodParameters.length + 1).putByte(
                    methodParametersCount);
            out.putByteArray(methodParameters.data, 0, methodParameters.length);
        }
        if (org.objectweb.asm.ClassReader.ANNOTATIONS && annd != null) {
            out.putShort(cw.newUTF8("AnnotationDefault"));
            out.putInt(annd.length);
            out.putByteArray(annd.data, 0, annd.length);
        }
        if (org.objectweb.asm.ClassReader.ANNOTATIONS && anns != null) {
            out.putShort(cw.newUTF8("RuntimeVisibleAnnotations"));
            anns.put(out);
        }
        if (org.objectweb.asm.ClassReader.ANNOTATIONS && ianns != null) {
            out.putShort(cw.newUTF8("RuntimeInvisibleAnnotations"));
            ianns.put(out);
        }
        if (org.objectweb.asm.ClassReader.ANNOTATIONS && tanns != null) {
            out.putShort(cw.newUTF8("RuntimeVisibleTypeAnnotations"));
            tanns.put(out);
        }
        if (org.objectweb.asm.ClassReader.ANNOTATIONS && itanns != null) {
            out.putShort(cw.newUTF8("RuntimeInvisibleTypeAnnotations"));
            itanns.put(out);
        }
        if (org.objectweb.asm.ClassReader.ANNOTATIONS && panns != null) {
            out.putShort(cw.newUTF8("RuntimeVisibleParameterAnnotations"));
            AnnotationWriter.put(panns, synthetics, out);
        }
        if (ClassReader.ANNOTATIONS && ipanns != null) {
            out.putShort(cw.newUTF8("RuntimeInvisibleParameterAnnotations"));
            AnnotationWriter.put(ipanns, synthetics, out);
        }
        if (attrs != null) {
            attrs.put(cw, null, 0, -1, -1, out);
        }
    }

    // ------------------------------------------------------------------------
    // Utility methods: instruction resizing (used to handle GOTO_W and JSR_W)
    // ------------------------------------------------------------------------

    /**
     * Resizes and replaces the temporary instructions inserted by
     * {@link org.objectweb.asm.Label#resolve} for wide forward jumps, while keeping jump offsets
     * and instruction addresses consistent. This may require to resize other
     * existing instructions, or even to introduce new instructions: for
     * example, increasing the size of an instruction by 2 at the middle of a
     * method can increases the offset of an IFEQ instruction from 32766 to
     * 32768, in which case IFEQ 32766 must be replaced with IFNEQ 8 GOTO_W
     * 32765. This, in turn, may require to increase the size of another jump
     * instruction, and so on... All these operations are handled automatically
     * by this method.
     * <p>
     * <i>This method must be called after all the method that is being built
     * has been visited</i>. In particular, the {@link org.objectweb.asm.Label Label} objects used
     * to construct the method are no longer valid after this method has been
     * called.
     */
    private void resizeInstructions() {
        byte[] b = code.data; // bytecode of the method
        int u, v, label; // indexes in b
        int i, j; // loop indexes
        /*
         * 1st step: As explained above, resizing an instruction may require to
         * resize another one, which may require to resize yet another one, and
         * so on. The first step of the algorithm consists in finding all the
         * instructions that need to be resized, without modifying the code.
         * This is done by the following "fix point" algorithm:
         * 
         * Parse the code to find the jump instructions whose offset will need
         * more than 2 bytes to be stored (the future offset is computed from
         * the current offset and from the number of bytes that will be inserted
         * or removed between the source and target instructions). For each such
         * instruction, adds an entry in (a copy of) the indexes and sizes
         * arrays (if this has not already been done in a previous iteration!).
         * 
         * If at least one entry has been added during the previous step, go
         * back to the beginning, otherwise stop.
         * 
         * In fact the real algorithm is complicated by the fact that the size
         * of TABLESWITCH and LOOKUPSWITCH instructions depends on their
         * position in the bytecode (because of padding). In order to ensure the
         * convergence of the algorithm, the number of bytes to be added or
         * removed from these instructions is over estimated during the previous
         * loop, and computed exactly only after the loop is finished (this
         * requires another pass to parse the bytecode of the method).
         */
        int[] allIndexes = new int[0]; // copy of indexes
        int[] allSizes = new int[0]; // copy of sizes
        boolean[] resize; // instructions to be resized
        int newOffset; // future offset of a jump instruction

        resize = new boolean[code.length];

        // 3 = loop again, 2 = loop ended, 1 = last pass, 0 = done
        int state = 3;
        do {
            if (state == 3) {
                state = 2;
            }
            u = 0;
            while (u < b.length) {
                int opcode = b[u] & 0xFF; // opcode of current instruction
                int insert = 0; // bytes to be added after this instruction

                switch (org.objectweb.asm.ClassWriter.TYPE[opcode]) {
                    case org.objectweb.asm.ClassWriter.NOARG_INSN:
                    case org.objectweb.asm.ClassWriter.IMPLVAR_INSN:
                        u += 1;
                        break;
                    case org.objectweb.asm.ClassWriter.LABEL_INSN:
                        if (opcode > 201) {
                            // converts temporary opcodes 202 to 217, 218 and
                            // 219 to IFEQ ... JSR (inclusive), IFNULL and
                            // IFNONNULL
                            opcode = opcode < 218 ? opcode - 49 : opcode - 20;
                            label = u + readUnsignedShort(b, u + 1);
                        } else {
                            label = u + readShort(b, u + 1);
                        }
                        newOffset = getNewOffset(allIndexes, allSizes, u, label);
                        if (newOffset < Short.MIN_VALUE
                                || newOffset > Short.MAX_VALUE) {
                            if (!resize[u]) {
                                if (opcode == org.objectweb.asm.Opcodes.GOTO || opcode == org.objectweb.asm.Opcodes.JSR) {
                                    // two additional bytes will be required to
                                    // replace this GOTO or JSR instruction with
                                    // a GOTO_W or a JSR_W
                                    insert = 2;
                                } else {
                                    // five additional bytes will be required to
                                    // replace this IFxxx <l> instruction with
                                    // IFNOTxxx <l'> GOTO_W <l>, where IFNOTxxx
                                    // is the "opposite" opcode of IFxxx (i.e.,
                                    // IFNE for IFEQ) and where <l'> designates
                                    // the instruction just after the GOTO_W.
                                    insert = 5;
                                }
                                resize[u] = true;
                            }
                        }
                        u += 3;
                        break;
                    case org.objectweb.asm.ClassWriter.LABELW_INSN:
                        u += 5;
                        break;
                    case org.objectweb.asm.ClassWriter.TABL_INSN:
                        if (state == 1) {
                            // true number of bytes to be added (or removed)
                            // from this instruction = (future number of padding
                            // bytes - current number of padding byte) -
                            // previously over estimated variation =
                            // = ((3 - newOffset%4) - (3 - u%4)) - u%4
                            // = (-newOffset%4 + u%4) - u%4
                            // = -(newOffset & 3)
                            newOffset = getNewOffset(allIndexes, allSizes, 0, u);
                            insert = -(newOffset & 3);
                        } else if (!resize[u]) {
                            // over estimation of the number of bytes to be
                            // added to this instruction = 3 - current number
                            // of padding bytes = 3 - (3 - u%4) = u%4 = u & 3
                            insert = u & 3;
                            resize[u] = true;
                        }
                        // skips instruction
                        u = u + 4 - (u & 3);
                        u += 4 * (readInt(b, u + 8) - readInt(b, u + 4) + 1) + 12;
                        break;
                    case org.objectweb.asm.ClassWriter.LOOK_INSN:
                        if (state == 1) {
                            // like TABL_INSN
                            newOffset = getNewOffset(allIndexes, allSizes, 0, u);
                            insert = -(newOffset & 3);
                        } else if (!resize[u]) {
                            // like TABL_INSN
                            insert = u & 3;
                            resize[u] = true;
                        }
                        // skips instruction
                        u = u + 4 - (u & 3);
                        u += 8 * readInt(b, u + 4) + 8;
                        break;
                    case org.objectweb.asm.ClassWriter.WIDE_INSN:
                        opcode = b[u + 1] & 0xFF;
                        if (opcode == org.objectweb.asm.Opcodes.IINC) {
                            u += 6;
                        } else {
                            u += 4;
                        }
                        break;
                    case org.objectweb.asm.ClassWriter.VAR_INSN:
                    case org.objectweb.asm.ClassWriter.SBYTE_INSN:
                    case org.objectweb.asm.ClassWriter.LDC_INSN:
                        u += 2;
                        break;
                    case org.objectweb.asm.ClassWriter.SHORT_INSN:
                    case org.objectweb.asm.ClassWriter.LDCW_INSN:
                    case org.objectweb.asm.ClassWriter.FIELDORMETH_INSN:
                    case org.objectweb.asm.ClassWriter.TYPE_INSN:
                    case org.objectweb.asm.ClassWriter.IINC_INSN:
                        u += 3;
                        break;
                    case org.objectweb.asm.ClassWriter.ITFMETH_INSN:
                    case org.objectweb.asm.ClassWriter.INDYMETH_INSN:
                        u += 5;
                        break;
                    // case ClassWriter.MANA_INSN:
                    default:
                        u += 4;
                        break;
                }
                if (insert != 0) {
                    // adds a new (u, insert) entry in the allIndexes and
                    // allSizes arrays
                    int[] newIndexes = new int[allIndexes.length + 1];
                    int[] newSizes = new int[allSizes.length + 1];
                    System.arraycopy(allIndexes, 0, newIndexes, 0,
                            allIndexes.length);
                    System.arraycopy(allSizes, 0, newSizes, 0, allSizes.length);
                    newIndexes[allIndexes.length] = u;
                    newSizes[allSizes.length] = insert;
                    allIndexes = newIndexes;
                    allSizes = newSizes;
                    if (insert > 0) {
                        state = 3;
                    }
                }
            }
            if (state < 3) {
                --state;
            }
        } while (state != 0);

        // 2nd step:
        // copies the bytecode of the method into a new bytevector, updates the
        // offsets, and inserts (or removes) bytes as requested.

        org.objectweb.asm.ByteVector newCode = new org.objectweb.asm.ByteVector(code.length);

        u = 0;
        while (u < code.length) {
            int opcode = b[u] & 0xFF;
            switch (org.objectweb.asm.ClassWriter.TYPE[opcode]) {
                case org.objectweb.asm.ClassWriter.NOARG_INSN:
                case org.objectweb.asm.ClassWriter.IMPLVAR_INSN:
                    newCode.putByte(opcode);
                    u += 1;
                    break;
                case org.objectweb.asm.ClassWriter.LABEL_INSN:
                    if (opcode > 201) {
                        // changes temporary opcodes 202 to 217 (inclusive), 218
                        // and 219 to IFEQ ... JSR (inclusive), IFNULL and
                        // IFNONNULL
                        opcode = opcode < 218 ? opcode - 49 : opcode - 20;
                        label = u + readUnsignedShort(b, u + 1);
                    } else {
                        label = u + readShort(b, u + 1);
                    }
                    newOffset = getNewOffset(allIndexes, allSizes, u, label);
                    if (resize[u]) {
                        // replaces GOTO with GOTO_W, JSR with JSR_W and IFxxx
                        // <l> with IFNOTxxx <l'> GOTO_W <l>, where IFNOTxxx is
                        // the "opposite" opcode of IFxxx (i.e., IFNE for IFEQ)
                        // and where <l'> designates the instruction just after
                        // the GOTO_W.
                        if (opcode == org.objectweb.asm.Opcodes.GOTO) {
                            newCode.putByte(200); // GOTO_W
                        } else if (opcode == org.objectweb.asm.Opcodes.JSR) {
                            newCode.putByte(201); // JSR_W
                        } else {
                            newCode.putByte(opcode <= 166 ? ((opcode + 1) ^ 1) - 1
                                    : opcode ^ 1);
                            newCode.putShort(8); // jump offset
                            newCode.putByte(200); // GOTO_W
                            // newOffset now computed from start of GOTO_W
                            newOffset -= 3;
                        }
                        newCode.putInt(newOffset);
                    } else {
                        newCode.putByte(opcode);
                        newCode.putShort(newOffset);
                    }
                    u += 3;
                    break;
                case org.objectweb.asm.ClassWriter.LABELW_INSN:
                    label = u + readInt(b, u + 1);
                    newOffset = getNewOffset(allIndexes, allSizes, u, label);
                    newCode.putByte(opcode);
                    newCode.putInt(newOffset);
                    u += 5;
                    break;
                case org.objectweb.asm.ClassWriter.TABL_INSN:
                    // skips 0 to 3 padding bytes
                    v = u;
                    u = u + 4 - (v & 3);
                    // reads and copies instruction
                    newCode.putByte(org.objectweb.asm.Opcodes.TABLESWITCH);
                    newCode.putByteArray(null, 0, (4 - newCode.length % 4) % 4);
                    label = v + readInt(b, u);
                    u += 4;
                    newOffset = getNewOffset(allIndexes, allSizes, v, label);
                    newCode.putInt(newOffset);
                    j = readInt(b, u);
                    u += 4;
                    newCode.putInt(j);
                    j = readInt(b, u) - j + 1;
                    u += 4;
                    newCode.putInt(readInt(b, u - 4));
                    for (; j > 0; --j) {
                        label = v + readInt(b, u);
                        u += 4;
                        newOffset = getNewOffset(allIndexes, allSizes, v, label);
                        newCode.putInt(newOffset);
                    }
                    break;
                case org.objectweb.asm.ClassWriter.LOOK_INSN:
                    // skips 0 to 3 padding bytes
                    v = u;
                    u = u + 4 - (v & 3);
                    // reads and copies instruction
                    newCode.putByte(org.objectweb.asm.Opcodes.LOOKUPSWITCH);
                    newCode.putByteArray(null, 0, (4 - newCode.length % 4) % 4);
                    label = v + readInt(b, u);
                    u += 4;
                    newOffset = getNewOffset(allIndexes, allSizes, v, label);
                    newCode.putInt(newOffset);
                    j = readInt(b, u);
                    u += 4;
                    newCode.putInt(j);
                    for (; j > 0; --j) {
                        newCode.putInt(readInt(b, u));
                        u += 4;
                        label = v + readInt(b, u);
                        u += 4;
                        newOffset = getNewOffset(allIndexes, allSizes, v, label);
                        newCode.putInt(newOffset);
                    }
                    break;
                case org.objectweb.asm.ClassWriter.WIDE_INSN:
                    opcode = b[u + 1] & 0xFF;
                    if (opcode == Opcodes.IINC) {
                        newCode.putByteArray(b, u, 6);
                        u += 6;
                    } else {
                        newCode.putByteArray(b, u, 4);
                        u += 4;
                    }
                    break;
                case org.objectweb.asm.ClassWriter.VAR_INSN:
                case org.objectweb.asm.ClassWriter.SBYTE_INSN:
                case org.objectweb.asm.ClassWriter.LDC_INSN:
                    newCode.putByteArray(b, u, 2);
                    u += 2;
                    break;
                case org.objectweb.asm.ClassWriter.SHORT_INSN:
                case org.objectweb.asm.ClassWriter.LDCW_INSN:
                case org.objectweb.asm.ClassWriter.FIELDORMETH_INSN:
                case org.objectweb.asm.ClassWriter.TYPE_INSN:
                case org.objectweb.asm.ClassWriter.IINC_INSN:
                    newCode.putByteArray(b, u, 3);
                    u += 3;
                    break;
                case org.objectweb.asm.ClassWriter.ITFMETH_INSN:
                case org.objectweb.asm.ClassWriter.INDYMETH_INSN:
                    newCode.putByteArray(b, u, 5);
                    u += 5;
                    break;
                // case MANA_INSN:
                default:
                    newCode.putByteArray(b, u, 4);
                    u += 4;
                    break;
            }
        }

        // updates the stack map frame labels
        if (compute == FRAMES) {
            org.objectweb.asm.Label l = labels;
            while (l != null) {
                /*
                 * Detects the labels that are just after an IF instruction that
                 * has been resized with the IFNOT GOTO_W pattern. These labels
                 * are now the target of a jump instruction (the IFNOT
                 * instruction). Note that we need the original label position
                 * here. getNewOffset must therefore never have been called for
                 * this label.
                 */
                u = l.position - 3;
                if (u >= 0 && resize[u]) {
                    l.status |= org.objectweb.asm.Label.TARGET;
                }
                getNewOffset(allIndexes, allSizes, l);
                l = l.successor;
            }
            // Update the offsets in the uninitialized types
            for (i = 0; i < cw.typeTable.length; ++i) {
                org.objectweb.asm.Item item = cw.typeTable[i];
                if (item != null && item.type == ClassWriter.TYPE_UNINIT) {
                    item.intVal = getNewOffset(allIndexes, allSizes, 0,
                            item.intVal);
                }
            }
            // The stack map frames are not serialized yet, so we don't need
            // to update them. They will be serialized in visitMaxs.
        } else if (frameCount > 0) {
            /*
             * Resizing an existing stack map frame table is really hard. Not
             * only the table must be parsed to update the offets, but new
             * frames may be needed for jump instructions that were inserted by
             * this method. And updating the offsets or inserting frames can
             * change the format of the following frames, in case of packed
             * frames. In practice the whole table must be recomputed. For this
             * the frames are marked as potentially invalid. This will cause the
             * whole class to be reread and rewritten with the COMPUTE_FRAMES
             * option (see the ClassWriter.toByteArray method). This is not very
             * efficient but is much easier and requires much less code than any
             * other method I can think of.
             */
            cw.invalidFrames = true;
        }
        // updates the exception handler block labels
        org.objectweb.asm.Handler h = firstHandler;
        while (h != null) {
            getNewOffset(allIndexes, allSizes, h.start);
            getNewOffset(allIndexes, allSizes, h.end);
            getNewOffset(allIndexes, allSizes, h.handler);
            h = h.next;
        }
        // updates the instructions addresses in the
        // local var and line number tables
        for (i = 0; i < 2; ++i) {
            ByteVector bv = i == 0 ? localVar : localVarType;
            if (bv != null) {
                b = bv.data;
                u = 0;
                while (u < bv.length) {
                    label = readUnsignedShort(b, u);
                    newOffset = getNewOffset(allIndexes, allSizes, 0, label);
                    writeShort(b, u, newOffset);
                    label += readUnsignedShort(b, u + 2);
                    newOffset = getNewOffset(allIndexes, allSizes, 0, label)
                            - newOffset;
                    writeShort(b, u + 2, newOffset);
                    u += 10;
                }
            }
        }
        if (lineNumber != null) {
            b = lineNumber.data;
            u = 0;
            while (u < lineNumber.length) {
                writeShort(
                        b,
                        u,
                        getNewOffset(allIndexes, allSizes, 0,
                                readUnsignedShort(b, u)));
                u += 4;
            }
        }
        // updates the labels of the other attributes
        Attribute attr = cattrs;
        while (attr != null) {
            org.objectweb.asm.Label[] labels = attr.getLabels();
            if (labels != null) {
                for (i = labels.length - 1; i >= 0; --i) {
                    getNewOffset(allIndexes, allSizes, labels[i]);
                }
            }
            attr = attr.next;
        }

        // replaces old bytecodes with new ones
        code = newCode;
    }

    /**
     * Reads an unsigned short value in the given byte array.
     *
     * @param b
     *            a byte array.
     * @param index
     *            the start index of the value to be read.
     * @return the read value.
     */
    static int readUnsignedShort(final byte[] b, final int index) {
        return ((b[index] & 0xFF) << 8) | (b[index + 1] & 0xFF);
    }

    /**
     * Reads a signed short value in the given byte array.
     *
     * @param b
     *            a byte array.
     * @param index
     *            the start index of the value to be read.
     * @return the read value.
     */
    static short readShort(final byte[] b, final int index) {
        return (short) (((b[index] & 0xFF) << 8) | (b[index + 1] & 0xFF));
    }

    /**
     * Reads a signed int value in the given byte array.
     *
     * @param b
     *            a byte array.
     * @param index
     *            the start index of the value to be read.
     * @return the read value.
     */
    static int readInt(final byte[] b, final int index) {
        return ((b[index] & 0xFF) << 24) | ((b[index + 1] & 0xFF) << 16)
                | ((b[index + 2] & 0xFF) << 8) | (b[index + 3] & 0xFF);
    }

    /**
     * Writes a short value in the given byte array.
     *
     * @param b
     *            a byte array.
     * @param index
     *            where the first byte of the short value must be written.
     * @param s
     *            the value to be written in the given byte array.
     */
    static void writeShort(final byte[] b, final int index, final int s) {
        b[index] = (byte) (s >>> 8);
        b[index + 1] = (byte) s;
    }

    /**
     * Computes the future value of a bytecode offset.
     * <p>
     * Note: it is possible to have several entries for the same instruction in
     * the <tt>indexes</tt> and <tt>sizes</tt>: two entries (index=a,size=b) and
     * (index=a,size=b') are equivalent to a single entry (index=a,size=b+b').
     *
     * @param indexes
     *            current positions of the instructions to be resized. Each
     *            instruction must be designated by the index of its <i>last</i>
     *            byte, plus one (or, in other words, by the index of the
     *            <i>first</i> byte of the <i>next</i> instruction).
     * @param sizes
     *            the number of bytes to be <i>added</i> to the above
     *            instructions. More precisely, for each i < <tt>len</tt>,
     *            <tt>sizes</tt>[i] bytes will be added at the end of the
     *            instruction designated by <tt>indexes</tt>[i] or, if
     *            <tt>sizes</tt>[i] is negative, the <i>last</i> |
     *            <tt>sizes[i]</tt>| bytes of the instruction will be removed
     *            (the instruction size <i>must not</i> become negative or
     *            null).
     * @param begin
     *            index of the first byte of the source instruction.
     * @param end
     *            index of the first byte of the target instruction.
     * @return the future value of the given bytecode offset.
     */
    static int getNewOffset(final int[] indexes, final int[] sizes,
                            final int begin, final int end) {
        int offset = end - begin;
        for (int i = 0; i < indexes.length; ++i) {
            if (begin < indexes[i] && indexes[i] <= end) {
                // forward jump
                offset += sizes[i];
            } else if (end < indexes[i] && indexes[i] <= begin) {
                // backward jump
                offset -= sizes[i];
            }
        }
        return offset;
    }

    /**
     * Updates the offset of the given label.
     *
     * @param indexes
     *            current positions of the instructions to be resized. Each
     *            instruction must be designated by the index of its <i>last</i>
     *            byte, plus one (or, in other words, by the index of the
     *            <i>first</i> byte of the <i>next</i> instruction).
     * @param sizes
     *            the number of bytes to be <i>added</i> to the above
     *            instructions. More precisely, for each i < <tt>len</tt>,
     *            <tt>sizes</tt>[i] bytes will be added at the end of the
     *            instruction designated by <tt>indexes</tt>[i] or, if
     *            <tt>sizes</tt>[i] is negative, the <i>last</i> |
     *            <tt>sizes[i]</tt>| bytes of the instruction will be removed
     *            (the instruction size <i>must not</i> become negative or
     *            null).
     * @param label
     *            the label whose offset must be updated.
     */
    static void getNewOffset(final int[] indexes, final int[] sizes,
                             final org.objectweb.asm.Label label) {
        if ((label.status & org.objectweb.asm.Label.RESIZED) == 0) {
            label.position = getNewOffset(indexes, sizes, 0, label.position);
            label.status |= Label.RESIZED;
        }
    }
}
