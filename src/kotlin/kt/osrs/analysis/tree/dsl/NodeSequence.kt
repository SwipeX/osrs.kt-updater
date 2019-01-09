package kt.osrs.analysis.tree.dsl

class NodeSequence(init: NodeSequence.() -> TreeNode) {
    var tree: TreeNode? = null

    init {
        tree = init(this)
    }


    fun vn(build: TreeNode.() -> Unit) = TreeNode(NodeType.VariableNode).apply(build)
    fun vn() = TreeNode(NodeType.VariableNode)

    fun fmn() = TreeNode(NodeType.FieldMemberNode)
    fun fmn(build: TreeNode.() -> Unit) = TreeNode(NodeType.FieldMemberNode).apply(build)

}