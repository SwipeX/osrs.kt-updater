package kt.osrs.analysis.model

import kt.osrs.analysis.classIdentity

/**
 * Created by IntelliJ IDEA.
 * User: Mihael Bercic
 * Date: 18. 12. 2018
 * Time: 13:47
 */
class ClassInfo : Identifiable() {

    override val executeIndex = 78
    override val identity = classIdentity {
        name = "ClassInfo"
        staticDefinition {
            superName = "{Node}"
            "I" occurs 2
            "[I" occurs 3
            "[Ljava/lang/reflect/Field;" occurs 1
            "[Ljava/lang/reflect/Method;" occurs 1
            "[[[B" occurs 1
        }
    }
}