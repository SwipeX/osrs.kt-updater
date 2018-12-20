package kt.osrs.analysis.model

import kt.osrs.analysis.classIdentity

class WidgetActionNode : Identifiable() {
    override val executeIndex: Int = 22
    override val identity = classIdentity {
        name = "WidgetActionNode"
        staticDefinition {
            superName = "{Node}"
            "L{Widget};" occurs 2
        }
    }
}