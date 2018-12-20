package kt.osrs.analysis.model

import kt.osrs.analysis.classIdentity

class World : Identifiable() {
    override val executeIndex: Int = 24
    override val identity = classIdentity {
        name = "World"
        staticDefinition {
            "Ljava/lang/String;" occurs 2
            "I" occurs 5
        }
    }
}