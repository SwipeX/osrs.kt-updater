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
package org.objectweb.asm.tree;

import org.objectweb.asm.*;
import org.objectweb.asm.commons.cfg.query.InsnQuery;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A node that represents a method.
 *
 * @author Eric Bruneton
 */
public class MethodNode extends MethodVisitor {

    public final org.objectweb.asm.tree.ClassNode owner;

    /**
     * The method's access flags (see {@link Opcodes}). This field also
     * indicates if the method is synthetic and/or deprecated.
     */
    public int access;

    /**
     * The method's name.
     */
    public String name;

    /**
     * The method's descriptor (see {@link Type}).
     */
    public String desc;

    /**
     * The method's signature. May be <tt>null</tt>.
     */
    public String signature;

    /**
     * The internal names of the method's exception classes (see
     * {@link Type#getInternalName() getInternalName}). This list is a list of
     * {@link String} objects.
     */
    public List<String> exceptions;

    /**
     * The method parameter info (access flags and name)
     */
    public List<ParameterNode> parameters;

    /**
     * The runtime visible annotations of this method. This list is a list of
     * {@link org.objectweb.asm.tree.AnnotationNode} objects. May be <tt>null</tt>.
     *
     * @associates objectweb.org.objectweb.org.objectweb.AnnotationNode
     * @label visible
     */
    public List<AnnotationNode> visibleAnnotations;

    /**
     * The runtime invisible annotations of this method. This list is a list of
     * {@link AnnotationNode} objects. May be <tt>null</tt>.
     *
     * @associates objectweb.org.objectweb.org.objectweb.AnnotationNode
     * @label invisible
     */
    public List<AnnotationNode> invisibleAnnotations;

    /**
     * The runtime visible type annotations of this method. This list is a list
     * of {@link org.objectweb.asm.tree.TypeAnnotationNode} objects. May be <tt>null</tt>.
     *
     * @associates objectweb.org.objectweb.org.objectweb.TypeAnnotationNode
     * @label visible
     */
    public List<TypeAnnotationNode> visibleTypeAnnotations;

    /**
     * The runtime invisible type annotations of this method. This list is a
     * list of {@link TypeAnnotationNode} objects. May be <tt>null</tt>.
     *
     * @associates objectweb.org.objectweb.org.objectweb.TypeAnnotationNode
     * @label invisible
     */
    public List<TypeAnnotationNode> invisibleTypeAnnotations;

    /**
     * The non standard attributes of this method. This list is a list of
     * {@link Attribute} objects. May be <tt>null</tt>.
     *
     * @associates objectweb.org.objectweb.org.objectweb.Attribute
     */
    public List<Attribute> attrs;

    /**
     * The default value of this annotation interface method. This field must be
     * a {@link Byte}, {@link Boolean}, {@link Character}, {@link Short},
     * {@link Integer}, {@link Long}, {@link Float}, {@link Double},
     * {@link String} or {@link Type}, or an two elements String array (for
     * enumeration values), a {@link AnnotationNode}, or a {@link List} of
     * values of one of the preceding types. May be <tt>null</tt>.
     */
    public Object annotationDefault;

    /**
     * The runtime visible parameter annotations of this method. These lists are
     * lists of {@link AnnotationNode} objects. May be <tt>null</tt>.
     *
     * @associates objectweb.org.objectweb.org.objectweb.AnnotationNode
     * @label invisible parameters
     */
    public List<AnnotationNode>[] visibleParameterAnnotations;

    /**
     * The runtime invisible parameter annotations of this method. These lists
     * are lists of {@link AnnotationNode} objects. May be <tt>null</tt>.
     *
     * @associates objectweb.org.objectweb.org.objectweb.AnnotationNode
     * @label visible parameters
     */
    public List<AnnotationNode>[] invisibleParameterAnnotations;

    /**
     * The instructions of this method. This list is a list of
     * {@link org.objectweb.asm.tree.AbstractInsnNode} objects.
     *
     * @associates objectweb.org.objectweb.org.objectweb.AbstractInsnNode
     * @label instructions
     */
    public org.objectweb.asm.tree.InsnList instructions;

