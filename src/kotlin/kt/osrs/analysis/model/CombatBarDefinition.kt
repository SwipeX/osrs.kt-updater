package kt.osrs.analysis.model

import kt.osrs.analysis.classIdentity

/**
 * Created by IntelliJ IDEA.
 * User: Mihael Bercic
 * Date: 18. 12. 2018
 * Time: 13:22
 */
class CombatBarDefinition : Identifiable() {

    override val executeIndex = 59
    override val identity = classIdentity {
        name = "CombatBarDefinition"
        staticDefinition {
            superName = "{CacheableNode}"
            "I" occurs  10
            count = 10
        }
    }
}