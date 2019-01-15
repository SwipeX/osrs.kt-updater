package kt.osrs.analysis.model

import kt.osrs.analysis.classIdentity
import kt.osrs.analysis.tree.dsl.NodeSequence
import org.objectweb.asm.Opcodes.*

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
            name = "quantity"
            desc = "I"
            sequence = NodeSequence {
                !fmn() {
                    fmn() {
                        vn(ALOAD, 0)
                    } and vn(ILOAD, 5)
                }
            }
        }
        memberIdentity {
            name = "price"
            desc = "I"
            sequence = NodeSequence {
                !fmn() {
                    fmn() {
                        vn(ALOAD, 0)
                    } and vn(ILOAD, 4)
                }
            }
        }
        memberIdentity {
            name = "itemID"
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
            name = "transferred"
            desc = "I"
            sequence = NodeSequence {
                node(IASTORE) {
                   !fmn() {
                        node(AALOAD) {
                            fmn(GETSTATIC) and vn()
                        }
                    }
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