    /**
     * The try catch blocks of this method. This list is a list of
     * {@link org.objectweb.asm.tree.TryCatchBlockNode} objects.
     *
     * @associates objectweb.org.objectweb.org.objectweb.TryCatchBlockNode
     */
    public List<TryCatchBlockNode> tryCatchBlocks;

    /**
     * The maximum stack size of this method.
     */
    public int maxStack;

    /**
     * The maximum number of local variables of this method.
     */
    public int maxLocals;

    /**
     * The local variables of this method. This list is a list of
     * {@link org.objectweb.asm.tree.LocalVariableNode} objects. May be <tt>null</tt>
     *
     * @associates objectweb.org.objectweb.org.objectweb.LocalVariableNode
     */
    public List<LocalVariableNode> localVariables;

    /**
     * The visible local variable annotations of this method. This list is a
     * list of {@link LocalVariableAnnotationNode} objects. May be <tt>null</tt>
     *
     * @associates objectweb.org.objectweb.org.objectweb.LocalVariableAnnotationNode
     */
    public List<LocalVariableAnnotationNode> visibleLocalVariableAnnotations;

    /**
     * The invisible local variable annotations of this method. This list is a
     * list of {@link LocalVariableAnnotationNode} objects. May be <tt>null</tt>
     *
     * @associates objectweb.org.objectweb.org.objectweb.LocalVariableAnnotationNode
     */
    public List<LocalVariableAnnotationNode> invisibleLocalVariableAnnotations;

    /**
     * If the accept method has been called on this object.
     */
    private boolean visited;

    /**
     * The handle for this method.
     */
    public final Handle handle;

    /**
     * Constructs an uninitialized {@link MethodNode}.
     */
    public MethodNode(org.objectweb.asm.tree.ClassNode owner) {
        this.owner = owner;
        this.instructions = new org.objectweb.asm.tree.InsnList();
        this.handle = new Handle(0, owner.name, name, desc);
    }

    /**
     * Constructs a new {@link MethodNode}.
     *
     * @param access     the method's access flags (see {@link Opcodes}). This
     *                   parameter also indicates if the method is synthetic and/or
     *                   deprecated.
     * @param name       the method's name.
     * @param desc       the method's descriptor (see {@link Type}).
     * @param signature  the method's signature. May be <tt>null</tt>.
     * @param exceptions the internal names of the method's exception classes (see
     *                   {@link Type#getInternalName() getInternalName}). May be
     *                   <tt>null</tt>.
     */
    public MethodNode(org.objectweb.asm.tree.ClassNode owner, final int access, final String name, final String desc, final String signature,
                      final String[] exceptions) {
        this.owner = owner;
        this.access = access;
        this.name = name;
        this.desc = desc;
        this.signature = signature;
        this.exceptions = new ArrayList<String>(exceptions == null ? 0 : exceptions.length);
        boolean isAbstract = (access & Opcodes.ACC_ABSTRACT) != 0;
        if (!isAbstract) {
            this.localVariables = new ArrayList<>(5);
        }
        this.tryCatchBlocks = new ArrayList<>();
        if (exceptions != null) {
            this.exceptions.addAll(Arrays.asList(exceptions));
        }
        this.instructions = new InsnList();
        this.handle = new Handle(0, owner.name, name, desc);
    }

    // ------------------------------------------------------------------------
    // Implementation of the MethodVisitor abstract class
    // ------------------------------------------------------------------------

    @Override
    public void visitParameter(ParameterNode pn) {
        if (parameters == null) {
            parameters = new ArrayList<>(5);
        }
        parameters.add(pn);
    }

    @Override
    public AnnotationVisitor visitAnnotationDefault() {
        return new AnnotationNode(new ArrayList<Object>(0) {
            @Override
            public boolean add(final Object o) {
                annotationDefault = o;
                return super.add(o);
            }
        });
    }

    @Override
    public AnnotationVisitor visitAnnotation(AnnotationNode an, final boolean visible) {
        if (visible) {
            if (visibleAnnotations == null) {
                visibleAnnotations = new ArrayList<>(1);
            }
            visibleAnnotations.add(an);
        } else {
            if (invisibleAnnotations == null) {
                invisibleAnnotations = new ArrayList<>(1);
            }
            invisibleAnnotations.add(an);
        }
        return an;
    }

