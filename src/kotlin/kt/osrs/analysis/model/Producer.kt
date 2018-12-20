package kt.osrs.analysis.model

import kt.osrs.analysis.classIdentity

/**
 * Created by IntelliJ IDEA.
 * User: Mihael Bercic
 * Date: 18. 12. 2018
 * Time: 13:00
 */
class Producer : Identifiable() {

    override val executeIndex = 52
    override val identity = classIdentity {
        name = "Producer"
        staticDefinition {
            superName = "java/lang/Object"
            "[I" occurs 1
            "I" occurs 2
            count = 3
        }
    }
}