package org.objectweb.asm.commons.cfg;

import org.objectweb.asm.Handle;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.cfg.graph.CallGraph;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

/**
 * @author Tyler Sedlar
 */
public class CallVisitor extends MethodVisitor {

    public final CallGraph graph = new CallGraph();

    private MethodNode mn;

    public void visit(MethodNode mn) {
        this.mn = mn;
        mn.accept(this);
    }

    @Override
    public void visitMethodInsn(MethodInsnNode min) {
        graph.addMethodCall(mn.handle, new Handle(0, min.owner, min.name, min.desc));
    }
}