    @Override
    public AnnotationVisitor visitTypeAnnotation(TypeAnnotationNode tan, boolean visible) {
        if (visible) {
            if (visibleTypeAnnotations == null) {
                visibleTypeAnnotations = new ArrayList<>(1);
            }
            visibleTypeAnnotations.add(tan);
        } else {
            if (invisibleTypeAnnotations == null) {
                invisibleTypeAnnotations = new ArrayList<>(1);
            }
            invisibleTypeAnnotations.add(tan);
        }
        return tan;
    }

    @Override
    @SuppressWarnings("unchecked")
    public AnnotationVisitor visitParameterAnnotation(ParameterAnnotationNode pan, final boolean visible) {
        AnnotationNode an = new AnnotationNode(desc);
        if (visible) {
            if (visibleParameterAnnotations == null) {
                int params = Type.getArgumentTypes(this.desc).length;
                visibleParameterAnnotations = (List<AnnotationNode>[]) new List<?>[params];
            }
            if (visibleParameterAnnotations[pan.parameter] == null) {
                visibleParameterAnnotations[pan.parameter] = new ArrayList<>(1);
            }
            visibleParameterAnnotations[pan.parameter].add(an);
        } else {
            if (invisibleParameterAnnotations == null) {
                int params = Type.getArgumentTypes(this.desc).length;
                invisibleParameterAnnotations = (List<AnnotationNode>[]) new List<?>[params];
            }
            if (invisibleParameterAnnotations[pan.parameter] == null) {
                invisibleParameterAnnotations[pan.parameter] = new ArrayList<>(1);
            }
            invisibleParameterAnnotations[pan.parameter].add(an);
        }
        return an;
    }

    @Override
    public void visitAttribute(final Attribute attr) {
        if (attrs == null) {
            attrs = new ArrayList<>(1);
        }
        attrs.add(attr);
    }

    @Override
    public void visitCode() {
    }

    @Override
    public void visitFrame(org.objectweb.asm.tree.FrameNode frame) {
        frame.method = this;
        int nLocal = frame.local != null ? frame.local.size() : 0;
        int nStack = frame.stack != null ? frame.stack.size() : 0;
        instructions.add(new FrameNode(frame.type, nLocal,
                frame.local != null ? getLabelNodes(frame.local.toArray()) : null, nStack,
                frame.stack != null ? getLabelNodes(frame.stack.toArray()) : null));
    }

    @Override
    public void visitInsn(InsnNode in) {
        in.method = this;
        instructions.add(in);
    }

    @Override
    public void visitIntInsn(IntInsnNode iin) {
        iin.method = this;
        instructions.add(iin);
    }

    @Override
    public void visitVarInsn(VarInsnNode vin) {
        vin.method = this;
        instructions.add(vin);
    }

    @Override
    public void visitTypeInsn(TypeInsnNode tin) {
        tin.method = this;
        instructions.add(tin);
    }

    @Override
    public void visitFieldInsn(FieldInsnNode fin) {
        fin.method = this;
        instructions.add(fin);
        owner.references.add(fin.owner + "." + fin.name + fin.desc);
    }

    @Override
    public void visitMethodInsn(MethodInsnNode min) {
        min.method = this;
        instructions.add(min);
        owner.references.add(min.owner + "." + min.name + min.desc);
    }

    @Override
    public void visitInvokeDynamicInsn(InvokeDynamicInsnNode idin) {
        idin.method = this;
        instructions.add(idin);
    }

    @Override
    public void visitJumpInsn(JumpInsnNode jin) {
        jin.method = this;
        instructions.add(new JumpInsnNode(jin.opcode(), getLabelNode(jin.label.getLabel())));
    }

    @Override
    public void visitLabel(final Label label) {
        instructions.add(getLabelNode(label));
    }

    @Override
    public void visitLdcInsn(LdcInsnNode ldc) {
        ldc.method = this;
        instructions.add(ldc);
    }

