package kt.osrs

import kt.osrs.ClassAnalyser.classes
import kt.osrs.ClassAnalyser.graphs
import kt.osrs.analysis.ClassIdentity
import kt.osrs.analysis.tree.dsl.TreeNode
import kt.osrs.analysis.tree.flow.BlockVisitor
import kt.osrs.analysis.tree.node.AbstractNode

object MemberAnalyser {
    fun identify(id: ClassIdentity, name: String) {
        // Searching for members
        if (id.members.isNotEmpty()) {
            for (memberIdentity in id.members) {
                var lock = false
                memberIdentity.sequence?.apply {
                    classes?.values?.forEach {
                        if (lock) return
                        val graphz = graphs[it]
                        it.methods.forEach {
                            if (lock) return
                            val graph = graphz!![it]
                            graph?.forEach {
                                if (lock) return
                                object : BlockVisitor() {
                                    override fun visit(block: kt.osrs.analysis.tree.flow.Block) {
                                        val treeNode = memberIdentity.sequence!!.tree!!
                                        block.tree().forEach { matches(treeNode, it) }.let {
                                            val fields = treeNode.collected()
                                            if (fields.size == 1) {
                                                val node = fields[0]
                                                if (node.desc() == interpolate(memberIdentity.desc!!) && (memberIdentity.static || node.owner() == name)) {
                                                    memberIdentity.foundName = node.name()!!
                                                    memberIdentity.foundOwnerName = node.owner()!!
                                                    //better way to return would be nice
                                                    lock = true
                                                }
                                            }
                                        }
                                    }
                                }.visit(it)
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
    //node is accepted, we will match them together
    treeNode.match = node
    //check for children recursively
    if (node.size < treeNode.children.size) return false
    treeNode.children.forEach { child ->
        if (node.none { matches(child, it) }) {
            return false
        }
    }
    //check for parent recursively
    if (treeNode.parent != null) {
        if (node.parent() == null) return false
        if (!matches(treeNode.parent!!, node.parent()!!)) return false
    }
    return true
}

