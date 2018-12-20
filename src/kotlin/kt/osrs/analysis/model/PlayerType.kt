package kt.osrs.analysis.model

import kt.osrs.analysis.classIdentity

/**
 * Created by IntelliJ IDEA.
 * User: Mihael Bercic
 * Date: 18. 12. 2018
 * Time: 13:45
 */
class PlayerType : Identifiable() {

    override val executeIndex = 76
    override val identity = classIdentity {
        name = "PlayerType"
        staticDefinition {
            superName = "java/lang/Enum"
            "I" occurs 2
            "Z" occurs 2
            count = 4
        }
    }
}