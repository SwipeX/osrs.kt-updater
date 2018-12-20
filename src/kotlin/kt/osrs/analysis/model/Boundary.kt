package kt.osrs.analysis.model

import kt.osrs.analysis.classIdentity

class Boundary : Identifiable() {

    override val executeIndex = 35
    override val identity = classIdentity {
        name = "Boundary"
        staticDefinition {
            "I" occurs 6
            "J" occurs 1
            "L{RenderableNode};" occurs 2
        }
    }
}