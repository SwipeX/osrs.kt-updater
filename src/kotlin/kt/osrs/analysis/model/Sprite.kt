package kt.osrs.analysis.model

import kt.osrs.analysis.classIdentity

class Sprite : Identifiable() {
    override val executeIndex: Int = 25
    override val identity = classIdentity {
        name = "Sprite"
        staticDefinition {
            hasMethod("(II)V")
            hasMethod("(IIIII)V")
            "I" occurs 6
            "[I" occurs 1
        }
    }
}