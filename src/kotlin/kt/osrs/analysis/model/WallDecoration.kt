package kt.osrs.analysis.model

import kt.osrs.analysis.classIdentity

class WallDecoration : Identifiable() {

    override val executeIndex: Int = 20

    override val identity = classIdentity {
        name = "WallDecoration"
        staticDefinition {
            "I" occurs 8
            "L{RenderableNode};" occurs 2
        }
    }
}