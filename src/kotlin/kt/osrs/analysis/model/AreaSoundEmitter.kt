package kt.osrs.analysis.model

import kt.osrs.analysis.classIdentity

/**
 * Created by IntelliJ IDEA.
 * User: Mihael Bercic
 * Date: 18. 12. 2018
 * Time: 13:53
 */
class AreaSoundEmitter : Identifiable() {

    override val executeIndex = 85
    override val identity = classIdentity {
        name = "AreaSoundEmitter"
        staticDefinition {
            superName = "{Node}"
            "I" occurs 10
            "L{ObjectDefinition};" occurs 1
            "[I" occurs 1
            "L{AudioRequestNode};" occurs 2
        }
    }
}