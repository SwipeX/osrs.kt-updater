package kt.osrs.analysis.tree.node

import kt.osrs.analysis.tree.NodeTree
import org.objectweb.asm.tree.AbstractInsnNode
import org.objectweb.asm.tree.TypeInsnNode

/**
 * @author Tyler Sedlar
 */
class TypeNode(tree: NodeTree, insn: AbstractInsnNode, collapsed: Int, producing: Int) :
    AbstractNode(tree, insn, collapsed, producing) {

    fun type(): String {
        return (insn() as TypeInsnNode).desc
    }
}
