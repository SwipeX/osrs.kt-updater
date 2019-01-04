package kt.osrs.analysis.model

import kt.osrs.analysis.classIdentity
import org.objectweb.asm.Opcodes.*

class Player : Identifiable() {
    override val executeIndex: Int = 11
    override val identity = classIdentity {
        name = "Player"

        staticDefinition {
            superName = "{Actor}"
            "[Ljava/lang/String;" occurs 1
            "L{PlayerDefinition};" occurs 1
        }

        memberIdentity {
            name = "definition"
            desc = "L{PlayerDefinition};"
            treePattern { opcodes(GETFIELD) }
        }

        memberIdentity {
            name = "actions"
            desc = "[Ljava/lang/String;"
            treePattern { opcodes(ASTORE, INVOKEVIRTUAL, INVOKEVIRTUAL, INVOKEVIRTUAL, INVOKEVIRTUAL, AALOAD, GETFIELD) }
        }

        memberIdentity {
            name = "nameComposite"
            desc = "L{NameComposite};"
            treePattern { opcodes(PUTFIELD) }
        }
    }
}