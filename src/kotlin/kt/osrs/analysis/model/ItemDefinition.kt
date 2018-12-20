package kt.osrs.analysis.model

import kt.osrs.analysis.classIdentity

class ItemDefinition : Identifiable() {

    override val executeIndex = 33
    override val identity = classIdentity {
        name = "ItemDefiniton"
        staticDefinition {
            superName = "{CacheableNode}"
            "[Ljava/lang/String;" occurs 2
        }
    }
}