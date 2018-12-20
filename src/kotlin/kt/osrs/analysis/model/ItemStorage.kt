package kt.osrs.analysis.model

import kt.osrs.analysis.classIdentity

/**
 * Created by IntelliJ IDEA.
 * User: Mihael Bercic
 * Date: 18. 12. 2018
 * Time: 12:36
 */
class ItemStorage : Identifiable() {

    override val executeIndex = 47
    override val identity = classIdentity {
        name = "ItemStorage"
        staticDefinition {
            "[I" occurs 2
        }
    }
}