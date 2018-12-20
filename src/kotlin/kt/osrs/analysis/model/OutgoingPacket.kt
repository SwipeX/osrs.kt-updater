package kt.osrs.analysis.model

import kt.osrs.analysis.classIdentity

/**
 * Created by IntelliJ IDEA.
 * User: Mihael Bercic
 * Date: 18. 12. 2018
 * Time: 13:25
 */
class OutgoingPacket : Identifiable() {

    override val executeIndex = 62
    override val identity = classIdentity {
        name = "OutgoingPacket"
        staticDefinition {
            superName = "java/lang/Object"
            "I" occurs 2
            count = 2
        }
    }
}