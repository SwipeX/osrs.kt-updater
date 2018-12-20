package kt.osrs.analysis.model

import kt.osrs.analysis.classIdentity

class PlayerDefinition : Identifiable() {
    override val executeIndex: Int = 9
    override val identity = classIdentity {
        name = "PlayerDefinition"

        staticDefinition {
            superName = "java/lang/Object"
            "[I" occurs 2
            "J" occurs 2
        }
    }
}