package kt.osrs.analysis.model

import kt.osrs.analysis.classIdentity

class Region : Identifiable() {

    override val executeIndex = 27
    override val identity = classIdentity {
        name = "Region"
        staticDefinition {
            "I" occurs 5
            "[[[L{Tile};" occurs 1
            "[L{InteractableObject};" occurs 1
        }
    }
}