package kt.osrs.analysis.model

import kt.osrs.analysis.classIdentity

class Applet : Identifiable() {
    override val executeIndex = 0
    override val identity = classIdentity {
        name = "RSApplet"
        staticDefinition { superName = "java/applet/Applet" }

    }
}