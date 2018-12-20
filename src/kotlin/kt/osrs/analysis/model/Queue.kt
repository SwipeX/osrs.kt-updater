package kt.osrs.analysis.model

import kt.osrs.analysis.classIdentity

class Queue : Identifiable() {

    override val executeIndex = 28
    override val identity = classIdentity {
        name = "Queue"
        staticDefinition {
            count = 1
            "L{CacheableNode};" occurs 1
        }
    }
}