package kt.osrs.analysis.tree.dsl

class NodeSequence(init: NodeSequence.() -> TreeNode) {
    var tree: TreeNode? = null

    init {
        tree = init(this)
    }

    //AbstractNode
    fun node(opcode: Int = -1) = TreeNode(NodeType.AbstractNode, opcode)
    fun node(opcode: Int = -1, build: TreeNode.() -> Unit) = TreeNode(NodeType.AbstractNode, opcode).apply(build)
    //ArithmeticNode
    fun ar(opcode: Int = -1) = TreeNode(NodeType.ArithmeticNode, opcode)
    fun ar(opcode: Int = -1, build: TreeNode.() -> Unit) = TreeNode(NodeType.ArithmeticNode, opcode).apply(build)
    //ConstantNode
    fun cn(cst: Any? = null, build: TreeNode.() -> Unit) = TreeNode(NodeType.ConstantNode).value(cst).apply(build)
    fun cn(cst: Any? = null) = TreeNode(NodeType.ConstantNode).value(cst)
    fun cn(opcode: Int = -1, cst: Any? = null, build: TreeNode.() -> Unit) = TreeNode(NodeType.ConstantNode, opcode).value(cst).apply(build)
    fun cn(opcode: Int = -1, cst: Any? = null) = TreeNode(NodeType.ConstantNode, opcode).value(cst)
    //IncNode
    fun inc(number: Int = -1, build: TreeNode.() -> Unit) = TreeNode(NodeType.IncNode).value(number).apply(build)
    fun inc(number: Int = -1) = TreeNode(NodeType.IncNode).value(number)
    fun inc(opcode: Int = -1, number: Int = -1, build: TreeNode.() -> Unit) = TreeNode(NodeType.IncNode, opcode).value(number).apply(build)
    fun inc(opcode: Int = -1, number: Int = -1) = TreeNode(NodeType.IncNode, opcode).value(number)
    //VariableNode
    fun vn(number: Int = -1, build: TreeNode.() -> Unit) = TreeNode(NodeType.VariableNode).value(number).apply(build)
    fun vn(number: Int = -1) = TreeNode(NodeType.VariableNode).value(number)
    fun vn(opcode: Int = -1, number: Int = -1, build: TreeNode.() -> Unit) = TreeNode(NodeType.VariableNode, opcode).value(number).apply(build)
    fun vn(opcode: Int = -1, number: Int = -1) = TreeNode(NodeType.VariableNode, opcode).value(number)
    //TypeNode
    fun tn(type: String? = null, build: TreeNode.() -> Unit) = TreeNode(NodeType.ConstantNode).value(type).apply(build)
    fun tn(type: String? = null) = TreeNode(NodeType.ConstantNode).value(type)
    fun tn(opcode: Int = -1, type: Any? = null, build: TreeNode.() -> Unit) = TreeNode(NodeType.ConstantNode, opcode).value(type).apply(build)
    fun tn(opcode: Int = -1, type: Any? = null) = TreeNode(NodeType.ConstantNode, opcode).value(type)
    //FieldMemberNode
    fun fmn(owner: String? = null) = TreeNode(NodeType.FieldMemberNode).owner(owner)
    fun fmn(owner: String? = null, build: TreeNode.() -> Unit) = TreeNode(NodeType.FieldMemberNode).owner(owner).apply(build)
    fun fmn(owner: String? = null, desc: String? = null) = TreeNode(NodeType.FieldMemberNode).owner(owner).desc(desc)
    fun fmn(owner: String? = null, desc: String? = null, build: TreeNode.() -> Unit) = TreeNode(NodeType.FieldMemberNode).owner(owner).desc(desc).apply(build)
    fun fmn(opcode: Int = -1, owner: String? = null) = TreeNode(NodeType.FieldMemberNode, opcode).owner(owner)
    fun fmn(opcode: Int = -1, owner: String? = null, build: TreeNode.() -> Unit) = TreeNode(NodeType.FieldMemberNode, opcode).owner(owner).apply(build)
    fun fmn(opcode: Int = -1, owner: String? = null, desc: String? = null) = TreeNode(NodeType.FieldMemberNode, opcode).owner(owner).desc(desc)
    fun fmn(opcode: Int = -1, owner: String? = null, desc: String? = null, build: TreeNode.() -> Unit) = TreeNode(NodeType.FieldMemberNode, opcode).owner(owner).desc(desc).apply(build)
    //MethodMemberNode
    fun mmn(owner: String? = null) = TreeNode(NodeType.MethodMemberNode).owner(owner)
    fun mmn(owner: String? = null, build: TreeNode.() -> Unit) = TreeNode(NodeType.MethodMemberNode).owner(owner).apply(build)
    fun mmn(owner: String? = null, desc: String? = null) = TreeNode(NodeType.MethodMemberNode).owner(owner).desc(desc)
    fun mmn(owner: String? = null, desc: String? = null, build: TreeNode.() -> Unit) = TreeNode(NodeType.MethodMemberNode).owner(owner).desc(desc).apply(build)
    fun mmn(opcode: Int = -1, owner: String? = null) = TreeNode(NodeType.MethodMemberNode, opcode).owner(owner)
    fun mmn(opcode: Int = -1, owner: String? = null, build: TreeNode.() -> Unit) = TreeNode(NodeType.MethodMemberNode, opcode).owner(owner).apply(build)
    fun mmn(opcode: Int = -1, owner: String? = null, desc: String? = null) = TreeNode(NodeType.MethodMemberNode, opcode).owner(owner).desc(desc)
    fun mmn(opcode: Int = -1, owner: String? = null, desc: String? = null, build: TreeNode.() -> Unit) = TreeNode(NodeType.MethodMemberNode, opcode).owner(owner).desc(desc).apply(build)
}