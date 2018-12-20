package kt.osrs.analysis.rank.usage

import org.objectweb.asm.tree.MethodNode

data class MemberUsage<T>(val caller: MethodNode, val member: T){
    override fun toString() = "MemberUsage[caller = ${caller.handle}, member = $member]"
}