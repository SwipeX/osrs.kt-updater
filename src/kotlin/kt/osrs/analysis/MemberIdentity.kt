package kt.osrs.analysis

import kt.osrs.analysis.tree.dsl.NodeSequence
import kt.osrs.analysis.tree.dsl.TreeNode

class MemberIdentity(init: MemberIdentity.() -> Unit) : Identity() {
    init {
        apply(init)
    }

    var ownerName: String? = null
    var foundOwnerName: String? = null
    var desc: String? = null
    var sequence: NodeSequence? = null
    var static = false

    fun nodeSequence(init: NodeSequence.() -> TreeNode) {
        sequence = NodeSequence(init)
    }
}