    @Override
    public void visitIincInsn(IincInsnNode iin) {
        iin.method = this;
        instructions.add(iin);
    }

    @Override
    public void visitTableSwitchInsn(TableSwitchInsnNode tsin) {
        tsin.method = this;
        Label[] labels = new Label[tsin.labels.size()];
        for (int i = 0; i < labels.length; i++) {
            labels[i] = tsin.labels.get(i).getLabel();
        }
        instructions.add(new TableSwitchInsnNode(tsin.min, tsin.max, getLabelNode(tsin.dflt.getLabel()),
                getLabelNodes(labels)));
    }

    @Override
    public void visitLookupSwitchInsn(LookupSwitchInsnNode lsin) {
        lsin.method = this;
        int[] keys = new int[lsin.keys.size()];
        for (int i = 0; i < keys.length; i++) {
            keys[i] = lsin.keys.get(i);
        }
        Label[] labels = new Label[lsin.labels.size()];
        for (int i = 0; i < labels.length; i++) {
            labels[i] = lsin.labels.get(i).getLabel();
        }
        instructions.add(new LookupSwitchInsnNode(getLabelNode(lsin.dflt.getLabel()), keys,
                getLabelNodes(labels)));
    }

    @Override
    public void visitMultiANewArrayInsn(MultiANewArrayInsnNode manain) {
        manain.method = this;
        instructions.add(manain);
    }

    @Override
    public AnnotationVisitor visitInsnAnnotation(TypeAnnotationNode tan, boolean visible) {
        // Finds the last real instruction, i.e. the instruction targeted by
        // this annotation.
        AbstractInsnNode insn = instructions.getLast();
        while (insn.opcode() == -1) {
            insn = insn.previous();
        }
        // Adds the annotation to this instruction.
        TypeAnnotationNode an = new TypeAnnotationNode(tan.typeRef, tan.typePath, desc);
        if (visible) {
            if (insn.visibleTypeAnnotations == null) {
                insn.visibleTypeAnnotations = new ArrayList<>(1);
            }
            insn.visibleTypeAnnotations.add(an);
        } else {
            if (insn.invisibleTypeAnnotations == null) {
                insn.invisibleTypeAnnotations = new ArrayList<>(1);
            }
            insn.invisibleTypeAnnotations.add(an);
        }
        return an;
    }

    @Override
    public void visitTryCatchBlock(TryCatchBlockNode tcbn) {
        tryCatchBlocks.add(new TryCatchBlockNode(getLabelNode(tcbn.start.getLabel()),
                getLabelNode(tcbn.end.getLabel()), getLabelNode(tcbn.handler.getLabel()), tcbn.type));
    }

    @Override
    public AnnotationVisitor visitTryCatchAnnotation(TypeAnnotationNode tan, boolean visible) {
        TryCatchBlockNode tcb = tryCatchBlocks.get((tan.typeRef & 0x00FFFF00) >> 8);
        if (visible) {
            if (tcb.visibleTypeAnnotations == null) {
                tcb.visibleTypeAnnotations = new ArrayList<>(1);
            }
            tcb.visibleTypeAnnotations.add(tan);
        } else {
            if (tcb.invisibleTypeAnnotations == null) {
                tcb.invisibleTypeAnnotations = new ArrayList<>(1);
            }
            tcb.invisibleTypeAnnotations.add(tan);
        }
        return tan;
    }

    @Override
    public void visitLocalVariable(LocalVariableNode lvn) {
        localVariables.add(new LocalVariableNode(name, desc, signature, getLabelNode(lvn.start.getLabel()),
                getLabelNode(lvn.end.getLabel()), lvn.index));
    }

