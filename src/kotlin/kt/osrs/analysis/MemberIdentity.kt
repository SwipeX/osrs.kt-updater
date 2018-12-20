package kt.osrs.analysis

import kt.osrs.analysis.rank.usage.UsageDefinition

class MemberIdentity(init: MemberIdentity.() -> Unit) : Identity() {
    init {
        apply(init)
    }

    var ownerName: String? = null
    var foundOwnerName: String? = null
    var desc: String? = null
    var usageDefinition: UsageDefinition? = null

    fun usageDefinition(init: UsageDefinition.() -> Unit) = UsageDefinition(init).apply {
        usageDefinition = this
    }
}