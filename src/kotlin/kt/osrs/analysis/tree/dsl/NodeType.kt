package kt.osrs.analysis.tree.dsl

enum class NodeType {
    AbstractNode, ArithmeticNode, ConstantNode, ConversionNode,
    FieldMemberNode, IncNode, JumpNode, MethodMemberNode,
    NumberNode, TargetNode, TypeNode, VariableNode
}