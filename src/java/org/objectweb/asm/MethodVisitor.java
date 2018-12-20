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
import org.objectweb.asm.tree.ParameterNode;

/**
 * A visitor to visit a Java method. The methods of this class must be called in
 * the following order: ( <tt>visitParameter</tt> )* [
 * <tt>visitAnnotationDefault</tt> ] ( <tt>visitAnnotation</tt> |
 * <tt>visitTypeAnnotation</tt> | <tt>visitAttribute</tt> )* [
 * <tt>visitCode</tt> ( <tt>visitFrame</tt> | <tt>visit<i>X</i>Insn</tt> |
 * <tt>visitLabel</tt> | <tt>visitInsnAnnotation</tt> |
 * <tt>visitTryCatchBlock</tt> | <tt>visitTryCatchBlockAnnotation</tt> |
 * <tt>visitLocalVariable</tt> | <tt>visitLocalVariableAnnotation</tt> |
 * <tt>visitLineNumber</tt> )* <tt>visitMaxs</tt> ] <tt>visitEnd</tt>. In
 * addition, the <tt>visit<i>X</i>Insn</tt> and <tt>visitLabel</tt> methods must
 * be called in the sequential order of the bytecode instructions of the visited
 * code, <tt>visitInsnAnnotation</tt> must be called <i>after</i> the annotated
 * instruction, <tt>visitTryCatchBlock</tt> must be called <i>before</i> the
 * labels passed as arguments have been visited,
 * <tt>visitTryCatchBlockAnnotation</tt> must be called <i>after</i> the
 * corresponding try catch block has been visited, and the
 * <tt>visitLocalVariable</tt>, <tt>visitLocalVariableAnnotation</tt> and
 * <tt>visitLineNumber</tt> methods must be called <i>after</i> the labels
 * passed as arguments have been visited.
 *
 * @author Eric Bruneton
 */
public abstract class MethodVisitor {

    /**
     * The method visitor to which this visitor must delegate method calls. May
     * be null.
     */
    protected MethodVisitor mv;

    /**
     * Constructs a new {@link MethodVisitor}.
     */
    public MethodVisitor() {
        this(null);
    }

    /**
     * Constructs a new {@link MethodVisitor}.
     *
     * @param mv
     *            the method visitor to which this visitor must delegate method
     *            calls. May be null.
     */
    public MethodVisitor(final MethodVisitor mv) {
        this.mv = mv;
    }

    // -------------------------------------------------------------------------
    // Parameters, annotations and non standard attributes
    // -------------------------------------------------------------------------

    /**
     * Visits a parameter of this method.
     */
    public void visitParameter(ParameterNode pn) {
        if (mv != null) {
            mv.visitParameter(pn);
        }
    }

    /**
     * Visits the default value of this annotation interface method.
     *
     * @return a visitor to the visit the actual default value of this
     *         annotation interface method, or <tt>null</tt> if this visitor is
     *         not interested in visiting this default value. The 'name'
     *         parameters passed to the methods of this annotation visitor are
     *         ignored. Moreover, exacly one visit method must be called on this
     *         annotation visitor, followed by visitEnd.
     */
    public AnnotationVisitor visitAnnotationDefault() {
        if (mv != null) {
            return mv.visitAnnotationDefault();
        }
        return null;
    }

    /**
     * Visits an annotation of this method.
     *
     * @param visible
     *            <tt>true</tt> if the annotation is visible at runtime.
     * @return a visitor to visit the annotation values, or <tt>null</tt> if
     *         this visitor is not interested in visiting this annotation.
     */
    public AnnotationVisitor visitAnnotation(AnnotationNode an, boolean visible) {
        if (mv != null) {
            return mv.visitAnnotation(an, visible);
        }
        return null;
    }

    /**
     * Visits an annotation on a type in the method signature.
     *
     * @param visible
     *            <tt>true</tt> if the annotation is visible at runtime.
     * @return a visitor to visit the annotation values, or <tt>null</tt> if
     *         this visitor is not interested in visiting this annotation.
     */
    public AnnotationVisitor visitTypeAnnotation(TypeAnnotationNode tan, boolean visible) {
        if (mv != null) {
            return mv.visitTypeAnnotation(tan, visible);
        }
        return null;
    }

    /**
     * Visits an annotation of a parameter this method.
     *
     * @param visible
     *            <tt>true</tt> if the annotation is visible at runtime.
     * @return a visitor to visit the annotation values, or <tt>null</tt> if
     *         this visitor is not interested in visiting this annotation.
     */
    public AnnotationVisitor visitParameterAnnotation(ParameterAnnotationNode pan, boolean visible) {
        if (mv != null) {
            return mv.visitParameterAnnotation(pan, visible);
        }
        return null;
    }

    /**
     * Visits a non standard attribute of this method.
     *
     * @param attr
     *            an attribute.
     */
    public void visitAttribute(org.objectweb.asm.Attribute attr) {
        if (mv != null) {
            mv.visitAttribute(attr);
        }
    }

