package kt.osrs.analysis.tree.node

import kt.osrs.analysis.tree.NodeTree
import org.objectweb.asm.tree.AbstractInsnNode
import org.objectweb.asm.tree.MethodInsnNode

/**
 * @author Tyler Sedlar
 */
class MethodMemberNode(tree: NodeTree, insn: AbstractInsnNode, collapsed: Int, producing: Int) :
    kt.osrs.analysis.tree.node.ReferenceNode(tree, insn, collapsed, producing) {

    fun min(): MethodInsnNode {
        return insn() as MethodInsnNode
    }
}
