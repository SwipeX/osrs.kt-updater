package kt.osrs.analysis.model

import kt.osrs.analysis.classIdentity

class Canvas : Identifiable() {
    override val executeIndex: Int = 2
    override val identity = classIdentity {
        name = "Canvas"
        staticDefinition {
            superName = "java/awt/Canvas"
            memberIdentity {
                name = "component"
                desc = "Ljava/awt/Component;"
            }
        }
    }
}