package kt.osrs.analysis.model

import kt.osrs.analysis.classIdentity

/**
 * Created by IntelliJ IDEA.
 * User: Mihael Bercic
 * Date: 17. 12. 2018
 * Time: 12:56
 */
class ISAACCipher : Identifiable() {

    override val executeIndex = 41
    override val identity = classIdentity {
        name = "ISAACCipher"
        staticDefinition {
            superName = "java/lang/Object"
            "I" occurs 4
            "[I" occurs 2
        }
    }
}