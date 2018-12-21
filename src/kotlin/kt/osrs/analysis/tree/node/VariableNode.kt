package kt.osrs.analysis.tree.node

import kt.osrs.analysis.tree.NodeTree
import org.objectweb.asm.tree.AbstractInsnNode
import org.objectweb.asm.tree.VarInsnNode

/**
 * @author Tyler Sedlar
 */
class VariableNode(tree: NodeTree, insn: AbstractInsnNode, collapsed: Int, producing: Int) :
    kt.osrs.analysis.tree.node.AbstractNode(tree, insn, collapsed, producing) {

    fun variable(): Int {
        return (insn() as VarInsnNode).`var`
    }
}
