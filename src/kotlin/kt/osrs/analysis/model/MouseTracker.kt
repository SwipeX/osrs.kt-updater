package kt.osrs.analysis.model

import kt.osrs.analysis.classIdentity

/**
 * Created by IntelliJ IDEA.
 * User: Mihael Bercic
 * Date: 18. 12. 2018
 * Time: 13:56
 */
class MouseTracker : Identifiable() {

    override val executeIndex = 90
    override val identity = classIdentity {
        name = "MouseTracker"
        staticDefinition {
            superName = "java/lang/Object"
            "Z" occurs 1
            "Ljava/lang/Object;" occurs 1
            "I" occurs 1
            "[I" occurs 2
            "[J" occurs 1
        }
    }
}