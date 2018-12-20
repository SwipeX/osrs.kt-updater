package kt.osrs.analysis.model

import kt.osrs.analysis.classIdentity

/**
 * Created by IntelliJ IDEA.
 * User: Mihael Bercic
 * Date: 18. 12. 2018
 * Time: 12:22
 */
class Link : Identifiable() {

    override val executeIndex = 43
    override val identity = classIdentity {
        name = "Link"
        staticDefinition {
            "L{self};" occurs 2
            count = 2
            superName = "java/lang/Object"
        }
    }
}