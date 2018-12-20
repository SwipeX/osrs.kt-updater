package kt.osrs.analysis.model

import kt.osrs.analysis.classIdentity

/**
 * Created by IntelliJ IDEA.
 * User: Mihael Bercic
 * Date: 18. 12. 2018
 * Time: 13:14
 */
class SoftReference : Identifiable() {

    override val executeIndex = 55
    override val identity = classIdentity {
        name = "SoftReference"
        staticDefinition {
            superName = "{Reference}"
            "Ljava/lang/ref/SoftReference;" occurs 1
            count = 1
        }
    }
}