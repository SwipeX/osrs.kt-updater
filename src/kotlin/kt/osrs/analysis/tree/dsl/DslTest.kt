package kt.osrs.analysis.tree.dsl

import org.objectweb.asm.Opcodes

fun test() {
    TreePattern {
        opcodes = arrayOf(Opcodes.GETFIELD, Opcodes.AALOAD)
    }
}