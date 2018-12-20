package kt.osrs.analysis.model

import kt.osrs.analysis.classIdentity

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
        }
    }
}