package kt.osrs.analysis.tree.node

import kt.osrs.analysis.tree.NodeTree
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.AbstractInsnNode
import org.objectweb.asm.tree.FieldInsnNode

/**
 * @author Tyler Sedlar
 */
class FieldMemberNode(tree: NodeTree, insn: AbstractInsnNode, collapsed: Int, producing: Int) :
        kt.osrs.analysis.tree.node.ReferenceNode(tree, insn, collapsed, producing) {

    fun fin(): FieldInsnNode {
        return insn() as FieldInsnNode
    }

    fun getting(): Boolean {
        return opcode() == Opcodes.GETFIELD || opcode() == Opcodes.GETSTATIC
    }

    fun putting(): Boolean {
        return opcode() == Opcodes.PUTFIELD || opcode() == Opcodes.PUTSTATIC
    }

}
