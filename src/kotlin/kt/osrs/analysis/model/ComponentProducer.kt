package kt.osrs.analysis.model

import kt.osrs.analysis.classIdentity

/**
 * Created by IntelliJ IDEA.
 * User: Mihael Bercic
 * Date: 18. 12. 2018
 * Time: 13:01
 */
class ComponentProducer : Identifiable() {

    override val executeIndex = 53
    override val identity = classIdentity {
        name = "ComponentProducer"
        staticDefinition {
            superName = "{Producer}"
            "Ljava/awt/Image;" occurs 1
            "Ljava/awt/Component;" occurs 1
            count = 2
        }
    }
}