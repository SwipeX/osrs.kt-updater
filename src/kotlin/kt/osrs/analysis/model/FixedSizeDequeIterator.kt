package kt.osrs.analysis.model

import kt.osrs.analysis.classIdentity

/**
 * Created by IntelliJ IDEA.
 * User: Mihael Bercic
 * Date: 18. 12. 2018
 * Time: 12:51
 */
class FixedSizeDequeIterator : Identifiable() {

    override val executeIndex = 48
    override val identity = classIdentity {
        name = "FixedSizeDequeIterator"
        staticDefinition {
            superName = "java/lang/Object"
            "L{Node};" occurs 2
            "L{FixedSizeDeque};" occurs 1
            "I" occurs 1
        }
    }
}