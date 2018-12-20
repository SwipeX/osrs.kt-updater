package kt.osrs.analysis.model

import kt.osrs.analysis.classIdentity

/**
 * Created by IntelliJ IDEA.
 * User: Mihael Bercic
 * Date: 18. 12. 2018
 * Time: 13:24
 */
class CombatBar : Identifiable() {

    override val executeIndex = 61
    override val identity = classIdentity {
        name = "CombatBar"
        staticDefinition {
            superName = "{Node}"
            "L{NodeList};" occurs 1
            "L{CombatBarDefinition};" occurs 1
            count = 2
        }
    }
}