package kt.osrs.analysis.model

import kt.osrs.analysis.classIdentity

/**
 * Created by IntelliJ IDEA.
 * User: Mihael Bercic
 * Date: 18. 12. 2018
 * Time: 13:54
 */
class TaskData : Identifiable() {

    override val executeIndex = 86
    override val identity = classIdentity {
        name = "TaskData"
        staticDefinition {
            superName = "java/lang/Object"
            "[L{TaskDataNode};" occurs 2
            "[I" occurs 1
            "L{TaskDataNode};" occurs 1
            "I" occurs 8
            "Z" occurs 1
            "J" occurs 3
        }
    }
}