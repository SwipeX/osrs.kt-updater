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
            treePattern {
                static = true
                opcodes(IFNULL, AALOAD, GETSTATIC)
            }
        }
        memberIdentity {
            name = "getPlayers"
            desc = "[L{Player};"
            treePattern {
                static = true
                opcodes(IF_ACMPEQ, AALOAD, GETSTATIC)
            }
        }
    }
}