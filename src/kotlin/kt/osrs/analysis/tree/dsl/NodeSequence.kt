package kt.osrs.analysis.tree.dsl

class NodeSequence(init: NodeSequence.() -> TreeNode) {
    var tree: TreeNode? = null

    init {
        tree = init(this)
    }
    //AbstractNode
    fun node(opcode: Int = -1) = TreeNode(NodeType.AbstractNode, opcode)

    fun node(opcode: Int = -1, build: TreeNode.() -> TreeNode) = TreeNode(NodeType.AbstractNode, opcode).apply {
        children.add(build(this))
    }

    //JumpNode
    fun jn(opcode: Int = -1) = TreeNode(NodeType.JumpNode, opcode)

    fun jn(opcode: Int = -1, build: TreeNode.() -> TreeNode) = TreeNode(NodeType.JumpNode, opcode).apply {
        children.add(build(this))
    }

    //ArithmeticNode
    fun an(opcode: Int = -1) = TreeNode(NodeType.ArithmeticNode, opcode)

    fun an(opcode: Int = -1, build: TreeNode.() -> TreeNode) = TreeNode(NodeType.ArithmeticNode, opcode).apply {
        children.add(build(this))
    }

    //ConstantNode
    fun cn(cst: Any? = null, build: TreeNode.() -> TreeNode) = TreeNode(NodeType.ConstantNode).value(cst).apply {
        children.add(build(this))
    }

    fun cn(cst: Any? = null) = TreeNode(NodeType.ConstantNode).value(cst)
    fun cn(opcode: Int = -1, cst: Any? = null, build: TreeNode.() -> TreeNode) = TreeNode(NodeType.ConstantNode, opcode).value(cst).apply {
        children.add(build(this))
    }

    fun cn(opcode: Int = -1, cst: Any? = null) = TreeNode(NodeType.ConstantNode, opcode).value(cst)
    //NumberNode
    fun nn(number: Int = -1, build: TreeNode.() -> TreeNode) = TreeNode(NodeType.NumberNode).value(number).apply {
        children.add(build(this))
    }

    fun nn(number: Int = -1) = TreeNode(NodeType.NumberNode).value(number)
    fun nn(opcode: Int = -1, number: Int = -1, build: TreeNode.() -> TreeNode) = TreeNode(NodeType.NumberNode, opcode).value(number).apply {
        children.add(build(this))
    }

    fun nn(opcode: Int = -1, number: Int = -1) = TreeNode(NodeType.NumberNode, opcode).value(number)
    //IncNode
    fun inc(number: Int = -1, build: TreeNode.() -> TreeNode) = TreeNode(NodeType.IncNode).value(number).apply {
        children.add(build(this))
    }

    fun inc(number: Int = -1) = TreeNode(NodeType.IncNode).value(number)
    fun inc(opcode: Int = -1, number: Int = -1, build: TreeNode.() -> TreeNode) = TreeNode(NodeType.IncNode, opcode).value(number).apply {
        children.add(build(this))
    }

    fun inc(opcode: Int = -1, number: Int = -1) = TreeNode(NodeType.IncNode, opcode).value(number)
    //VariableNode
    fun vn(opcode: Int = -1, build: TreeNode.() -> TreeNode) = TreeNode(NodeType.VariableNode, opcode).apply {
        children.add(build(this))
    }

    fun vn(number: Int = -1) = TreeNode(NodeType.VariableNode).value(number)
    fun vn(opcode: Int = -1, number: Int = -1) = TreeNode(NodeType.VariableNode, opcode).value(number)
    //TypeNode
    fun tn(type: String? = null, build: TreeNode.() -> TreeNode) = TreeNode(NodeType.ConstantNode).value(type).apply {
        children.add(build(this))
    }

    fun tn(type: String? = null) = TreeNode(NodeType.ConstantNode).value(type)
    fun tn(opcode: Int = -1, type: Any? = null, build: TreeNode.() -> TreeNode) = TreeNode(NodeType.ConstantNode, opcode).value(type).apply {
        children.add(build(this))
    }

    fun tn(opcode: Int = -1, type: Any? = null) = TreeNode(NodeType.ConstantNode, opcode).value(type)
    //FieldMemberNode
    fun fmn(owner: String? = null) = TreeNode(NodeType.FieldMemberNode).owner(owner)

    fun fmn(owner: String? = null, build: TreeNode.() -> TreeNode) = TreeNode(NodeType.FieldMemberNode).owner(owner).apply {
        children.add(build(this))
    }

    fun fmn(owner: String? = null, desc: String? = null) = TreeNode(NodeType.FieldMemberNode).owner(owner).desc(desc)
    fun fmn(owner: String? = null, desc: String? = null, build: TreeNode.() -> TreeNode) = TreeNode(NodeType.FieldMemberNode).owner(owner).desc(desc).apply {
        children.add(build(this))
    }

    fun fmn(opcode: Int = -1, owner: String? = null) = TreeNode(NodeType.FieldMemberNode, opcode).owner(owner)
    fun fmn(opcode: Int = -1, owner: String? = null, desc: String? = null) = TreeNode(NodeType.FieldMemberNode, opcode).owner(owner).desc(desc)
    //MethodMemberNode
    fun mmn(owner: String? = null) = TreeNode(NodeType.MethodMemberNode).owner(owner)

    fun mmn(owner: String? = null, build: TreeNode.() -> TreeNode) = TreeNode(NodeType.MethodMemberNode).owner(owner).apply {
        children.add(build(this))
    }

    fun mmn(owner: String? = null, desc: String? = null) = TreeNode(NodeType.MethodMemberNode).owner(owner).desc(desc)
    fun mmn(owner: String? = null, desc: String? = null, build: TreeNode.() -> TreeNode) = TreeNode(NodeType.MethodMemberNode).owner(owner).desc(desc).apply {
        children.add(build(this))
    }

    fun mmn(opcode: Int = -1, owner: String? = null) = TreeNode(NodeType.MethodMemberNode, opcode).owner(owner)
    fun mmn(opcode: Int = -1, owner: String? = null, build: TreeNode.() -> TreeNode) = TreeNode(NodeType.MethodMemberNode, opcode).owner(owner).apply {
        children.add(build(this))
    }

    fun mmn(opcode: Int = -1, owner: String? = null, desc: String? = null) = TreeNode(NodeType.MethodMemberNode, opcode).owner(owner).desc(desc)
    fun mmn(opcode: Int = -1, owner: String? = null, desc: String? = null, build: TreeNode.() -> TreeNode) = TreeNode(NodeType.MethodMemberNode, opcode).owner(owner).desc(desc).apply {
        children.add(build(this))
    }
}