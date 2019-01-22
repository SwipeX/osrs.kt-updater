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
            name = "npcs"
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
            name = "players"
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
            name = "cameraHooks"
            static = true
            nodeSequence {
                mmn(INVOKEVIRTUAL, null, "(IIIIII)V") {
                    fmn(GETSTATIC) and
                            fmn(GETSTATIC) hooks "cameraX" and
                            fmn(GETSTATIC) hooks "cameraZ" and
                            fmn(GETSTATIC) hooks "cameraY" and
                            fmn(GETSTATIC) hooks "cameraPitch" and
                            fmn(GETSTATIC) hooks "cameraYaw" and
                            vn(ILOAD)
                }
            }
        }

        memberIdentity {
            name = "plane"
            desc = "I"
            static = true
            nodeSequence {
                vn {
                    mmn(INVOKESTATIC, null, "(III)I") {
                        fmn(GETSTATIC) and fmn(GETSTATIC) and !fmn(GETSTATIC)
                    }
                }
            }
        }
        memberIdentity {
            name = "baseLocation"
            desc = "I"
            static = true
            nodeSequence {
                mmn(INVOKESTATIC, null, "(III)V") {
                    node(IADD) {
                        node(ISHL) {
                            node(ISUB) {
                                fmn(GETSTATIC) and fmn(GETSTATIC) hooks "baseX"
                            } and nn(BIPUSH, 7)
                        }
                    } and node(IADD) {
                        node(ISHL) {
                            node(ISUB) {
                                fmn(GETSTATIC) and fmn(GETSTATIC) hooks "baseY"
                            } and nn(BIPUSH, 7)
                        }
                    }
                }
            }
        }
        memberIdentity {
            name = "levels"
            desc = "[I"
            static = true
            nodeSequence {
                tree {
                    vn(ISTORE) {
                        mmn("{PacketBuffer}")
                    } and vn(ISTORE) {
                        mmn("{PacketBuffer}")
                    } and vn(ISTORE) {
                        mmn("{PacketBuffer}")
                    } and node(IASTORE) {
                        fmn(GETSTATIC) hooks "experiences"
                    } and node(IASTORE) {
                        fmn(GETSTATIC) hooks "currentLevels"
                    } and node(IASTORE) {
                        fmn(GETSTATIC) hooks "realLevels"
                    }
                }
            }
        }
    }
}