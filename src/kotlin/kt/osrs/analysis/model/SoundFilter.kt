package kt.osrs.analysis.model

import kt.osrs.analysis.classIdentity

/**
 * Created by IntelliJ IDEA.
 * User: Mihael Bercic
 * Date: 18. 12. 2018
 * Time: 13:59
 */
class SoundFilter : Identifiable() {

    override val executeIndex = 94
    override val identity = classIdentity {
        name = "SoundFilter"
        staticDefinition {
            superName = "java/lang/Object"
            "[[[I" occurs 2
            "[I" occurs 2
            count = 4
        }
    }
}