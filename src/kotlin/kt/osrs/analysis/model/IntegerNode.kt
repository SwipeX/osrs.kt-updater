package kt.osrs.analysis.model

import kt.osrs.analysis.classIdentity

/**
 * Created by IntelliJ IDEA.
 * User: Mihael Bercic
 * Date: 18. 12. 2018
 * Time: 13:48
 */
class IntegerNode : Identifiable() {

    override val executeIndex = 78
    override val identity = classIdentity {
        name = "IntegerNode"
        staticDefinition {
            superName = "{Node}"
            "I" occurs 1
            count = 1
        }
    }
}