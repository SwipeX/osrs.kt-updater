package kt.osrs.analysis

import kt.osrs.analysis.rank.usage.UsageDefinition
import kt.osrs.analysis.tree.dsl.TreePattern

class MemberIdentity(init: MemberIdentity.() -> Unit) : Identity() {
    init {
        apply(init)
    }

    var ownerName: String? = null
    var foundOwnerName: String? = null
    var desc: String? = null
    var usageDefinition: UsageDefinition? = null
    var treePattern: TreePattern? = null

    fun treePattern(init: TreePattern.() -> Unit) = TreePattern(init).apply {
        treePattern = this
    }

    fun usageDefinition(init: UsageDefinition.() -> Unit) = UsageDefinition(init).apply {
        usageDefinition = this
    }
}