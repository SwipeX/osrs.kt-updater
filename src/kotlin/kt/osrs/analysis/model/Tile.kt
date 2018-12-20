package kt.osrs.analysis.model

import kt.osrs.analysis.classIdentity

class Tile : Identifiable() {
    override val executeIndex: Int = 18
    override val identity = classIdentity {
        name = "Tile"
        staticDefinition {
            superName = "{Node}"
            "Z" occurs 3
        }
    }
}