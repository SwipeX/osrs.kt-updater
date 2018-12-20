package kt.osrs.analysis.tree.node

import kt.osrs.analysis.tree.NodeTree
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.AbstractInsnNode

class ConversionNode(tree: NodeTree, insn: AbstractInsnNode, collapsed: Int, producing: Int) :
    kt.osrs.analysis.tree.node.AbstractNode(tree, insn, collapsed, producing) {

    fun fromInt(): Boolean {
        return opcode() == Opcodes.I2B || opcode() == Opcodes.I2C || opcode() == Opcodes.I2S || opcode() == Opcodes.I2L || opcode() == Opcodes.I2D
    }

    fun toInt(): Boolean {
        return opcode() == Opcodes.D2I || opcode() == Opcodes.L2I || opcode() == Opcodes.F2I
    }

    fun toChar(): Boolean {
        return opcode() == Opcodes.I2C
    }

    fun toShort(): Boolean {
        return opcode() == Opcodes.I2S
    }

    fun fromDouble(): Boolean {
        return opcode() == Opcodes.D2I || opcode() == Opcodes.D2F || opcode() == Opcodes.D2L
    }

    fun toDouble(): Boolean {
        return opcode() == Opcodes.I2D || opcode() == Opcodes.L2D || opcode() == Opcodes.F2D
    }

    fun fromLong(): Boolean {
        return opcode() == Opcodes.L2I || opcode() == Opcodes.L2F || opcode() == Opcodes.L2D
    }

    fun toLong(): Boolean {
        return opcode() == Opcodes.I2L || opcode() == Opcodes.D2L || opcode() == Opcodes.F2L
    }

    fun fromFloat(): Boolean {
        return opcode() == Opcodes.F2I || opcode() == Opcodes.F2D || opcode() == Opcodes.F2L
    }

    fun toFloat(): Boolean {
        return opcode() == Opcodes.I2F || opcode() == Opcodes.D2F || opcode() == Opcodes.L2F
    }
}
