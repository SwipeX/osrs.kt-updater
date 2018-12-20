package kt.osrs.analysis.model

import kt.osrs.analysis.classIdentity

class Node : Identifiable() {
    override val executeIndex: Int = 3
    override val identity = classIdentity {
        name = "Node"

        staticDefinition {
            superName = "java/lang/Object"
            count = 3
            "J" occurs 1
            "L{self};" occurs 2
        }
        memberIdentity {
            name = "uid"
            desc = "J"
        }
    }
}