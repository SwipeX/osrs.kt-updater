package kt.osrs.analysis.model

import kt.osrs.analysis.classIdentity

/**
 * Created by IntelliJ IDEA.
 * User: Mihael Bercic
 * Date: 18. 12. 2018
 * Time: 13:58
 */
class GenericTile : Identifiable() {

    override val executeIndex = 92
    override val identity = classIdentity {
        name = "GenericTile"
        staticDefinition {
            superName = "java/lang/Object"
            "Z" occurs 1
            "I" occurs 6
            count = 7
        }
    }
}