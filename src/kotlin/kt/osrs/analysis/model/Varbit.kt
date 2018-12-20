package kt.osrs.analysis.model

import kt.osrs.analysis.classIdentity

class Varbit : Identifiable() {
    override val executeIndex: Int = 19
    override val identity = classIdentity {
        name = "Varbit"
        staticDefinition {
            superName = "{CacheableNode}"
            count = 3
            "I" occurs 3
        }
    }
}