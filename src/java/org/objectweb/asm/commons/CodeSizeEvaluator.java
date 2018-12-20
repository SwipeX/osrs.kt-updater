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
package org.objectweb.asm.commons;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

/**
 * A {@link MethodVisitor} that can be used to approximate method size.
 *
 * @author Eugene Kuleshov
 */
public class CodeSizeEvaluator extends MethodVisitor implements Opcodes {

    private int minSize;

    private int maxSize;

    public CodeSizeEvaluator() {
        this(null);
    }

    public CodeSizeEvaluator(MethodVisitor mv) {
        super(mv);
    }

    public int getMinSize() {
        return minSize;
    }

    public int getMaxSize() {
        return maxSize;
    }

    @Override
    public void visitInsn(InsnNode in) {
        minSize += 1;
        maxSize += 1;
        if (mv != null) {
            mv.visitInsn(in);
        }
    }

    @Override
    public void visitIntInsn(IntInsnNode iin) {
        if (iin.opcode() == SIPUSH) {
            minSize += 3;
            maxSize += 3;
        } else {
            minSize += 2;
            maxSize += 2;
        }
        if (mv != null) {
            mv.visitIntInsn(iin);
        }
    }

    @Override
    public void visitVarInsn(VarInsnNode vin) {
        if (vin.var < 4 && vin.opcode() != RET) {
            minSize += 1;
            maxSize += 1;
        } else if (vin.var >= 256) {
            minSize += 4;
            maxSize += 4;
        } else {
            minSize += 2;
            maxSize += 2;
        }
        if (mv != null) {
            mv.visitVarInsn(vin);
        }
    }

    @Override
    public void visitTypeInsn(TypeInsnNode tin) {
        minSize += 3;
        maxSize += 3;
        if (mv != null) {
            mv.visitTypeInsn(tin);
        }
    }

    @Override
    public void visitFieldInsn(FieldInsnNode fin) {
        minSize += 3;
        maxSize += 3;
        if (mv != null) {
            mv.visitFieldInsn(fin);
        }
    }

    @Override
    public void visitMethodInsn(MethodInsnNode min) {
        if (min.opcode() == INVOKEINTERFACE) {
            minSize += 5;
            maxSize += 5;
        } else {
            minSize += 3;
            maxSize += 3;
        }
        if (mv != null) {
            mv.visitMethodInsn(min);
        }
    }

    @Override
    public void visitInvokeDynamicInsn(InvokeDynamicInsnNode idin) {
        minSize += 5;
        maxSize += 5;
        if (mv != null) {
            mv.visitInvokeDynamicInsn(idin);
        }
    }

    @Override
    public void visitJumpInsn(JumpInsnNode jin) {
        minSize += 3;
        if (jin.opcode() == GOTO || jin.opcode() == JSR) {
            maxSize += 5;
        } else {
            maxSize += 8;
        }
        if (mv != null) {
            mv.visitJumpInsn(jin);
        }
    }

    @Override
    public void visitLdcInsn(LdcInsnNode ldc) {
        if (ldc.cst instanceof Long || ldc.cst instanceof Double) {
            minSize += 3;
            maxSize += 3;
        } else {
            minSize += 2;
            maxSize += 3;
        }
        if (mv != null) {
            mv.visitLdcInsn(ldc);
        }
    }

    @Override
    public void visitIincInsn(IincInsnNode iin) {
        if (iin.var > 255 || iin.incr > 127 || iin.incr < -128) {
            minSize += 6;
            maxSize += 6;
        } else {
            minSize += 3;
            maxSize += 3;
        }
        if (mv != null) {
            mv.visitIincInsn(iin);
        }
    }

    @Override
    public void visitTableSwitchInsn(TableSwitchInsnNode tsin) {
        minSize += 13 + tsin.labels.size() * 4;
        maxSize += 16 + tsin.labels.size() * 4;
        if (mv != null) {
            mv.visitTableSwitchInsn(tsin);
        }
    }

    @Override
    public void visitLookupSwitchInsn(LookupSwitchInsnNode lsin) {
        minSize += 9 + lsin.keys.size() * 8;
        maxSize += 12 + lsin.keys.size() * 8;
        if (mv != null) {
            mv.visitLookupSwitchInsn(lsin);
        }
    }

    @Override
    public void visitMultiANewArrayInsn(MultiANewArrayInsnNode manain) {
        minSize += 4;
        maxSize += 4;
        if (mv != null) {
            mv.visitMultiANewArrayInsn(manain);
        }
    }
}
