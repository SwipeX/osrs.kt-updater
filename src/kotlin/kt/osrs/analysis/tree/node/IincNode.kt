package kt.osrs.analysis.tree.node

import kt.osrs.analysis.tree.NodeTree
import org.objectweb.asm.tree.AbstractInsnNode
import org.objectweb.asm.tree.IincInsnNode

/**
 * @author Tyler Sedlar
 */
class IincNode(tree: NodeTree, insn: AbstractInsnNode, collapsed: Int, producing: Int) :
    AbstractNode(tree, insn, collapsed, producing) {

    fun increment(): Int {
        return (insn() as IincInsnNode).incr
    }

    fun `var`(): Int {
        return (insn() as IincInsnNode).`var`
    }
}
