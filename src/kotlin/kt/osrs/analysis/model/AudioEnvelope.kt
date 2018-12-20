package kt.osrs.analysis.model

import kt.osrs.analysis.classIdentity

/**
 * Created by IntelliJ IDEA.
 * User: Mihael Bercic
 * Date: 18. 12. 2018
 * Time: 13:58
 */
class AudioEnvelope : Identifiable() {

    override val executeIndex = 93
    override val identity = classIdentity {
        name = "AudioEnvelope"
        staticDefinition {
            superName = "java/lang/Object"
            "I" occurs 9
            "[I" occurs 2
            count = 11
        }
    }
}