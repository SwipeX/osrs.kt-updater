package kt.osrs

import kt.osrs.ClassAnalyser.classes
import kt.osrs.ClassAnalyser.graphs
import kt.osrs.analysis.ClassIdentity
import kt.osrs.analysis.MemberIdentity
import kt.osrs.analysis.tree.dsl.TreeNode
import kt.osrs.analysis.tree.node.AbstractNode

object MemberAnalyser {
    fun identify(id: ClassIdentity, name: String) {
        if (id.members.isNotEmpty()) {
            for (memberIdentity in id.members) {
                locate(memberIdentity, name)
            }
        }
    }

    // Searching for members
    fun locate(memberIdentity: MemberIdentity, name: String) {
        memberIdentity.sequence?.apply {
            classes?.values?.forEach {
                val graphz = graphs[it]
                it.methods.forEach {
                    val graph = graphz!![it]
                    graph?.forEach {
                        val treeNode = memberIdentity.sequence!!.tree!!
                        it.tree().forEach { matches(treeNode, it) }.let {
                            val fields = treeNode.collected()
                            if (fields.size == 1) {
                                val node = fields[0]
                                if (node.desc == interpolate(memberIdentity.desc!!) && (memberIdentity.static || node.owner == name)) {
                                    memberIdentity.foundName = node.name!!
                                    memberIdentity.foundOwnerName = node.owner!!
                                    return@apply
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun matches(treeNode: TreeNode, node: AbstractNode): Boolean {
        if (!treeNode.accepts(node)) return false
        //check for children recursively
        if (node.size < treeNode.children.size) return false
        var lastIdx = 0
        treeNode.children.forEach { child ->
            for (i in lastIdx until node.size) {
                if (matches(child, node[i])) {
                    lastIdx = i + 1
                    break
                } else if (i == node.size - 1) return false
            }
        }
        //check for parent recursively -- cannot do because of SOF atm
//        if (treeNode.parent != null) {
//            if (node.parent() == null) return false
//            if (!treeNode.parent!!.accepts(node.parent()!!)) return false
//        }
        //node is accepted, we will match them together
        treeNode.match = node.insn()
        return true
    }
}

