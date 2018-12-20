package kt.osrs.analysis.model

import kt.osrs.analysis.classIdentity

class Widget : Identifiable() {
    override val executeIndex: Int = 21
    override val identity = classIdentity {
        name = "Widget"
        staticDefinition {
            superName = "{Node}"
            "I" occurs 71
            "[Ljava/lang/String;" occurs 2
            "[I" occurs 10
            "Z" occurs 16
            "[L{self};" occurs 1
            "[[I" occurs 1
            "[Ljava/lang/Object;" occurs 28
            "Ljava/lang/String;" occurs 6
            "L{self};" occurs 1
            // TODO
            /*
            byte[][] x2
i           int[][] x1
             */
        }
    }
}