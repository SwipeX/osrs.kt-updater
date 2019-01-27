package kt.osrs.analysis.model

import kt.osrs.analysis.classIdentity
import kt.osrs.analysis.tree.dsl.NodeSequence
import org.objectweb.asm.Opcodes.*

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
            sequence = NodeSequence {
                jn {
                    node(IALOAD) {
                        fmn {
                            mmn(INVOKESTATIC) {
                                !fmn()
                            }
                        }
                    }
                }
            }
        }

        // TODO FIX.
        memberIdentity {
            name = "animationDelay"
            desc = "I"
            nodeSequence {
                fmn {
                    node(ISUB) {
                        !fmn {
                            node(DUP) {
                                vn()
                            }
                        } and nn(ICONST_1)
                    }
                }
            }
        }

        memberIdentity {
            name = "location"
            desc = "I"
            nodeSequence {
                jn(IF_ICMPEQ) {
                    fmn(GETFIELD) {
                        vn(ALOAD)
                    } hooks "orientation" and fmn(GETFIELD) {
                        vn(ALOAD)
                    }
                }
            }
        }

        memberIdentity {
            name = "interactingIndex"
            desc = "I"
            sequence = NodeSequence {
                node(ASTORE) {
                    node(AALOAD) {
                        node(ISUB) {
                            !fmn()
                        }
                    }
                }
            }
        }
        memberIdentity {
            name = "locals"
            desc = "I"
            sequence = NodeSequence {
                mmn(null, "(III)V") {
                    fmn() {
                        vn(ALOAD, 0)
                    } hooks "localX" and fmn() {
                        vn(ALOAD, 0)
                    } hooks "localY" and vn(ILOAD, 1)
                }
            }
        }

    }
}