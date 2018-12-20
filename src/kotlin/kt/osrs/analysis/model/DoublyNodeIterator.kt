package kt.osrs.analysis.model

import kt.osrs.analysis.classIdentity

/**
 * Created by IntelliJ IDEA.
 * User: Mihael Bercic
 * Date: 18. 12. 2018
 * Time: 13:18
 */
class DoublyNodeIterator : Identifiable() {

    override val executeIndex = 57
    override val identity = classIdentity {
        name = "DoublyNodeIterator"
        staticDefinition {
            superName = "java/lang/Object"
            "L{CacheableNode};" occurs 2
            "L{DoublyNode};" occurs 1
            count = 3
        }
    }
}