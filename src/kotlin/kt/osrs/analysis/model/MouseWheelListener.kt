package kt.osrs.analysis.model

import kt.osrs.analysis.classIdentity

/**
 * Created by IntelliJ IDEA.
 * User: Mihael Bercic
 * Date: 18. 12. 2018
 * Time: 14:07
 */
class MouseWheelListener : Identifiable() {

    override val executeIndex = 102
    override val identity = classIdentity {
        name = "MouseWheelListener"
        staticDefinition {
            superName = "java/lang/Object"
            "I" occurs 1
            count = 1
            hasMethod("(Ljava/awt/Component;)V")
            hasMethod("(Ljava/awt/event/MouseWheelEvent;)V")
        }
    }
}