package kt.osrs.analysis.tree.dsl

import kt.osrs.analysis.tree.node.*
import kt.osrs.interpolate

class TreeNode(val type: NodeType = NodeType.AbstractNode, val opcode: Int = -1) {
    val children: MutableList<TreeNode> = mutableListOf()
    var next: TreeNode? = null
    var parent: TreeNode? = null
    //optional parameters
    var desc: String? = null
    var owner: String? = null
    var value: Any? = null
    var collect: Boolean = false
    fun desc(v: String?): TreeNode = apply { desc = v }
    fun owner(v: String?): TreeNode = apply { owner = v }
    fun value(v: Any?): TreeNode = apply { value = v }
    //Sets the next node equal to the parameter, also ensures the parents are equal
    infix operator fun plus(node: TreeNode) = node.apply {
        next = this
        parent = this@TreeNode.parent
    }

    //sets the parent node, also adds as child if needed
    infix operator fun minus(node: TreeNode) = node.apply {
        parent = this
        if (!node.children.contains(this)) node.children.add(this)
    }

    //adds the node to the list of child nodes, also sets parent of node
    operator fun get(vararg nodes: TreeNode) {
        children.addAll(nodes)
        nodes.forEach { it.parent = this }
    }

    operator fun not() = apply { collect = true }

    //Checks for opcode, type, value, desc, owner --- where applicable or non-null
    fun accepts(node: AbstractNode): Boolean {

        // YES
        return when {
            opcode != -1 && opcode != node.opcode()
                    || type != NodeType.AbstractNode && type != type(node)
                    || node is ConstantNode && value != node.cst()
                    || node is IncNode && value != node.increment()
                    || node is NumberNode && value != node.number()
                    || node is VariableNode && value != node.variable()
                    || node is ReferenceNode && desc != null
                    && (interpolate(desc!!) != node.desc() || interpolate(owner!!) != node.desc()) -> false
            else                                                                                   -> true
        }
        /*

        if (opcode != -1 && opcode != node.opcode()) return false
        if (type != NodeType.AbstractNode && type != type(node)) return false
        //constantNode, incNode, numberNode, variableNode
        if (node is ConstantNode)
            if (value != null && value != node.cst()) return false
        if (node is IncNode)
            if (value != null && value != node.increment()) return false
        if (node is NumberNode)
            if (value != null && value != node.number()) return false
        if (node is VariableNode)
            if (value != null && value != node.variable()) return false
        //referenceNode
        if (node is ReferenceNode) {
            if (desc != null && interpolate(desc!!) != node.desc()) return false
            if (owner != null && interpolate(owner!!) != node.owner()) return false
        }
        return true
        */
    }

    private fun type(node: AbstractNode): NodeType {
        val type = NodeType.values().filter { it.name == node.javaClass.simpleName }
        if (type.isNotEmpty()) return type.first()
        return NodeType.AbstractNode
    }
}