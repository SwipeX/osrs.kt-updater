package kt.osrs.analysis.model

import kt.osrs.analysis.classIdentity
import org.objectweb.asm.Opcodes

class Reference : Identifiable() {
    override val executeIndex: Int = 10
    override val identity = classIdentity {
            name = "Reference"

            staticDefinition {superName = "{CacheableNode}"
                "I" occurs 1
                access = Opcodes.ACC_ABSTRACT
                hasMethod("()Z")
            }
        }
}