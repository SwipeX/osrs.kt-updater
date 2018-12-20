package kt.osrs.analysis.model

import kt.osrs.analysis.classIdentity

/**
 * Created by IntelliJ IDEA.
 * User: Mihael Bercic
 * Date: 18. 12. 2018
 * Time: 12:33
 */
class Inflater : Identifiable() {

    override val executeIndex = 46
    override val identity = classIdentity {
        name = "Inflater"
        staticDefinition {
            superName = "java/lang/Object"
            "Ljava/util/zip/Inflater;" occurs 1
            count = 1
        }
    }
}