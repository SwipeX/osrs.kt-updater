package kt.osrs.analysis.model

import kt.osrs.analysis.classIdentity

/**
 * Created by IntelliJ IDEA.
 * User: Mihael Bercic
 * Date: 18. 12. 2018
 * Time: 13:10
 */
class InventoryDefinition : Identifiable() {

    override val executeIndex = 54
    override val identity = classIdentity {
        name = "InventoryDefinition"
        staticDefinition {
            superName = "{CacheableNode}"
            "I" occurs 1
            count = 1

            /*
            im.<init>()V
	        im.q(L{NodeByteBuffer};I)V
	        im.e(L{NodeByteBuffer};)V
             */
        }
    }
}