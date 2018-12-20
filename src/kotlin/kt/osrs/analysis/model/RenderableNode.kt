package kt.osrs.analysis.model

import kt.osrs.analysis.classIdentity
import org.objectweb.asm.Opcodes

class RenderableNode : Identifiable() {
    override val executeIndex: Int = 5
    override val identity = classIdentity {
        name = "RenderableNode"

        staticDefinition {
            superName = "{CacheableNode}"
            "I" occurs 1
            access = Opcodes.ACC_ABSTRACT
            hasMethod("(IIIIIIIIJ)V")
        }
        memberIdentity {
            name = "height"
            desc = "I"
        }
    }
}