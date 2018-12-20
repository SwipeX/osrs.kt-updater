package kt.osrs.analysis.model

import kt.osrs.analysis.classIdentity

/**
 * Created by IntelliJ IDEA.
 * User: Mihael Bercic
 * Date: 18. 12. 2018
 * Time: 13:16
 */
class DoublyNode : Identifiable() {

    override val executeIndex = 56
    override val identity = classIdentity {
        name = "DoublyNode"
        staticDefinition {
            superName = "java/lang/Object"
            "L{CacheableNode};" occurs 2
            count = 2
        }
    }
}