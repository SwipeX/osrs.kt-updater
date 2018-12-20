package kt.osrs.analysis.model

import kt.osrs.analysis.classIdentity

class Model : Identifiable() {
    override val executeIndex: Int = 15
    override val identity = classIdentity {
        name = "Model"

        staticDefinition {
            superName = "{RenderableNode}"
            "[S" occurs 1
            "[[I" occurs 2
        }
    }
}