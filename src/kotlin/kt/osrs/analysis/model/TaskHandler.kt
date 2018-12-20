package kt.osrs.analysis.model

import kt.osrs.analysis.classIdentity

/**
 * Created by IntelliJ IDEA.
 * User: Mihael Bercic
 * Date: 18. 12. 2018
 * Time: 13:55
 */
class TaskHandler : Identifiable() {

    override val executeIndex = 88
    override val identity = classIdentity {
        name = "TaskHandler"
        staticDefinition {
            superName = "java/lang/Object"
            "Z" occurs 1
            "L{Task};" occurs 2
            "Ljava/lang/Thread;" occurs 1
        }
    }
}