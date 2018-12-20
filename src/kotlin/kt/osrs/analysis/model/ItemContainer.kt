package kt.osrs.analysis.model

import kt.osrs.analysis.classIdentity

class ItemContainer : Identifiable() {
    override val executeIndex: Int = 17
    override val identity = classIdentity {
        name = "ItemContainer"
        staticDefinition {
            "[I" occurs 2
            count = 2
            superName = "{Node}"
        }
    }
}