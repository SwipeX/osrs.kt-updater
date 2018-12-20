package kt.osrs.analysis.model

import kt.osrs.analysis.classIdentity
import kt.osrs.analysis.rank.usage.GETFIELD
import kt.osrs.analysis.rank.usage.PUTFIELD

/**
 * Created by IntelliJ IDEA.
 * User: Mihael Bercic
 * Date: 14. 12. 2018
 * Time: 12:13
 */
class NameComposite : Identifiable() {

    override val executeIndex = 10 // To identify before player.
    override val identity = classIdentity {
        name = "NameComposite"
        staticDefinition {
            superName = "java/lang/Object"
            interfaces.add("java/lang/Comparable")
            "Ljava/lang/String;" occurs 2
        }

        memberIdentity {
            name = "formattedName"
            desc = "Ljava/lang/String;"
            usageDefinition {
                "{NameComposite}" from "()Z" using GETFIELD x 1
            }
        }

        memberIdentity {
            name = "name"
            desc = "Ljava/lang/String;"
            usageDefinition {
                "{NameComposite}" from "()Ljava/lang/String;" using GETFIELD x 1
            }
        }
    }
}