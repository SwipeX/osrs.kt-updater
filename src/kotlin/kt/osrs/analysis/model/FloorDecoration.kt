package kt.osrs.analysis.model

import kt.osrs.analysis.classIdentity

/**
 * Created by IntelliJ IDEA.
 * User: Mihael Bercic
 * Date: 18. 12. 2018
 * Time: 14:04
 */
class FloorDecoration : Identifiable() {

    override val executeIndex = 100
    override val identity = classIdentity {
        name = "FloorDecoration"
        staticDefinition {
            superName = "java/lang/Object"
            "L{RenderableNode};" occurs 1
            "I" occurs 4
            "J" occurs 1
            count = 6
        }
    }
}