    /**
     * Starts the visit of the method's code, if any (i.e. non abstract method).
     */
    public void visitCode() {
        if (mv != null) {
            mv.visitCode();
        }
    }

    /**
     * Visits the current state of the local variables and operand stack
     * elements. This method must(*) be called <i>just before</i> any
     * instruction <b>i</b> that follows an unconditional branch instruction
     * such as GOTO or THROW, that is the target of a jump instruction, or that
     * starts an exception handler block. The visited types must describe the
     * values of the local variables and of the operand stack elements <i>just
     * before</i> <b>i</b> is executed.<br>
     * <br>
     * (*) this is mandatory only for classes whose version is greater than or
     * equal to {@link org.objectweb.asm.Opcodes#V1_6 V1_6}. <br>
     * <br>
     * The frames of a method must be given either in expanded form, or in
     * compressed form (all frames must use the same format, i.e. you must not
     * mix expanded and compressed frames within a single method):
     * <ul>
     * <li>In expanded form, all frames must have the F_NEW type.</li>
     * <li>In compressed form, frames are basically "deltas" from the state of
     * the previous frame:
     * <ul>
     * <li>{@link org.objectweb.asm.Opcodes#F_SAME} representing frame with exactly the same
     * locals as the previous frame and with the empty stack.</li>
     * <li>{@link org.objectweb.asm.Opcodes#F_SAME1} representing frame with exactly the same
     * locals as the previous frame and with single value on the stack (
     * <code>nStack</code> is 1 and <code>stack[0]</code> contains value for the
     * type of the stack item).</li>
     * <li>{@link org.objectweb.asm.Opcodes#F_APPEND} representing frame with current locals are
     * the same as the locals in the previous frame, except that additional
     * locals are defined (<code>nLocal</code> is 1, 2 or 3 and
     * <code>local</code> elements contains values representing added types).</li>
     * <li>{@link org.objectweb.asm.Opcodes#F_CHOP} representing frame with current locals are the
     * same as the locals in the previous frame, except that the last 1-3 locals
     * are absent and with the empty stack (<code>nLocals</code> is 1, 2 or 3).</li>
     * <li>{@link Opcodes#F_FULL} representing complete frame data.</li>
     * </ul>
     * </li>
     * </ul>
     * <br>
     * In both cases the first frame, corresponding to the method's parameters
     * and access flags, is implicit and must not be visited. Also, it is
     * illegal to visit two or more frames for the same code location (i.e., at
     * least one instruction must be visited between two calls to visitFrame).
     *
     * @throws IllegalStateException
     *             if a frame is visited just after another one, without any
     *             instruction between the two (unless this frame is a
     *             Opcodes#F_SAME frame, in which case it is silently ignored).
     */
    public void visitFrame(FrameNode frame) {
        if (mv != null) {
            mv.visitFrame(frame);
        }
    }

    // -------------------------------------------------------------------------
    // Normal instructions
    // -------------------------------------------------------------------------

    /**
     * Visits a zero operand instruction.
     */
    public void visitInsn(InsnNode in) {
        if (mv != null) {
            mv.visitInsn(in);
        }
    }

    /**
     * Visits an instruction with a single int operand.
     */
    public void visitIntInsn(IntInsnNode iin) {
        if (mv != null) {
            mv.visitIntInsn(iin);
        }
    }

    /**
     * Visits a local variable instruction. A local variable instruction is an
     * instruction that loads or stores the value of a local variable.
     */
    public void visitVarInsn(VarInsnNode vin) {
        if (mv != null) {
            mv.visitVarInsn(vin);
        }
    }

    /**
     * Visits a type instruction. A type instruction is an instruction that
     * takes the internal name of a class as parameter.
     */
    public void visitTypeInsn(TypeInsnNode tin) {
        if (mv != null) {
            mv.visitTypeInsn(tin);
        }
    }

    /**
     * Visits a field instruction. A field instruction is an instruction that
     * loads or stores the value of a field of an object.
     */
    public void visitFieldInsn(FieldInsnNode fin) {
        if (mv != null) {
            mv.visitFieldInsn(fin);
        }
    }

    /**
     * Visits a method instruction. A method instruction is an instruction that
     * invokes a method.
     */
    public void visitMethodInsn(MethodInsnNode min) {
        if (mv != null) {
            mv.visitMethodInsn(min);
        }
    }

    /**
     * Visits an invokedynamic instruction.
     */
    public void visitInvokeDynamicInsn(InvokeDynamicInsnNode idin) {
        if (mv != null) {
            mv.visitInvokeDynamicInsn(idin);
        }
    }

    /**
     * Visits a jump instruction. A jump instruction is an instruction that may
     * jump to another instruction.
     */
    public void visitJumpInsn(JumpInsnNode jin) {
        if (mv != null) {
            mv.visitJumpInsn(jin);
        }
    }

    /**
     * Visits a label. A label designates the instruction that will be visited
     * just after it.
     *
     * @param label
     *            a {@link org.objectweb.asm.Label Label} object.
     */
    public void visitLabel(Label label) {
        if (mv != null) {
            mv.visitLabel(label);
        }
    }

