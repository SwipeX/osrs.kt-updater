package kt.osrs.analysis.model

import kt.osrs.analysis.classIdentity

/**
 * Created by IntelliJ IDEA.
 * User: Mihael Bercic
 * Date: 18. 12. 2018
 * Time: 12:56
 */
class AbstractByteBuffer : Identifiable() {

    override val executeIndex = 50
    override val identity = classIdentity {
        name = "AbstractByteBuffer"
        staticDefinition {
            superName = "java/lang/Object"
            hasMethod("([B)V")
            hasMethod("()[B")
        }
    }
}