package kt.osrs.analysis.model

import kt.osrs.analysis.classIdentity
import kt.osrs.analysis.tree.dsl.NodeSequence

class Actor : Identifiable() {
    override val executeIndex = 7
    override val identity = classIdentity {
        name = "Actor"
        staticDefinition {
            superName = "{RenderableNode}"
            "Ljava/lang/String;" occurs 1
        }

        memberIdentity {
            name = "animation"
            desc = "I"
            //treePattern { opcodes(IF_ICMPLE, IALOAD, GETFIELD, INVOKESTATIC, GETFIELD) }
        }
        memberIdentity {
            name = "interactingIndex"
            desc = "I"
            sequence = NodeSequence {
                vn {
                    this[vn {
                        this[an {
                            this[fmn()]
                        }]
                    }]
                }
            }
         //   treePattern { opcodes(ASTORE, AALOAD, ISUB, GETFIELD) }
        }
        memberIdentity {
            name = "localX"
            desc = "I"
//            treePattern {
//                opcodes(PUTFIELD)
//                leafElement = Pair(ILOAD, 3)
//            }
        }
        memberIdentity {
            name = "localY"
            desc = "I"
//            treePattern {
//                opcodes(PUTFIELD)
//                leafElement = Pair(ILOAD, 4)
//            }
        }
    }
}