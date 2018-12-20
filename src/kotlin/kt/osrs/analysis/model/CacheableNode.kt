package kt.osrs.analysis.model

import kt.osrs.analysis.classIdentity

class CacheableNode : Identifiable() {
    override val executeIndex: Int = 4
    override val identity = classIdentity {
        name = "CacheableNode"
        staticDefinition {
            superName = "{Node}"
            "L{self};" occurs 2
            "J" occurs 1
        }
    }
}

