package kt.osrs.analysis.model

import kt.osrs.analysis.classIdentity
import kt.osrs.analysis.tree.dsl.NodeSequence
import org.objectweb.asm.Opcodes.ALOAD
import org.objectweb.asm.Opcodes.INVOKEVIRTUAL


class NpcDefinition : Identifiable() {
    override val executeIndex: Int = 12
    override val identity = classIdentity {
        name = "NpcDefinition"

        staticDefinition {
            superName = "{CacheableNode}"
            "Ljava/lang/String;" occurs 1
            "[Ljava/lang/String;" occurs 1
            "Z" occurs 5
        }

        memberIdentity {
            name = "name"
            desc = "Ljava/lang/String;"
            nodeSequence {
                !fmn {
                    vn(ALOAD, 0) and mmn(INVOKEVIRTUAL) {
                        vn(ALOAD, 1)
                    }
                }
            }
        }

        memberIdentity {
            name = "overheadIcons"
            desc = "[I"
            sequence = NodeSequence {
                jn() {
                    !fmn {
                        vn(ALOAD, 0)
                    }
                }
            }
        }
    }
}