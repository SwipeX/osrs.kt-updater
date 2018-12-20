package kt.osrs.analysis.tree.node

import kt.osrs.analysis.tree.NodeTree
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.AbstractInsnNode
import org.objectweb.asm.tree.IntInsnNode
import org.objectweb.asm.tree.LdcInsnNode

class NumberNode(tree: NodeTree, insn: AbstractInsnNode, collapsed: Int, producing: Int) :
    AbstractNode(tree, insn, collapsed, producing) {

    fun number(): Int {
        val insn = insn()
        val op = insn!!.opcode()
        when (op) {
            Opcodes.NEWARRAY, Opcodes.BIPUSH, Opcodes.SIPUSH -> {
                return (insn as IntInsnNode).operand
            }
            Opcodes.ICONST_M1, Opcodes.ICONST_0, Opcodes.ICONST_1, Opcodes.ICONST_2, Opcodes.ICONST_3, Opcodes.ICONST_4, Opcodes.ICONST_5 -> {
                return op - Opcodes.ICONST_0
            }
            Opcodes.LCONST_0, Opcodes.LCONST_1 -> {
                return op - Opcodes.LCONST_0
            }
            Opcodes.FCONST_0, Opcodes.FCONST_1, Opcodes.FCONST_2 -> {
                return op - Opcodes.FCONST_0
            }
            Opcodes.DCONST_0, Opcodes.DCONST_1 -> {
                return op - Opcodes.DCONST_0
            }
            Opcodes.LDC -> {
                run {
                    val cst = (insn as LdcInsnNode).cst
                    if (cst is Number) {
                        return cst.toInt()
                    }
                }
                run { return -1 }
            }
            else -> {
                return -1
            }
        }
    }

    fun setNumber(number: Int) {
        val ain = insn()
        if (ain is IntInsnNode) {
            (insn() as IntInsnNode).operand = number
            ain.operand = number
        } else if (ain is LdcInsnNode) {
            (insn() as LdcInsnNode).cst = number
            ain.cst = number
        }
    }
}