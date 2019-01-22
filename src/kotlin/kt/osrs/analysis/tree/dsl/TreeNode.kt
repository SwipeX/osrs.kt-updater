package kt.osrs.analysis.tree.dsl

import kt.osrs.analysis.tree.node.*
import kt.osrs.interpolate
import org.objectweb.asm.tree.AbstractInsnNode
import org.objectweb.asm.tree.FieldInsnNode

class TreeNode(val type: NodeType = NodeType.AbstractNode, val opcode: Int = -1) {
    //core variables
    val children: MutableList<TreeNode> = mutableListOf()
    var parent: TreeNode? = null
    //optional parameters
    var desc: String? = null
    var owner: String? = null
    var value: Any? = null
    //collection data
    var collect: Boolean = false
    var collectName: String? = null
    var match: AbstractInsnNode? = null
    //DSL setters
    fun desc(v: String?): TreeNode = apply { desc = v }

    fun owner(v: String?): TreeNode = apply { owner = v }
    fun value(v: Any?): TreeNode = apply { value = v }
    //Sets the next node equal to the parameter, also ensures the parents are equal
    infix fun TreeNode.and(node: TreeNode) = node.let {
        this@TreeNode.children.add(this)
        it.parent = this@TreeNode
        it
    }

    infix fun TreeNode.hooks(arg: String): TreeNode {
        this@hooks.collectName = arg
        this@hooks.collect = true
        return this
    }

    operator fun not(): TreeNode {
        collect = true
        return this
    }

    fun collected(): MutableList<Pair<TreeNode, FieldInsnNode>> {
        val nodes = mutableListOf<Pair<TreeNode, FieldInsnNode>>()
        fun collectAll(node: TreeNode, list: MutableList<Pair<TreeNode, FieldInsnNode>>) {
            if (node.collect && node.match != null && node.match is FieldInsnNode)
                list.add(Pair(node, node.match as FieldInsnNode))
            node.children.forEach { collectAll(it, list) }
        }
        collectAll(this, nodes)
        return nodes
    }

    //Checks for opcode, type, value, desc, owner --- where applicable or non-null
    fun accepts(node: AbstractNode): Boolean {
        if (opcode == Int.MIN_VALUE) return true
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

    private fun type(node: AbstractNode): NodeType {
        val type = NodeType.values().filter { it.name == node.javaClass.simpleName }
        if (type.isNotEmpty()) {
            return type.first()
        }
        return NodeType.AbstractNode
    }
}