package kt.osrs

import kt.osrs.ClassAnalyser.classes
import kt.osrs.ClassAnalyser.graphs
import kt.osrs.analysis.ClassIdentity
import kt.osrs.analysis.tree.NodeVisitor
import kt.osrs.analysis.tree.flow.BlockVisitor
import kt.osrs.analysis.tree.node.FieldMemberNode

object MemberAnalyser {
    fun identify(id: ClassIdentity, name: String) {
        // Searching for members
        if (id.members.isNotEmpty()) {
            //TODO instruction kt.osrs.analysis.tree search
            id.members.forEach { memberIdentity ->
                memberIdentity.treePattern?.apply {
                    classes?.values?.forEach {
                        val graphz = graphs[it]
                        it.methods.forEach {
                            val graph = graphz!![it]
                            graph?.forEach {
                                object : BlockVisitor() {
                                    override fun visit(block: kt.osrs.analysis.tree.flow.Block) {
                                        if (opcodes.size == 1) {
                                            //We are only searching for one get/put...hopefully?
                                            block.tree().accept(object : NodeVisitor() {
                                                override fun visitField(node: FieldMemberNode) {
                                                    if (node.desc() == interpolate(memberIdentity.desc!!) && (!static && node.owner() == name)) {
                                                        //If leaf var exists, return if it fails
                                                        if (leafElement.first != -1 && leafElement.second != -1) {
                                                            if (!node.leafVariable(leafElement.first, leafElement.second)) {
                                                                return
                                                            }
                                                        }
                                                        memberIdentity.foundName = node.name()!!
                                                        memberIdentity.foundOwnerName = node.owner()!!
                                                    }
                                                }
                                            })
                                        } else {
                                            //search for tree
                                            val node = block.tree().leaf(*opcodes.toIntArray())
                                            //TODO opcode index -- scale up parent 'n' times
                                            if (node != null && node is FieldMemberNode) {
                                                //check that the desc/owner matches
                                                if (node.desc() == interpolate(memberIdentity.desc!!) && (!static || node.owner() == name)) {
                                                    //If leaf var exists, return if it fails
                                                    if (leafElement.first != -1 && leafElement.second != -1) {
                                                        if (!node.leafVariable(leafElement.first, leafElement.second)) {
                                                            return
                                                        }
                                                    }
                                                    memberIdentity.foundName = node.name()!!
                                                    memberIdentity.foundOwnerName = node.owner()!!
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
