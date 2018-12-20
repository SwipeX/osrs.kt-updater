package kt.osrs.analysis.model

import kt.osrs.analysis.classIdentity

class AnimableObject : Identifiable() {

    override val executeIndex = 34
    override val identity = classIdentity {
        name = "AnimableObject"
        staticDefinition {
            superName = "{RenderableNode}"
            "L{AnimationSequence};" occurs 1
            "Z" occurs 1
            "I" occurs 8
        }
    }
}