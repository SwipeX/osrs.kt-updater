package kt.osrs.analysis.model

import kt.osrs.analysis.classIdentity

/**
 * Created by IntelliJ IDEA.
 * User: Mihael Bercic
 * Date: 18. 12. 2018
 * Time: 14:01
 */
class AccessFileHandler : Identifiable() {

    override val executeIndex = 98
    override val identity = classIdentity {
        name = "AccessFileHandler"
        staticDefinition {
            superName = "java/lang/Object"
            "J" occurs 6
            "I" occurs 2
            "L{AccessFile};" occurs 1
            "[B" occurs 2
            count = 11
        }
    }
}