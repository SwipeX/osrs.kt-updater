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
            //TODO instruction kt.osrs.analysis.tree search
            id.members.forEach { memberIdentity ->
                memberIdentity.sequence?.apply {
                    classes?.values?.forEach {
                        val graphz = graphs[it]
                        it.methods.forEach {
                            val graph = graphz!![it]
                            graph?.forEach {
                                object : BlockVisitor() {
                                    override fun visit(block: kt.osrs.analysis.tree.flow.Block) {
                                        val treeNode = memberIdentity.sequence!!.tree!!
                                        block.tree().filter { matches(treeNode, it) }.forEach {
                                            println(it.tree())
                                        }
                                        //add
                                        val node = null
                                        //TODO opcode index -- scale up parent 'n' times
//                                        if (node != null && node is FieldMemberNode) {
//                                            //check that the desc/owner matches
//                                            if (node.desc() == interpolate(memberIdentity.desc!!) && (static || node.owner() == name)) {
//                                                //If leaf var exists, return if it fails
//                                                if (leafElement.first != -1 && leafElement.second != -1) {
//                                                    if (!node.leafVariable(leafElement.first, leafElement.second)) {
//                                                        return
//                                                    }
//                                                }
//                                                memberIdentity.foundName = node.name()!!
//                                                memberIdentity.foundOwnerName = node.owner()!!
//                                            }
//                                        }
                                    }
                                }.visit(it)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun matches(treeNode: TreeNode, node: AbstractNode):Boolean {
        if(treeNode.accepts(node)) {
            treeNode.children.forEachIndexed { index, it ->  if(!matches(it, node[index]) ) return false }
        }
        return true
    }
}
