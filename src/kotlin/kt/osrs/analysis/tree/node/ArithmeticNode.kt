package kt.osrs.analysis.tree.node

import kt.osrs.analysis.tree.NodeTree
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.AbstractInsnNode

/**
 * @author Tyler Sedlar
 */
class ArithmeticNode(tree: NodeTree, insn: AbstractInsnNode, collapsed: Int, producing: Int) :
    kt.osrs.analysis.tree.node.AbstractNode(tree, insn, collapsed, producing) {

    val isInt: Boolean
        get() = opcode() == Opcodes.IADD || opcode() == Opcodes.ISUB || opcode() == Opcodes.IMUL || opcode() == Opcodes.IDIV

    val isDouble: Boolean
        get() = opcode() == Opcodes.DADD || opcode() == Opcodes.DSUB || opcode() == Opcodes.DMUL || opcode() == Opcodes.DDIV

    val isLong: Boolean
        get() = opcode() == Opcodes.LADD || opcode() == Opcodes.LSUB || opcode() == Opcodes.LMUL || opcode() == Opcodes.LDIV

    val isFloat: Boolean
        get() = opcode() == Opcodes.FADD || opcode() == Opcodes.FSUB || opcode() == Opcodes.FMUL || opcode() == Opcodes.FDIV

    fun adding(): Boolean {
        return opcode() == Opcodes.IADD || opcode() == Opcodes.DADD || opcode() == Opcodes.LADD || opcode() == Opcodes.FADD
    }

    fun subtracting(): Boolean {
        return opcode() == Opcodes.ISUB || opcode() == Opcodes.DSUB || opcode() == Opcodes.LSUB || opcode() == Opcodes.FSUB
    }

    fun multiplying(): Boolean {
        return opcode() == Opcodes.IMUL || opcode() == Opcodes.DMUL || opcode() == Opcodes.LMUL || opcode() == Opcodes.FMUL
    }

    fun dividing(): Boolean {
        return opcode() == Opcodes.IDIV || opcode() == Opcodes.DDIV || opcode() == Opcodes.LDIV || opcode() == Opcodes.FDIV
    }

    fun negating(): Boolean {
        return opcode() == Opcodes.INEG || opcode() == Opcodes.DNEG || opcode() == Opcodes.LNEG || opcode() == Opcodes.FNEG
    }

    fun remainding(): Boolean {
        return opcode() == Opcodes.IREM || opcode() == Opcodes.DREM || opcode() == Opcodes.LREM || opcode() == Opcodes.FREM
    }

    fun shifting(): Boolean {
        return rightShifting() || leftShifiting()
    }

    fun rightShifting(): Boolean {
        return opcode() == Opcodes.ISHR || opcode() == Opcodes.LSHR || opcode() == Opcodes.IUSHR || opcode() == Opcodes.LUSHR
    }

    fun leftShifiting(): Boolean {
        return opcode() == Opcodes.ISHL || opcode() == Opcodes.LSHL
    }

    fun including(): Boolean {
        return opcode() == Opcodes.IAND || opcode() == Opcodes.LAND
    }

    fun comparing(): Boolean {
        return opcode() == Opcodes.IXOR || opcode() == Opcodes.LXOR || opcode() == Opcodes.IOR || opcode() == Opcodes.LOR
    }

    fun bitwise(): Boolean {
        return negating() || remainding() || shifting() || including() || comparing()
    }
}
