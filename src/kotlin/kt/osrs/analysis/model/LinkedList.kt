package kt.osrs.analysis.model

import kt.osrs.analysis.classIdentity

/**
 * Created by IntelliJ IDEA.
 * User: Mihael Bercic
 * Date: 18. 12. 2018
 * Time: 12:24
 */
class LinkedList : Identifiable() {

    override val executeIndex = 44
    override val identity = classIdentity {
        name = "LinkedList"
        staticDefinition {
            superName = "java/lang/Object"
            "L{Link};" occurs 2
            count = 2
        }
    }
}