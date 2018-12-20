package kt.osrs.analysis.model

import kt.osrs.analysis.classIdentity

/**
 * Created by IntelliJ IDEA.
 * User: Mihael Bercic
 * Date: 18. 12. 2018
 * Time: 13:52
 */
class MenuItemNode : Identifiable() {

    override val executeIndex = 82
    override val identity = classIdentity {
        name = "MenuItemNode"
        staticDefinition {
            superName = "java/lang/Object"
            "I" occurs 4
            "Ljava/lang/String;" occurs 1
            count = 5
        }
    }
}