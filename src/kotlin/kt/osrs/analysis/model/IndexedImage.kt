package kt.osrs.analysis.model

import kt.osrs.analysis.classIdentity

/**
 * Created by IntelliJ IDEA.
 * User: Mihael Bercic
 * Date: 18. 12. 2018
 * Time: 13:41
 */
class IndexedImage : Identifiable() {

    override val executeIndex = 71
    override val identity = classIdentity {
        name = "Indexedimage"
        staticDefinition {
            "I" occurs 6
            "[I" occurs 1
            "[B" occurs 1
            count = 8
        }
    }
}