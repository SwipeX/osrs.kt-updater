package kt.osrs.analysis.tree.dsl

class TreeNode(val type: NodeType = NodeType.AbstractNode, val opcode: Int = -1) {
    val children: MutableList<TreeNode> = mutableListOf()
    var next: TreeNode? = null
    var parent: TreeNode? = null

    //Sets the next node equal to the parameter, also ensures the parents are equal
    infix operator fun plus(node: TreeNode) = {
        next = node
        node.parent = parent
    }

    //sets the parent node, also adds as child if needed
    infix operator fun minus(node: TreeNode) = {
        parent = node
        if (!node.children.contains(this)) node.children.add(this)
    }

    //adds the node to the list of child nodes, also sets parent of node
    infix operator fun plusAssign(node: TreeNode) {
        children.add(node)
        node.parent = this
    }
}