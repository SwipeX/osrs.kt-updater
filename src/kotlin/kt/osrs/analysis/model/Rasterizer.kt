package kt.osrs.analysis.model

import kt.osrs.analysis.classIdentity

/**
 * Created by IntelliJ IDEA.
 * User: Mihael Bercic
 * Date: 18. 12. 2018
 * Time: 13:35
 */
class Rasterizer : Identifiable() {

    override val executeIndex = 66
    override val identity = classIdentity {
        name = "Rasterizer"
        staticDefinition {
            superName = "{CacheableNode}"
            count = 0
        }
    }
}