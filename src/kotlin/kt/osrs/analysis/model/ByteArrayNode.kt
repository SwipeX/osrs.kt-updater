package kt.osrs.analysis.model

import kt.osrs.analysis.classIdentity

/**
 * Created by IntelliJ IDEA.
 * User: Mihael Bercic
 * Date: 18. 12. 2018
 * Time: 12:52
 */
class ByteArrayNode : Identifiable() {

    override val executeIndex = 49
    override val identity = classIdentity {
        name = "ByteArrayNode"
        staticDefinition {
            superName = "{Node}"
            "[B" occurs 1
            count = 1
        }
    }
}