package kt.osrs.analysis.model

import kt.osrs.analysis.classIdentity
import kt.osrs.analysis.rank.usage.GETFIELD
import kt.osrs.analysis.rank.usage.GETSTATIC
import kt.osrs.analysis.rank.usage.PUTFIELD
import kt.osrs.analysis.rank.usage.PUTSTATIC

class Client : Identifiable() {
    override val executeIndex: Int = 1
    override val identity = classIdentity {
        name = "Client"
        staticDefinition {
            superName = "{RSApplet}"
        }


        /*
        TODO ADD STATIC MEMBER SEARCH
        memberIdentity {
            name = "getMenuActions"
            desc = "[Ljava/lang/String;"
            static = true
        }*/
    }
}