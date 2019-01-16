package kt.osrs.analysis.model

import kt.osrs.analysis.classIdentity

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

        }

        memberIdentity {
            name = "name"
            desc = "Ljava/lang/String;"

        }
    }
}