package kt.osrs.analysis.tree

import kt.osrs.analysis.tree.node.AbstractNode
import org.objectweb.asm.tree.AbstractInsnNode
import org.objectweb.asm.tree.AbstractInsnNode.LABEL
import org.objectweb.asm.tree.MethodNode
import java.util.*

/**
 * @author Tyler Sedlar
 */
class NodeTree constructor(private val mn: MethodNode) : AbstractNode(null, null, -1, -1) {

    override fun method(): MethodNode {
        return mn
    }

    override fun accept(nv: NodeVisitor) {
        if (!nv.validate()) {
            return
        }
        nv.visitCode()
        for (node in this) {
            accept(nv, node)
        }
        nv.visitEnd()
    }

    private fun accept(nv: NodeVisitor, n: AbstractNode) {
        if (!nv.validate()) {
            return
        }
        n.accept(nv)
        for (node in n) {
            accept(nv, node)
        }
    }

    override fun collapse(): Array<AbstractInsnNode> {
        val instructions = super.collapse()
        val i = if (instructions.size > 1 && instructions[instructions.size - 2].type() == LABEL) 2 else 1
        return Arrays.copyOf(instructions, instructions.size - i)
    }
}
