package kt.osrs.analysis.model

import kt.osrs.analysis.classIdentity

class NodeByteBuffer : Identifiable() {

    override val executeIndex = 31
    override val identity = classIdentity {
        name = "NodeByteBuffer"
        staticDefinition {
            superName = "{Node}"
            "I" occurs 1
            "[B" occurs 1
            hasMethod("()I")
        }
    }
}