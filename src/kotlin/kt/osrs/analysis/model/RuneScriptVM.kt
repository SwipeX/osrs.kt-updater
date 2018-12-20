package kt.osrs.analysis.model

import kt.osrs.analysis.classIdentity

/**
 * Created by IntelliJ IDEA.
 * User: Mihael Bercic
 * Date: 18. 12. 2018
 * Time: 13:43
 */
class RuneScriptVM : Identifiable() {

    override val executeIndex = 73
    override val identity = classIdentity {
        name = "RuneScriptVM"
        staticDefinition {
            superName = "java/lang/Object"
            "Z" occurs 1
            "[Ljava/lang/String;" occurs 1
            "[I" occurs 1
            "J" occurs 1
            "[Z" occurs 2
        }
    }
}