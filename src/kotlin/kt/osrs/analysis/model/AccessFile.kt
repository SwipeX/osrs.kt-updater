package kt.osrs.analysis.model

import kt.osrs.analysis.classIdentity

/**
 * Created by IntelliJ IDEA.
 * User: Mihael Bercic
 * Date: 18. 12. 2018
 * Time: 14:00
 */
class AccessFile : Identifiable() {

    override val executeIndex = 97
    override val identity = classIdentity {
        name = "AccessFile"
        staticDefinition {
            superName = "java/lang/Object"
            "J" occurs 2
            "Ljava/io/RandomAccessFile;" occurs 1
            count = 3
        }
    }
}