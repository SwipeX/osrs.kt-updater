package kt.osrs.analysis.model

import kt.osrs.analysis.classIdentity

/**
 * Created by IntelliJ IDEA.
 * User: Mihael Bercic
 * Date: 18. 12. 2018
 * Time: 13:14
 */
class HardReference : Identifiable() {

    override val executeIndex = 55
    override val identity = classIdentity {
        name = "HardReference"
        staticDefinition {
            superName = "{Reference}"
            "Ljava/lang/Object;" occurs 1
            count = 1
        }
    }
}