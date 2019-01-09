package kt.osrs.analysis.tree.dsl

class TreeNode(val type: NodeType = NodeType.AbstractNode, val opcode: Int = -1) {
    val children: MutableList<TreeNode> = mutableListOf()
    var next: TreeNode? = null
    var parent: TreeNode? = null

    //Sets the next node equal to the parameter, also ensures the parents are equal
    operator fun plus(node: TreeNode): TreeNode {
        next = node
        node.parent = parent
        return this
    }

    //sets the parent node, also adds as child if needed
    operator fun minus(node: TreeNode): TreeNode {
        parent = node
        if (!node.children.contains(this)) node.children.add(this)
        return this
    }

    //adds the node to the list of child nodes, also sets parent of node
    operator fun times(node: TreeNode): TreeNode {
        children.add(node)
        node.parent = this
        return this
    }
}