package kt.osrs.analysis.model

import kt.osrs.analysis.classIdentity

/**
 * Created by IntelliJ IDEA.
 * User: Mihael Bercic
 * Date: 17. 12. 2018
 * Time: 12:55
 */
class PacketBuffer : Identifiable() {

    override val executeIndex = 42
    override val identity = classIdentity {
        name = "PacketBuffer"
        staticDefinition {
            superName = "{NodeByteBuffer}"
            "I" occurs 1
            "L{ISAACCipher};" occurs 1
        }
    }
}