package kt.osrs.analysis.model

import kt.osrs.analysis.classIdentity

class ObjectDefinition : Identifiable() {
    override val executeIndex: Int = 14
    override val identity = classIdentity {
        name = "ObjectDefinition"

        staticDefinition {  superName = "{CacheableNode}"
            "Ljava/lang/String;" occurs 1
            "[Ljava/lang/String;" occurs 1
            "Z" occurs 7
        }
    }
}