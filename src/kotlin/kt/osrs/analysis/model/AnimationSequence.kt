package kt.osrs.analysis.model

import kt.osrs.analysis.classIdentity

class AnimationSequence : Identifiable() {

    override val executeIndex = 29
    override val identity = classIdentity {
        name = "AnimationSequence"
        staticDefinition {
            superName = "{CacheableNode}"
            count = 14
            "Z" occurs 1
        }
    }
}