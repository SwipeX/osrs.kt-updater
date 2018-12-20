package kt.osrs.analysis.model

import kt.osrs.analysis.classIdentity

/**
 * Created by IntelliJ IDEA.
 * User: Mihael Bercic
 * Date: 18. 12. 2018
 * Time: 13:54
 */
class Task : Identifiable() {

    override val executeIndex = 87
    override val identity = classIdentity {
        name = "Task"
        staticDefinition {
            superName = "java/lang/Object"
            "I" occurs 3
            "Let;" occurs 1
            "Ljava/lang/Object;" occurs 2
        }
    }
}