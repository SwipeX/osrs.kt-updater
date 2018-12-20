package kt.osrs.analysis.model

import kt.osrs.analysis.classIdentity

/**
 * Created by IntelliJ IDEA.
 * User: Mihael Bercic
 * Date: 18. 12. 2018
 * Time: 12:59
 */
class BasicByteBuffer : Identifiable() {

    override val executeIndex = 51
    override val identity = classIdentity {
        name = "BasicByteBuffer"
        staticDefinition {
            superName = "{AbstractByteBuffer}"
            "Ljava/nio/ByteBuffer;" occurs 1
            count = 1
        }
    }
}