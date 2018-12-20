package kt.osrs.analysis.model

import kt.osrs.analysis.classIdentity

/**
 * Created by IntelliJ IDEA.
 * User: Mihael Bercic
 * Date: 18. 12. 2018
 * Time: 13:57
 */
class TilePaint : Identifiable() {

    override val executeIndex = 91
    override val identity = classIdentity {
        name = "TilePaint"
        staticDefinition {
            superName = "java/lang/Object"
            "[I" occurs 10
            "Z" occurs 1
            "I" occurs 4
        }
    }
}