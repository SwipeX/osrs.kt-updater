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
            for (i in 0 until id.members.size) {
                locate(id, id.members[i], name)
            }
        }
    }

    // Searching for members
    fun locate(classIdentity: ClassIdentity, memberIdentity: MemberIdentity, name: String) {
        memberIdentity.sequence?.apply {
            classes?.values?.forEach { clazz ->
                val graphz = graphs[clazz]
                clazz.methods.forEach { method ->
                    val graph = graphz!![method]
                    graph?.forEach { block ->
                        val treeNode = memberIdentity.sequence!!.tree!!
                        var shouldReturn = false
                        block.tree().forEach { matches(treeNode, it) }.let { match ->
                            treeNode.collected().forEach {
                                val node = it.second
                                //Single hook
                                if (it.first.collectName == null) {
                                    if (node.desc == interpolate(memberIdentity.desc!!) && (memberIdentity.static || node.owner == name)) {
                                        memberIdentity.foundName = node.name!!
                                        memberIdentity.foundOwnerName = node.owner!!
                                        shouldReturn = true
                                    }
                                } else {
                                    //Multi hook
                                    classIdentity.members.add(MemberIdentity {
                                        this.desc = node.desc
                                        this.name = it.first.collectName!!
                                        this.foundName = node.name
                                        this.foundOwnerName = node.owner
                                    })
                                    shouldReturn = true
                                }
                            }
                            if (shouldReturn)
                                return
                        }
                    }
                }
            }
        }
    }

    private fun matches(treeNode: TreeNode, node: AbstractNode, nextIndex: Int = 1): Boolean {
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
        if (treeNode.next != null) {
            for (i in nextIndex until node.size) {
                if (matches(treeNode.next!!, node[i], i + 1)) {
                    break
                } else if (i == node.size - 1) return false
            }
        }
        //node is accepted, we will match them together
        treeNode.match = node.insn()
        return true
    }
}

