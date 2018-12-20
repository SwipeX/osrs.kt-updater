package kt.osrs.analysis.model

import kt.osrs.analysis.classIdentity

/**
 * Created by IntelliJ IDEA.
 * User: Mihael Bercic
 * Date: 18. 12. 2018
 * Time: 14:00
 */
class AudioTrack : Identifiable() {

    override val executeIndex = 96
    override val identity = classIdentity {
        name = "AudioTrack"
        staticDefinition {
            superName = "java/lang/Object"
            "I" occurs 2
            "[L{AudioInstrument};" occurs 1
            count = 3
        }
    }
}