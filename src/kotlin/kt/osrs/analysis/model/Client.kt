package kt.osrs.analysis.model

import kt.osrs.analysis.classIdentity
import org.objectweb.asm.Opcodes.*

class Client : Identifiable() {
    override val executeIndex: Int = 1
    override val identity = classIdentity {
        name = "Client"
        staticDefinition {
            superName = "{RSApplet}"
        }

        memberIdentity {
            name = "getNpcs"
            desc = "[L{Npc};"
            static = true
            nodeSequence {
                jn() {
                    node(AALOAD) {
                        !fmn(GETSTATIC)
                    }
                }
            }
        }
        memberIdentity {
            name = "getPlayers"
            desc = "[L{Player};"
            static = true
            nodeSequence {
                jn() {
                    node(AALOAD) {
                        !fmn(GETSTATIC)
                    }
                }
            }
        }
        memberIdentity {
            name = "cameraX"
            desc = "I"
            static = true
            nodeSequence {
                vn {
                    mmn(INVOKESTATIC, null, "(III)I") {
                        !fmn(GETSTATIC) and fmn(GETSTATIC) and fmn(GETSTATIC)
                    }
                }
            }
        }
        memberIdentity {
            name = "cameraY"
            desc = "I"
            static = true
            nodeSequence {
                vn {
                    mmn(INVOKESTATIC, null, "(III)I") {
                        fmn(GETSTATIC) and !fmn(GETSTATIC) and fmn(GETSTATIC)
                    }
                }
            }
        }

        memberIdentity {
            name = "plane"
            desc = "I"
            static = true
            nodeSequence {
                vn() {
                    mmn(INVOKESTATIC, null, "(III)I") {
                        fmn(GETSTATIC) and fmn(GETSTATIC) and !fmn(GETSTATIC)
                    }
                }
            }
        }
        memberIdentity {
            name = "baseX"
            desc = "I"
            static = true
            nodeSequence {
                mmn(INVOKESTATIC, null, "(III)V") {
                    node(IADD) {
                        node(ISHL) {
                            node(ISUB) {
                                fmn(GETSTATIC) and !fmn(GETSTATIC)
                            } and nn(BIPUSH, 7)
                        }
                    } and node(IADD) {
                        node(ISHL) {
                            node(ISUB) {
                                fmn(GETSTATIC) and fmn(GETSTATIC)
                            } and nn(BIPUSH, 7)
                        }
                    }
                }
            }
        }
        memberIdentity {
            name = "baseY"
            desc = "I"
            static = true
            nodeSequence {
                mmn(INVOKESTATIC, null, "(III)V") {
                    node(IADD) {
                        node(ISHL) {
                            node(ISUB) {
                                fmn(GETSTATIC) and fmn(GETSTATIC)
                            } and nn(BIPUSH, 7)
                        }
                    } and node(IADD) {
                        node(ISHL) {
                            node(ISUB) {
                                fmn(GETSTATIC) and !fmn(GETSTATIC)
                            } and nn(BIPUSH, 7)
                        }
                    }
                }
            }
        }
    }
}