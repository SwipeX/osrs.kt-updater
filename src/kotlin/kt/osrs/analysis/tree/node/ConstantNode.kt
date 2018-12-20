package kt.osrs.analysis.tree.node

import kt.osrs.analysis.tree.NodeTree
import org.objectweb.asm.tree.AbstractInsnNode
import org.objectweb.asm.tree.LdcInsnNode

/**
 * @author Tyler Sedlar
 */
class ConstantNode(tree: NodeTree, insn: AbstractInsnNode, collapsed: Int, producing: Int) :
    kt.osrs.analysis.tree.node.AbstractNode(tree, insn, collapsed, producing) {

    override fun insn(): LdcInsnNode? {
        return super.insn() as LdcInsnNode
    }

    fun cst(): Any {
        return insn()!!.cst
    }
}
