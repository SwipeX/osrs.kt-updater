package kt.osrs.analysis.model

import kt.osrs.analysis.classIdentity

/**
 * Created by IntelliJ IDEA.
 * User: Mihael Bercic
 * Date: 18. 12. 2018
 * Time: 13:30
 */
class ReferenceTable : Identifiable() {

    override val executeIndex = 65
    override val identity = classIdentity {
        name = "ReferenceTable"
        staticDefinition {
            superName = "java/lang/Object"
            "[[I" occurs 2
            "[I" occurs 5
            "[Ljava/lang/Object;" occurs 1
            "[[Ljava/lang/Object;" occurs 1
            "Z" occurs 2
            "[Lgw;" occurs 1
            "Lgw;" occurs 1
            "I" occurs 2
        }
    }
}