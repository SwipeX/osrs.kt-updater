package kt.osrs.analysis.model

import kt.osrs.analysis.classIdentity

/**
 * Created by IntelliJ IDEA.
 * User: Mihael Bercic
 * Date: 18. 12. 2018
 * Time: 13:39
 */
class AnimatedModel : Identifiable() {

    override val executeIndex = 70
    override val identity = classIdentity {
        name = "AnimatedModel"
        staticDefinition {
            superName = "{RenderableNode}"
            "Z" occurs 1
            "[I" occurs 8
            "[Ldf;" occurs 2
            "I" occurs 8
            "[S" occurs 5
            "[B" occurs 5
            "[Lei;" occurs 1
            "[[I" occurs 2
            "B" occurs 1
            "S" occurs 2
        }
    }
}