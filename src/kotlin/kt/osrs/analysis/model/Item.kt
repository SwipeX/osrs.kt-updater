package kt.osrs.analysis.model

import kt.osrs.analysis.classIdentity
import org.objectweb.asm.Opcodes

class Item : Identifiable() {
    override val executeIndex: Int = 16
    override val identity = classIdentity {
        name = "Item"

        staticDefinition {
            superName = "{RenderableNode}"
            access = Opcodes.ACC_FINAL and Opcodes.ACC_PUBLIC
            "I" occurs 2
        }
    }
}