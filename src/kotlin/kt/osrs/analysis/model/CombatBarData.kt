package kt.osrs.analysis.model

import kt.osrs.analysis.classIdentity

/**
 * Created by IntelliJ IDEA.
 * User: Mihael Bercic
 * Date: 18. 12. 2018
 * Time: 13:23
 */
class CombatBarData : Identifiable() {

    override val executeIndex = 60
    override val identity = classIdentity {
        name = "CombatBarData"
        staticDefinition {
            superName = "{Node}"
            "I" occurs 4
            count = 4
        }
    }
}