package kt.osrs.analysis.model

import kt.osrs.analysis.classIdentity

class NodeList : Identifiable() {
    override val executeIndex: Int = 6
    override val identity = classIdentity {
        name = "NodeList"
        staticDefinition {
            interfaces = mutableListOf("java/lang/Iterable")
            superName = "java/lang/Object"
            "L{Node};" occurs 2
            count = 2
        }
    }
}
