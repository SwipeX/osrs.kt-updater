package kt.osrs.analysis.tree.dsl

import kt.osrs.analysis.tree.node.*
import kt.osrs.interpolate

class TreeNode(val type: NodeType = NodeType.AbstractNode, val opcode: Int = -1) {
    val children: MutableList<TreeNode> = mutableListOf()
    var parent: TreeNode? = null
    //optional parameters
    var desc: String? = null
    var owner: String? = null
    var value: Any? = null
    var collect: Boolean = false
    var match: AbstractNode? = null

    fun desc(v: String?): TreeNode = apply { desc = v }
    fun owner(v: String?): TreeNode = apply { owner = v }
    fun value(v: Any?): TreeNode = apply { value = v }
    //Sets the next node equal to the parameter, also ensures the parents are equal
    infix fun TreeNode.and(node: TreeNode) = node.let {
        this@TreeNode.children.add(this)
        it.parent = this@TreeNode
        it
    }

    //sets the parent node, also adds as child if needed
    infix fun parent(node: TreeNode) = node.apply {
        parent = this
        if (!node.children.contains(this)) node.children.add(this)
    }

    //adds the node to the list of child nodes, also sets parent of node
    operator fun get(vararg nodes: TreeNode) {
        children.addAll(nodes)
        nodes.forEach { it.parent = this }
    }

    operator fun not() = apply { collect = true }

    fun collected(): MutableList<FieldMemberNode> {
        val nodes = mutableListOf<FieldMemberNode>()
        fun collectAll(node: TreeNode, list: MutableList<FieldMemberNode>) {
            if (node.collect && node.match != null && node.match is FieldMemberNode)
                list.add(node.match as FieldMemberNode)
            node.children.forEach { collectAll(it, list) }
        }
        collectAll(this, nodes)
        return nodes
    }

    //Checks for opcode, type, value, desc, owner --- where applicable or non-null
    fun accepts(node: AbstractNode): Boolean {
        //check against opcode
        if (opcode != -1 && opcode != node.opcode()) return false
        //check against type
        if (type != NodeType.AbstractNode && type != type(node)) return false
        //check against value
        if ((value != null || value == -1)
                && (node is ConstantNode && value != node.cst()
                        || node is IncNode && value != node.increment()
                        || node is NumberNode && value != node.number()
                        || node is VariableNode && value != node.variable())) return false
        //check against desc/owner for field
        if (node is ReferenceNode &&
                (desc != null && (interpolate(desc!!) != node.desc())
                        || (owner != null && interpolate(owner!!) != node.owner()))) return false
        return true
    }
//    fun accepts(node: AbstractNode): Boolean {
//        return when {
//            (opcode != -1 && opcode != node.opcode() || type != NodeType.AbstractNode && type != type(node))
//                    && (node is ConstantNode && value != node.cst()
//                    || node is IncNode && value != node.increment()
//                    || node is NumberNode && value != node.number()
//                    || node is VariableNode && value != node.variable()
//                    || (node is ReferenceNode &&
//                    (desc != null && (interpolate(desc!!) != node.desc()) ||
//                            (owner != null && interpolate(owner!!) != node.owner())))
//                    ) -> false
//            else -> true
//        }
//    }

    private fun type(node: AbstractNode): NodeType {
        val type = NodeType.values().filter { it.name == node.javaClass.simpleName }
        if (type.isNotEmpty()) {
            return type.first()
        }
        return NodeType.AbstractNode
    }
}