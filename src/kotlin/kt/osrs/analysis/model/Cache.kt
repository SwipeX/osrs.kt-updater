package kt.osrs.analysis.model

import kt.osrs.analysis.classIdentity

class Cache : Identifiable() {

    override val executeIndex = 38
    override val identity = classIdentity {
        name = "Cache"
        staticDefinition {
            "L{CacheableNode};" occurs 1
            "L{HashTable};" occurs 1
        }
    }
}