package kt.osrs.analysis.model

import kt.osrs.analysis.classIdentity

class CollisionMap : Identifiable() {
    override val executeIndex = 40
    override val identity = classIdentity {
        name = "CollisionMap"
        staticDefinition {
            superName = "java/lang/Object"
            "I" occurs 4
            "[[I" occurs 1
        }
    }
}