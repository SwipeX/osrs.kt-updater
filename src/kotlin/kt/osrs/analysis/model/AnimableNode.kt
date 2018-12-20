package kt.osrs.analysis.model

import kt.osrs.analysis.classIdentity

/**
 * Created by IntelliJ IDEA.
 * User: Mihael Bercic
 * Date: 18. 12. 2018
 * Time: 13:46
 */
class AnimableNode : Identifiable() {

    override val executeIndex = 77
    override val identity = classIdentity {
        name = "AnimableNode"
        staticDefinition {
            superName = "{RenderableNode}"
            "L{AnimationSequence};" occurs 1
            "I" occurs 8
        }
    }
}