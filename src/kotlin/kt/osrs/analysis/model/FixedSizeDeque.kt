package kt.osrs.analysis.model

import kt.osrs.analysis.classIdentity

class FixedSizeDeque : Identifiable() {

    override val executeIndex = 36
    override val identity = classIdentity {
        name = "FixedSizeDeque"
        staticDefinition {
            interfaces.add("java/lang/Iterable")
            "I" occurs 2
            "L{Node};" occurs 2
            "[L{Node};" occurs 1
        }
    }
}