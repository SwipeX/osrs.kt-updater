package kt.osrs.analysis.model

import kt.osrs.analysis.classIdentity

class Npc : Identifiable() {
    override val executeIndex: Int = 13
    override val identity = classIdentity {
        name = "Npc"

        staticDefinition {
            superName = "{Actor}"
            count = 1
            "L{NpcDefinition};" occurs 1
        }

        memberIdentity {
            name = "definition"
            desc = "L{NpcDefinition};"
        }
    }
}