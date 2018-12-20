package kt.osrs.analysis.model

import kt.osrs.analysis.classIdentity

/**
 * Created by IntelliJ IDEA.
 * User: Mihael Bercic
 * Date: 18. 12. 2018
 * Time: 13:38
 */
class IdentityKitNode : Identifiable() {

    override val executeIndex = 69
    override val identity = classIdentity {
        name = "IdentityKitNode"
        staticDefinition {
            superName = "{CacheableNode}"
            "[L{IdentityKit};" occurs 1
        }
    }
}