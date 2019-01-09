package kt.osrs.analysis.tree.dsl

class TreeNode(val type: NodeType = NodeType.AbstractNode, val opcode: Int = -1) {
    val children: MutableList<TreeNode> = mutableListOf()
    var next: TreeNode? = null
    var parent: TreeNode? = null
    //optional parameters
    var desc: String? = null
    var owner: String? = null
    var value: Any? = null

    fun children(vararg chn: TreeNode) {
        children.addAll(chn)
    }

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


    operator fun get(vararg nodes: TreeNode) {
        children.addAll(nodes)
    }

    //adds the node to the list of child nodes, also sets parent of node
    infix operator fun plusAssign(node: TreeNode) {
        children.add(node)
        node.parent = this
    }
}