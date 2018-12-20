package kt.osrs.analysis.rank.usage

import kt.osrs.analysis.rank.*
import org.objectweb.asm.Opcodes

class UsageDefinition(init: UsageDefinition.() -> Unit) {

    val usages = mutableListOf<Usage>()

    init {
        apply(init)
    }

    infix fun String.from(desc: String): Usage = Usage(this, desc).apply {
        usages.add(this)
    }

    infix fun Usage.using(opcode: Int) = Opcode(opcode, 1).apply {
        opcodes.add(this)
    }

    infix fun Usage.and(opcode: Int) = Opcode(opcode, 1).apply {
        opcodes.add(this)
    }

    infix fun Opcode.x(occurrence: Int) = usages.firstOrNull { it.opcodes.contains(this) }!!.apply {
        occurs = occurrence
    }


}

data class Opcode(val opcode: Int, var occurs: Int, var equality: Equality = Equality.EXACT)

enum class Equality {
    EXACT, MIN, MAX
}

fun opcName(opcode: Int) = when (opcode) {
    PUTSTATIC -> "${color}PUTSTATIC$reset"
    GETSTATIC -> "${red}GETSTATIC$reset"
    GETFIELD -> "${blue}GETFIELD$reset"
    PUTFIELD -> "${magenta}PUTFIELD$reset"
    else -> "UNKNOWN!!"
}

val PUTSTATIC = Opcodes.PUTSTATIC
val GETSTATIC = Opcodes.GETSTATIC
val GETFIELD = Opcodes.GETFIELD
val PUTFIELD = Opcodes.PUTFIELD