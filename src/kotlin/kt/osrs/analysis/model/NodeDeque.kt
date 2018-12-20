package kt.osrs.analysis.model

import kt.osrs.analysis.classIdentity

class NodeDeque : Identifiable() {
    override val executeIndex: Int = 8
    override val identity = classIdentity {
        staticDefinition {
            name = "NodeDeque"
            superName = "java/lang/Object"
            "L{Node};" occurs 2
            count = 2
        }
    }
}
