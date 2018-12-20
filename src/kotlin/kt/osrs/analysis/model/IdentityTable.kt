package kt.osrs.analysis.model

import kt.osrs.analysis.classIdentity

/**
 * Created by IntelliJ IDEA.
 * User: Mihael Bercic
 * Date: 18. 12. 2018
 * Time: 13:29
 */
class IdentityTable : Identifiable() {

    override val executeIndex = 64
    override val identity = classIdentity {
        name = "IdentityTable"
        staticDefinition {
            superName = "java/lang/Object"
            "[I" occurs 1
            count = 1
        }
    }
}