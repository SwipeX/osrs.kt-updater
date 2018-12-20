package kt.osrs.analysis.tree.node

import kt.osrs.analysis.tree.NodeTree
import org.objectweb.asm.tree.AbstractInsnNode
import org.objectweb.asm.tree.LabelNode
import java.util.*

class TargetNode(tree: NodeTree, insn: AbstractInsnNode, collapsed: Int, producing: Int) :
    AbstractNode(tree, insn, collapsed, producing) {

    private val nodes = LinkedList<kt.osrs.analysis.tree.node.JumpNode>()

    fun addTargeter(jn: kt.osrs.analysis.tree.node.JumpNode) {
        nodes.add(jn)
    }

    fun label(): LabelNode {
        return insn() as LabelNode
    }

    fun removeTargeter(jn: kt.osrs.analysis.tree.node.JumpNode) {
        nodes.remove(jn)
    }

    fun resolve(): AbstractNode? {
        var n: AbstractNode? = this
        while (n != null && n.opcode() == -1) {
            n = n.next()
        }
        return n ?: parent()
    }

    fun targeters(): Array<kt.osrs.analysis.tree.node.JumpNode> {
        return nodes.toTypedArray()
    }

    public override fun toString(tab: Int): String {
        return "Target@" + Integer.toHexString(label().hashCode())
    }
}