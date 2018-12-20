package kt.osrs.analysis.model

import kt.osrs.analysis.classIdentity

/**
 * Created by IntelliJ IDEA.
 * User: Mihael Bercic
 * Date: 18. 12. 2018
 * Time: 13:20
 */
class NodeListIterator : Identifiable() {

    override val executeIndex = 58
    override val identity = classIdentity {
        name = "NodeListIterator"
        staticDefinition {
            superName = "java/lang/Object"
            "L{Node};" occurs 2
            "L{NodeList};" occurs 1
        }
    }
}