    @Override
    public AnnotationVisitor visitLocalVariableAnnotation(LocalVariableAnnotationNode lvan, boolean visible) {
        Label[] startNodes = new Label[lvan.start.size()];
        for (int i = 0; i < startNodes.length; i++) {
            startNodes[i] = lvan.start.get(i).getLabel();
        }
        Label[] endNodes = new Label[lvan.end.size()];
        for (int i = 0; i < endNodes.length; i++) {
            endNodes[i] = lvan.end.get(i).getLabel();
        }
        int[] index = new int[lvan.index.size()];
        for (int i = 0; i < index.length; i++) {
            index[i] = lvan.index.get(i);
        }
        LocalVariableAnnotationNode an = new LocalVariableAnnotationNode(lvan.typeRef, lvan.typePath,
                getLabelNodes(startNodes), getLabelNodes(endNodes), index, desc);
        if (visible) {
            if (visibleLocalVariableAnnotations == null) {
                visibleLocalVariableAnnotations = new ArrayList<>(1);
            }
            visibleLocalVariableAnnotations.add(an);
        } else {
            if (invisibleLocalVariableAnnotations == null) {
                invisibleLocalVariableAnnotations = new ArrayList<>(1);
            }
            invisibleLocalVariableAnnotations.add(an);
        }
        return an;
    }

    @Override
    public void visitLineNumber(LineNumberNode lnn) {
        lnn.method = this;
        instructions.add(new LineNumberNode(lnn.line, getLabelNode(lnn.start.getLabel())));
    }

    @Override
    public void visitMaxs(final int maxStack, final int maxLocals) {
        this.maxStack = maxStack;
        this.maxLocals = maxLocals;
    }

    @Override
    public void visitEnd() {
    }

    /**
     * Returns the LabelNode corresponding to the given Label. Creates a new
     * LabelNode if necessary. The default implementation of this method uses
     * the {@link Label#info} field to store associations between labels and
     * label nodes.
     *
     * @param l a Label.
     * @return the LabelNode corresponding to l.
     */
    protected org.objectweb.asm.tree.LabelNode getLabelNode(final Label l) {
        if (!(l.info instanceof org.objectweb.asm.tree.LabelNode)) {
            l.info = new org.objectweb.asm.tree.LabelNode();
        }
        org.objectweb.asm.tree.LabelNode ln = (org.objectweb.asm.tree.LabelNode) l.info;
        ln.method = this;
        return ln;
    }

    private org.objectweb.asm.tree.LabelNode[] getLabelNodes(final Label[] l) {
        org.objectweb.asm.tree.LabelNode[] nodes = new org.objectweb.asm.tree.LabelNode[l.length];
        for (int i = 0; i < l.length; ++i) {
            nodes[i] = getLabelNode(l[i]);
            nodes[i].method = this;
        }
        return nodes;
    }

    private Object[] getLabelNodes(final Object[] objs) {
        Object[] nodes = new Object[objs.length];
        for (int i = 0; i < objs.length; ++i) {
            Object o = objs[i];
            if (o instanceof Label) {
                o = getLabelNode((Label) o);
                ((LabelNode) o).method = this;
            }
            nodes[i] = o;
        }
        return nodes;
    }

    // ------------------------------------------------------------------------
    // Accept method
    // ------------------------------------------------------------------------

    /**
     * Makes the given class visitor visit this method.
     *
     * @param cv a class visitor.
     */
    public void accept(final ClassVisitor cv) {
        String[] exceptions = new String[this.exceptions.size()];
        this.exceptions.toArray(exceptions);
        MethodVisitor mv = cv.visitMethod(access, name, desc, signature,
                exceptions);
        if (mv != null) {
            accept(mv);
        }
    }

