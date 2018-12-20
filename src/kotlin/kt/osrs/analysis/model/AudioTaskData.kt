package kt.osrs.analysis.model

import kt.osrs.analysis.classIdentity

/**
 * Created by IntelliJ IDEA.
 * User: Mihael Bercic
 * Date: 18. 12. 2018
 * Time: 13:56
 */
class AudioTaskData : Identifiable() {

    override val executeIndex = 89
    override val identity = classIdentity {
        name = "AudioTaskData"
        staticDefinition {
            superName = "{TaskData}"
            "Ljavax/sound/sampled/AudioFormat;" occurs 1
            "I" occurs 1
            "Ljavax/sound/sampled/SourceDataLine;" occurs 1
            "[B" occurs 1
        }
    }
}