package kt.osrs.analysis.model

import kt.osrs.analysis.classIdentity

class WidgetNode : Identifiable() {
    override val executeIndex: Int = 23
    override val identity = classIdentity {
        name = "WidgetNode"
        staticDefinition {
            superName = "{Node}"
            "I" occurs 2
            "Z" occurs 1
        }
    }
}