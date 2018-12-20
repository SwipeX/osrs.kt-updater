package kt.osrs.analysis.model

import kt.osrs.analysis.classIdentity

/**
 * Created by IntelliJ IDEA.
 * User: Mihael Bercic
 * Date: 18. 12. 2018
 * Time: 14:03
 */
class DevelopmentStage : Identifiable() {

    override val executeIndex = 99
    override val identity = classIdentity {
        name = "DevelopmentStage"
        staticDefinition {
            superName = "java/lang/Object"
            "Ljava/lang/String;" occurs 1
            "I" occurs 1
            count = 2
        }
    }
}