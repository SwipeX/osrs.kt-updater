package kt.osrs.analysis.model

import kt.osrs.analysis.classIdentity

/**
 * Created by IntelliJ IDEA.
 * User: Mihael Bercic
 * Date: 18. 12. 2018
 * Time: 14:05
 */
class MouseListener : Identifiable() {

    override val executeIndex = 101
    override val identity = classIdentity {
        name = "MouseListener"
        staticDefinition {
            superName = "java/lang/Object"
            hasMethod("(Ljava/awt/event/MouseEvent;)V")
        }
    }
}