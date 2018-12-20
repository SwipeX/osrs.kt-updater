package kt.osrs.analysis.model

import kt.osrs.analysis.classIdentity

/**
 * Created by IntelliJ IDEA.
 * User: Mihael Bercic
 * Date: 18. 12. 2018
 * Time: 13:53
 */
class AudioTask : Identifiable() {

    override val executeIndex = 83
    override val identity = classIdentity {
        name = "AudioTask"
        staticDefinition {
            superName = "{TaskDataNode}"
            "I" occurs 4
            "[I" occurs 15
            "L{HashTable};" occurs 1
            "[[Lho;" occurs 2
            "Lht;" occurs 1
            "Lhp;" occurs 1
            "J" occurs 2
            "Z" occurs 1
        }
    }
}