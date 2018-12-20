package kt.osrs.analysis.model

import kt.osrs.analysis.classIdentity
import kt.osrs.analysis.rank.usage.Equality
import kt.osrs.analysis.rank.usage.GETFIELD
import kt.osrs.analysis.rank.usage.PUTFIELD

class Actor : Identifiable() {
    override val executeIndex = 7
    override val identity = classIdentity {
        name = "Actor"
        staticDefinition {
            superName = "{RenderableNode}"
            "Ljava/lang/String;" occurs 1
        }
        memberIdentity {
            name = "animation"
            desc = "I"
            usageDefinition {
                "{Npc}" from "(IIZ)V" using GETFIELD x 2 and PUTFIELD x 1
            }
        }
        memberIdentity {
            name = "interactingIndex"
            desc = "I"
            usageDefinition {
                "Any" from "(L{PacketBuffer};)V" using PUTFIELD x 2 and GETFIELD x 1
            }
        }

        memberIdentity {
            name = "animationDelay"
            desc = "I"
            usageDefinition {
                "Any" from "(L{Actor};I)V" using GETFIELD x 5 and PUTFIELD x 2
                // "Any" from "(L{Actor};)V" using GETFIELD x 1
                // ^-- breaks it because idk
            }
        }
    }
}