package kt.osrs.analysis.model

import kt.osrs.analysis.classIdentity

class ItemLayer : Identifiable() {

    override val executeIndex = 32
    override val identity = classIdentity {
        name = "ItemLayer"
        staticDefinition {
            "L{RenderableNode};" occurs 3
        }
    }
}