    /**
     * Makes the given method visitor visit this method.
     *
     * @param mv a method visitor.
     */
    public void accept(final MethodVisitor mv) {
        // visits the method parameters
        int i, j, n;
        n = parameters == null ? 0 : parameters.size();
        for (i = 0; i < n; i++) {
            ParameterNode parameter = parameters.get(i);
            mv.visitParameter(parameter);
        }
        // visits the method attributes
        if (annotationDefault != null) {
            AnnotationVisitor av = mv.visitAnnotationDefault();
            AnnotationNode.accept(av, null, annotationDefault);
            if (av != null) {
                av.visitEnd();
            }
        }
        n = visibleAnnotations == null ? 0 : visibleAnnotations.size();
        for (i = 0; i < n; ++i) {
            AnnotationNode an = visibleAnnotations.get(i);
            an.accept(mv.visitAnnotation(an, true));
        }
        n = invisibleAnnotations == null ? 0 : invisibleAnnotations.size();
        for (i = 0; i < n; ++i) {
            AnnotationNode an = invisibleAnnotations.get(i);
            an.accept(mv.visitAnnotation(an, false));
        }
        n = visibleTypeAnnotations == null ? 0 : visibleTypeAnnotations.size();
        for (i = 0; i < n; ++i) {
            TypeAnnotationNode an = visibleTypeAnnotations.get(i);
            an.accept(mv.visitTypeAnnotation(an, true));
        }
        n = invisibleTypeAnnotations == null ? 0 : invisibleTypeAnnotations.size();
        for (i = 0; i < n; ++i) {
            TypeAnnotationNode an = invisibleTypeAnnotations.get(i);
            an.accept(mv.visitTypeAnnotation(an, false));
        }
        n = visibleParameterAnnotations == null ? 0 : visibleParameterAnnotations.length;
        for (i = 0; i < n; ++i) {
            List<?> l = visibleParameterAnnotations[i];
            if (l == null) {
                continue;
            }
            for (j = 0; j < l.size(); ++j) {
                AnnotationNode an = (AnnotationNode) l.get(j);
                an.accept(mv.visitParameterAnnotation(new ParameterAnnotationNode(i, an.desc), true));
            }
        }
        n = invisibleParameterAnnotations == null ? 0 : invisibleParameterAnnotations.length;
        for (i = 0; i < n; ++i) {
            List<?> l = invisibleParameterAnnotations[i];
            if (l == null) {
                continue;
            }
            for (j = 0; j < l.size(); ++j) {
                AnnotationNode an = (AnnotationNode) l.get(j);
                an.accept(mv.visitParameterAnnotation(new ParameterAnnotationNode(i, an.desc), false));
            }
        }
        if (visited) {
            instructions.resetLabels();
        }
        n = attrs == null ? 0 : attrs.size();
        for (i = 0; i < n; ++i) {
            mv.visitAttribute(attrs.get(i));
        }
        // visits the method's code
        if (instructions.size() > 0) {
            mv.visitCode();
            // visits try catch blocks
            n = tryCatchBlocks == null ? 0 : tryCatchBlocks.size();
            for (i = 0; i < n; ++i) {
                tryCatchBlocks.get(i).updateIndex(i);
                tryCatchBlocks.get(i).accept(mv);
            }
            // visits instructions
            instructions.accept(mv);
            // visits local variables
            n = localVariables == null ? 0 : localVariables.size();
            for (i = 0; i < n; ++i) {
                localVariables.get(i).accept(mv);
            }
            // visits local variable annotations
            n = visibleLocalVariableAnnotations == null ? 0
                    : visibleLocalVariableAnnotations.size();
            for (i = 0; i < n; ++i) {
                visibleLocalVariableAnnotations.get(i).accept(mv, true);
            }
            n = invisibleLocalVariableAnnotations == null ? 0
                    : invisibleLocalVariableAnnotations.size();
            for (i = 0; i < n; ++i) {
                invisibleLocalVariableAnnotations.get(i).accept(mv, false);
            }
            // visits maxs
            mv.visitMaxs(maxStack, maxLocals);
            visited = true;
        }
        mv.visitEnd();
    }

    /**
     * Gets the amount of times the given opcodes has been matched
     *
     * @param opcode The opcode to match
     * @return The amount of times the given opcode has been matched.
     */
    public int count(int opcode) {
        int count = 0;
        for (AbstractInsnNode ain : instructions.toArray()) {
            if (ain.opcode() == opcode) {
                count++;
            }
        }
        return count;
    }

    /**
     * Gets the amount of times the given query has been matched
     *
     * @param entry The query to match
     * @return The amount of times the given query has been matched.
     */
    public int count(InsnQuery entry) {
        int count = 0;
        for (AbstractInsnNode ain : instructions.toArray()) {
            if (entry.matches(ain)) {
                count++;
            }
        }
        return count;
    }

    public int parameters() {
        return Type.getArgumentTypes(desc).length;
    }

    public boolean referenced(ClassNode cn) {
        return cn.references.contains(owner.name + "." + name + desc);
    }
}
