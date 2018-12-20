package kt.osrs.analysis.model

import kt.osrs.analysis.classIdentity

/**
 * Created by IntelliJ IDEA.
 * User: Mihael Bercic
 * Date: 18. 12. 2018
 * Time: 13:37
 */
class Skins : Identifiable() {

    override val executeIndex = 67
    override val identity = classIdentity {
        name = "Skins"
        staticDefinition {
            superName = "{Node}"
            "I" occurs 2
            "[I" occurs 1
            "[[I" occurs 1
        }
    }
}