package kt.osrs.analysis.model

import kt.osrs.analysis.classIdentity

/**
 * Created by IntelliJ IDEA.
 * User: Mihael Bercic
 * Date: 18. 12. 2018
 * Time: 13:44
 */
class Nameable : Identifiable() {

    override val executeIndex = 75
    override
    val identity = classIdentity {
        name = "Nameable"
        staticDefinition {
            superName = "java/lang/Object"
            "L{NameComposite};" occurs 2
            count = 2
        }
    }
}