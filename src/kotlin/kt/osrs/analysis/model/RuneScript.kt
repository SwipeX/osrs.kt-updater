package kt.osrs.analysis.model

import kt.osrs.analysis.classIdentity

/**
 * Created by IntelliJ IDEA.
 * User: Mihael Bercic
 * Date: 18. 12. 2018
 * Time: 13:42
 */
class RuneScript : Identifiable() {

    override val executeIndex = 72
    override val identity = classIdentity {
        name = "RuneScript"
        staticDefinition {
            superName = "{CacheableNode}"
            "[I" occurs 2
            "I" occurs 4
            "[Ljava/lang/String;" occurs 1
            "[L{FixedSizeDeque};" occurs 1
        }
    }
}