package kt.osrs.analysis.model

import kt.osrs.analysis.classIdentity
import kt.osrs.analysis.tree.dsl.NodeSequence
import org.objectweb.asm.Opcodes.ALOAD
import org.objectweb.asm.Opcodes.ILOAD

class ExchangeOffer : Identifiable() {
    override val executeIndex = 39
    override val identity = classIdentity {
        name = "ExchangeOffer"
        staticDefinition {
            superName = "java/lang/Object"
            "B" occurs 1
            "I" occurs 5
        }
        memberIdentity {
            name = "itemID"
            desc = "I"
//            treePattern {
//                opcodes(PUTFIELD)
//                leafElement = Pair(ILOAD, 3)
//            }
        }
        memberIdentity {
            name = "price"
            desc = "I"
//            treePattern {
//                opcodes(PUTFIELD)
//                leafElement = Pair(ILOAD, 4)
//            }
        }
        memberIdentity {
            name = "quantity"
            desc = "I"
            sequence = NodeSequence {
                !fmn() {
                    fmn() {
                        vn(ALOAD, 0)
                    } and vn(ILOAD, 3)
                }
            }
        }
        memberIdentity {
            name = "state"
            desc = "B"
//            treePattern {
//                opcodes(PUTFIELD)
//            }
        }
    }
}