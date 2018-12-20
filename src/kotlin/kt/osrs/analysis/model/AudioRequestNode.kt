package kt.osrs.analysis.model

import kt.osrs.analysis.classIdentity

/**
 * Created by IntelliJ IDEA.
 * User: Mihael Bercic
 * Date: 18. 12. 2018
 * Time: 13:53
 */
class AudioRequestNode : Identifiable() {

    override val executeIndex = 84
    override val identity = classIdentity {
        name = "AudioRequestNode"
        staticDefinition {
            superName = "{TaskDataNode}"
            "I" occurs 14
            "Z" occurs 1
        }
    }
}