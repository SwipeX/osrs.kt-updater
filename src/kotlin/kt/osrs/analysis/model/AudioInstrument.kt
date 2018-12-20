package kt.osrs.analysis.model

import kt.osrs.analysis.classIdentity

/**
 * Created by IntelliJ IDEA.
 * User: Mihael Bercic
 * Date: 18. 12. 2018
 * Time: 13:59
 */
class AudioInstrument : Identifiable() {

    override val executeIndex = 95
    override val identity = classIdentity {
        name = "AudioInstrument"
        staticDefinition {
            superName = "java/lang/Object"
            "L{AudioEnvelope};" occurs 9
            "[I" occurs 3
            "I" occurs 4
            "L{SoundFilter};" occurs 1
        }
    }
}