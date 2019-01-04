package kt.osrs.analysis.model

import kt.osrs.analysis.classIdentity
import org.objectweb.asm.Opcodes.*

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
            treePattern { opcodes(IF_ICMPLE, IALOAD, GETFIELD, INVOKESTATIC, GETFIELD) }

        }
        memberIdentity {
            name = "interactingIndex"
            desc = "I"

        }

        memberIdentity {
            name = "animationDelay"
            desc = "I"

        }
    }
}