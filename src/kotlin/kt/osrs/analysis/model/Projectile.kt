package kt.osrs.analysis.model

import kt.osrs.analysis.classIdentity

class Projectile : Identifiable() {

    override val executeIndex = 30
    override val identity = classIdentity {
        name = "Projectile"
        staticDefinition {
            superName = "{RenderableNode}"
            "Z" occurs 1
            "I" occurs 15
            "L{AnimationSequence};" occurs 1
        }
    }
}