    // -------------------------------------------------------------------------
    // Special instructions
    // -------------------------------------------------------------------------

    /**
     * Visits a LDC instruction.
     */
    public void visitLdcInsn(LdcInsnNode ldc) {
        if (mv != null) {
            mv.visitLdcInsn(ldc);
        }
    }

    /**
     * Visits an IINC instruction.
     */
    public void visitIincInsn(IincInsnNode iin) {
        if (mv != null) {
            mv.visitIincInsn(iin);
        }
    }

    /**
     * Visits a TABLESWITCH instruction.
     */
    public void visitTableSwitchInsn(TableSwitchInsnNode tsin) {
        if (mv != null) {
            mv.visitTableSwitchInsn(tsin);
        }
    }

    /**
     * Visits a LOOKUPSWITCH instruction.
     */
    public void visitLookupSwitchInsn(LookupSwitchInsnNode lsin) {
        if (mv != null) {
            mv.visitLookupSwitchInsn(lsin);
        }
    }

    /**
     * Visits a MULTIANEWARRAY instruction.
     */
    public void visitMultiANewArrayInsn(MultiANewArrayInsnNode manain) {
        if (mv != null) {
            mv.visitMultiANewArrayInsn(manain);
        }
    }

    /**
     * Visits an annotation on an instruction. This method must be called just
     * <i>after</i> the annotated instruction. It can be called several times
     * for the same instruction.
     *
     * @param visible
     *            <tt>true</tt> if the annotation is visible at runtime.
     * @return a visitor to visit the annotation values, or <tt>null</tt> if
     *         this visitor is not interested in visiting this annotation.
     */
    public AnnotationVisitor visitInsnAnnotation(TypeAnnotationNode tan, boolean visible) {
        if (mv != null) {
            return mv.visitInsnAnnotation(tan, visible);
        }
        return null;
    }

    // -------------------------------------------------------------------------
    // Exceptions table entries, debug information, max stack and max locals
    // -------------------------------------------------------------------------

    /**
     * Visits a try catch block.
     *
     * @throws IllegalArgumentException
     *             if one of the labels has already been visited by this visitor
     *             (by the {@link #visitLabel visitLabel} method).
     */
    public void visitTryCatchBlock(TryCatchBlockNode tcbn) {
        if (mv != null) {
            mv.visitTryCatchBlock(tcbn);
        }
    }

    /**
     * Visits an annotation on an exception handler type. This method must be
     * called <i>after</i> the {@link #visitTryCatchBlock} for the annotated
     * exception handler. It can be called several times for the same exception
     * handler.
     *
     * @param visible
     *            <tt>true</tt> if the annotation is visible at runtime.
     * @return a visitor to visit the annotation values, or <tt>null</tt> if
     *         this visitor is not interested in visiting this annotation.
     */
    public AnnotationVisitor visitTryCatchAnnotation(TypeAnnotationNode tan, boolean visible) {
        if (mv != null) {
            return mv.visitTryCatchAnnotation(tan, visible);
        }
        return null;
    }

    /**
     * Visits a local variable declaration.
     *
     * @throws IllegalArgumentException
     *             if one of the labels has not already been visited by this
     *             visitor (by the {@link #visitLabel visitLabel} method).
     */
    public void visitLocalVariable(LocalVariableNode lvn) {
        if (mv != null) {
            mv.visitLocalVariable(lvn);
        }
    }

    /**
     * Visits an annotation on a local variable type.
     *
     * @param visible
     *            <tt>true</tt> if the annotation is visible at runtime.
     * @return a visitor to visit the annotation values, or <tt>null</tt> if
     *         this visitor is not interested in visiting this annotation.
     */
    public AnnotationVisitor visitLocalVariableAnnotation(LocalVariableAnnotationNode lvan, boolean visible) {
        if (mv != null) {
            return mv.visitLocalVariableAnnotation(lvan, visible);
        }
        return null;
    }

    /**
     * Visits a line number declaration.
     *
     * @throws IllegalArgumentException
     *             if <tt>start</tt> has not already been visited by this
     *             visitor (by the {@link #visitLabel visitLabel} method).
     */
    public void visitLineNumber(LineNumberNode lnn) {
        if (mv != null) {
            mv.visitLineNumber(lnn);
        }
    }

    /**
     * Visits the maximum stack size and the maximum number of local variables
     * of the method.
     *
     * @param maxStack
     *            maximum stack size of the method.
     * @param maxLocals
     *            maximum number of local variables for the method.
     */
    public void visitMaxs(int maxStack, int maxLocals) {
        if (mv != null) {
            mv.visitMaxs(maxStack, maxLocals);
        }
    }

    /**
     * Visits the end of the method. This method, which is the last one to be
     * called, is used to inform the visitor that all the annotations and
     * attributes of the method have been visited.
     */
    public void visitEnd() {
        if (mv != null) {
            mv.visitEnd();
        }
    }
}
