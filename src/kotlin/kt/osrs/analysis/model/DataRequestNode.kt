package kt.osrs.analysis.model

import kt.osrs.analysis.classIdentity

/**
 * Created by IntelliJ IDEA.
 * User: Mihael Bercic
 * Date: 18. 12. 2018
 * Time: 13:48
 */
class DataRequestNode : Identifiable() {

    override val executeIndex = 80
    override val identity = classIdentity {
        name = "DataRequestNode"
        staticDefinition {
            "[B" occurs 1
            "I" occurs 3
            "Z" occurs 1
            count = 5
        }
    }
}