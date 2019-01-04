package kt.osrs.analysis.tree.dsl

import org.objectweb.asm.Opcodes

fun test() {
    TreePattern {
        opcodes(Opcodes.ASTORE, Opcodes.INVOKEVIRTUAL, Opcodes.INVOKEVIRTUAL, Opcodes.INVOKEVIRTUAL, Opcodes.INVOKEVIRTUAL, Opcodes.AALOAD, Opcodes.GETFIELD)
    }
}