package kt.osrs.analysis.tree.node

import kt.osrs.analysis.tree.NodeTree
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.AbstractInsnNode
import org.objectweb.asm.tree.FieldInsnNode
import org.objectweb.asm.tree.MethodInsnNode
import org.objectweb.asm.tree.MethodNode

open class ReferenceNode(tree: NodeTree, insn: AbstractInsnNode, collapsed: Int, producing: Int) :
    kt.osrs.analysis.tree.node.AbstractNode(tree, insn, collapsed, producing) {

    val isStatic: Boolean
        get() = opcode() == Opcodes.GETSTATIC || opcode() == Opcodes.PUTSTATIC || opcode() == Opcodes.INVOKESTATIC

    fun key(): String? {
        val ain = insn()
        if (ain is FieldInsnNode) {
            val fin = ain
            return fin.owner + "." + fin.name
        } else if (ain is MethodInsnNode) {
            val min = ain
            return min.owner + "." + min.name + min.desc
        }
        return null
    }

    fun owner(): String? {
        val insn = insn()
        if (this is FieldMemberNode) {
            return (insn as FieldInsnNode).owner
        } else if (this is MethodMemberNode) {
            return (insn as MethodInsnNode).owner
        }
        return null
    }

    fun name(): String? {
        val ain = insn()
        if (ain is FieldInsnNode) {
            return ain.name
        } else if (ain is MethodInsnNode) {
            return ain.name
        }
        return null
    }

    fun desc(): String? {
        val ain = insn()
        if (this is FieldMemberNode) {
            return (ain as FieldInsnNode).desc
        } else if (this is MethodMemberNode) {
            return (ain as MethodInsnNode).desc
        }
        return null
    }

    fun referenced(mn: MethodNode): Boolean {
        for (ain in mn.instructions.toArray()) {
            if (ain is FieldInsnNode) {
                val fin = ain
                if (key() == fin.owner + "." + fin.name) {
                    return true
                }
            } else if (ain is MethodInsnNode) {
                val min = ain
                if (key() == min.owner + "." + min.name + min.desc) {
                    return true
                }
            }
        }
        return false
    }
}