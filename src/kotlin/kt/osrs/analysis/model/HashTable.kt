package kt.osrs.analysis.model

import kt.osrs.analysis.classIdentity

class HashTable : Identifiable() {

    override val executeIndex = 37
    override val identity = classIdentity {
        name = "HashTable"
        staticDefinition {
            "I" occurs 2
            "L{Node};" occurs 2
            "[L{Node};" occurs 1
            hasMethod("(L{Node};J)V")
            hasMethod("()L{Node};")
        }
    }
}