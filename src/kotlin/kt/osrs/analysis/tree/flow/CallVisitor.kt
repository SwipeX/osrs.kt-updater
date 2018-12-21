package kt.osrs.analysis.tree.flow

import kt.osrs.analysis.tree.flow.graph.CallGraph
import org.objectweb.asm.Handle
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.tree.MethodInsnNode
import org.objectweb.asm.tree.MethodNode

/**
 * @author Tyler Sedlar
 */
class CallVisitor : MethodVisitor() {

    val graph = CallGraph()

    private var mn: MethodNode? = null

    fun visit(mn: MethodNode) {
        this.mn = mn
        mn.accept(this)
    }

    override fun visitMethodInsn(min: MethodInsnNode) {
        graph.addMethodCall(mn!!.handle, Handle(0, min.owner, min.name, min.desc))
    }
}