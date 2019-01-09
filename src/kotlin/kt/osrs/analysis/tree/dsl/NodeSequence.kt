package kt.osrs.analysis.tree.dsl

class NodeSequence(init: NodeSequence.() -> Unit) {
    var tree: TreeNode? = null

    init {
        apply(init)
    }
}