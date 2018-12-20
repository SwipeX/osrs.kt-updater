package kt.osrs.analysis.model

import kt.osrs.analysis.classIdentity

/**
 * Created by IntelliJ IDEA.
 * User: Mihael Bercic
 * Date: 18. 12. 2018
 * Time: 13:37
 */
class IdentityKit : Identifiable() {

    override val executeIndex = 68
    override val identity = classIdentity {
        name = "IdentityKit"
        staticDefinition {
            superName = "java/lang/Object"
            "L{Skins};" occurs 1
            "Z" occurs 1
            "I" occurs 1
            "[I" occurs 4
        }
    }
}