package kt.osrs.analysis.model

import jdk.internal.org.objectweb.asm.Opcodes
import kt.osrs.analysis.classIdentity

class InteractableObject : Identifiable() {

    override val executeIndex = 26
    override val identity = classIdentity {
        name = "InteractableObject"
        staticDefinition {
            access = Opcodes.ACC_FINAL
            "I" occurs 12
            "J" occurs 1
            "L{RenderableNode};" occurs 1
        }
    }
}