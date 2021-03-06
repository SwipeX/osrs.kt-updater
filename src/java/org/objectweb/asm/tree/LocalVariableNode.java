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

import org.objectweb.asm.MethodVisitor;

/**
 * A node that represents a local value declaration.
 *
 * @author Eric Bruneton
 */
public class LocalVariableNode {

    /**
     * The name of a local value.
     */
    public String name;

    /**
     * The type descriptor of this local value.
     */
    public String desc;

    /**
     * The signature of this local value. May be <tt>null</tt>.
     */
    public String signature;

    /**
     * The first instruction corresponding to the scope of this local value
     * (inclusive).
     */
    public org.objectweb.asm.tree.LabelNode start;

    /**
     * The last instruction corresponding to the scope of this local value
     * (exclusive).
     */
    public org.objectweb.asm.tree.LabelNode end;

    /**
     * The local value's index.
     */
    public int index;

    /**
     * Constructs a new {@link LocalVariableNode}.
     *
     * @param name
     *            the name of a local value.
     * @param desc
     *            the type descriptor of this local value.
     * @param signature
     *            the signature of this local value. May be <tt>null</tt>.
     * @param start
     *            the first instruction corresponding to the scope of this local
     *            value (inclusive).
     * @param end
     *            the last instruction corresponding to the scope of this local
     *            value (exclusive).
     * @param index
     *            the local value's index.
     */
    public LocalVariableNode(final String name, final String desc,
                             final String signature, final org.objectweb.asm.tree.LabelNode start, final org.objectweb.asm.tree.LabelNode end,
                             final int index) {
        this.name = name;
        this.desc = desc;
        this.signature = signature;
        this.start = start;
        this.end = end;
        this.index = index;
    }

    /**
     * Makes the given visitor visit this local value declaration.
     *
     * @param mv
     *            a method visitor.
     */
    public void accept(final MethodVisitor mv) {
        mv.visitLocalVariable(this);
    }
}
