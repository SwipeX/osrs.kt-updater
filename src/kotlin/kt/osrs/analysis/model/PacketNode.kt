package kt.osrs.analysis.model

import kt.osrs.analysis.classIdentity

/**
 * Created by IntelliJ IDEA.
 * User: Mihael Bercic
 * Date: 18. 12. 2018
 * Time: 13:28
 */
class PacketNode : Identifiable() {

    override val executeIndex = 63
    override val identity = classIdentity {
        name = "PacketNode"
        staticDefinition {
            superName = "{Node}"
            "L{PacketBuffer};" occurs 1
            "I" occurs 2
            "Lfq;" occurs 1
        }
    }
}