package kt.osrs.analysis.model

import kt.osrs.analysis.classIdentity

/**
 * Created by IntelliJ IDEA.
 * User: Mihael Bercic
 * Date: 18. 12. 2018
 * Time: 13:51
 */
class TaskDataNode : Identifiable() {

    override val executeIndex = 81
    override val identity = classIdentity {
        name = "TaskDataNode"
        staticDefinition {
            superName = "{Node}"
            "Z" occurs 1
            "Ldj;" occurs 1
            "Ldz;" occurs 1
            "I" occurs 1
            count = 4
        }
    }
}