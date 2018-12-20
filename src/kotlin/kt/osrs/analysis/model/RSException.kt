package kt.osrs.analysis.model

import kt.osrs.analysis.classIdentity

/**
 * Created by IntelliJ IDEA.
 * User: Mihael Bercic
 * Date: 18. 12. 2018
 * Time: 12:25
 */
class RSException : Identifiable() {

    override val executeIndex = 45

    override
    val identity = classIdentity {
        name = "RSException"
        staticDefinition {
            superName = "java/lang/RuntimeException"
            "Ljava/lang/String;" occurs 1
            "Ljava/lang/Throwable;" occurs 1
        }
    }
}