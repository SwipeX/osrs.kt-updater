package kt.osrs.analysis.tree.dsl

import org.objectweb.asm.Opcodes

class DslTest {
    fun ye() {
        NodeSequence {
            !fmn("{Player}","Ljava/lang/String;") + vn(5) {
                this[fmn("{Npc}"), vn(Opcodes.ALOAD,1)]
            } + vn() - vn {
                fmn()
            }
        }
    }
}