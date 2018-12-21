package kt.osrs.analysis.tree.util

import kt.osrs.analysis.tree.NodeTree
import kt.osrs.analysis.tree.node.*
import org.objectweb.asm.Opcodes.*
import org.objectweb.asm.Type
import kt.osrs.analysis.tree.flow.Block
import org.objectweb.asm.tree.*
import java.util.*


/**
 * @author Tyler Sedlar
 */
object TreeBuilder {

    val CDS: IntArray
    val PDS: IntArray

    private var treeIndex = -1

    var create: Long = 0
    var iterate: Long = 0

    init {
        CDS = intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 2, 2, 2, 2, 2, 2, 1, 2, 1, 2, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 4, 3, 4, 3, 3, 3, 3, 1, 2, 1, 2, 3, 2, 3, 4, 2, 2, 4, 2, 4, 2, 4, 2, 4, 2, 4, 2, 4, 2, 4, 2, 4, 2, 4, 2, 4, 1, 2, 1, 2, 2, 3, 2, 3, 2, 3, 2, 4, 2, 4, 2, 4, 0, 1, 1, 1, 2, 2, 2, 1, 1, 1, 2, 2, 2, 1, 1, 1, 4, 2, 2, 4, 4, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 0, 0, 0, 1, 1, 1, 2, 1, 2, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 1, 1, 0, 0)
        PDS = intArrayOf(0, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 1, 1, 1, 2, 2, 1, 1, 1, 0, 0, 1, 2, 1, 2, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 1, 2, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 3, 4, 4, 5, 6, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 0, 2, 1, 2, 1, 1, 2, 1, 2, 2, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0)
    }

    fun getTreeSize(ain: AbstractInsnNode): kt.osrs.analysis.tree.util.TreeSize {
        var c = 0
        var p = 0
        if (ain is InsnNode || ain is IntInsnNode || ain is VarInsnNode ||
                ain is JumpInsnNode || ain is TableSwitchInsnNode ||
                ain is LookupSwitchInsnNode) {
            c = CDS[ain.opcode()]
            p = PDS[ain.opcode()]
        } else if (ain is FieldInsnNode) {
            val fin = ain
            val d = fin.desc[0]
            when (fin.opcode()) {
                GETFIELD -> {
                    c = 1
                    p = if (d == 'D' || d == 'J') 2 else 1
                }
                GETSTATIC -> {
                    c = 0
                    p = if (d == 'D' || d == 'J') 2 else 1
                }
                PUTFIELD -> {
                    c = if (d == 'D' || d == 'J') 3 else 2
                    p = 0
                }
                PUTSTATIC -> {
                    c = if (d == 'D' || d == 'J') 2 else 1
                    p = 0
                }
                else -> {
                    c = 0
                    p = 0
                }
            }
        } else if (ain is MethodInsnNode) {
            val min = ain
            val `as` = Type.getArgumentsAndReturnSizes(min.desc)
            c = (`as` shr 2) - if (min.opcode() == INVOKEDYNAMIC || min.opcode() == INVOKESTATIC) 1 else 0
            p = `as` and 0x03
        } else if (ain is LdcInsnNode) {
            val cst = ain.cst
            p = if (cst is Double || cst is Long) 2 else 1
        } else if (ain is MultiANewArrayInsnNode) {
            c = ain.dims
            p = 1
        }
        return kt.osrs.analysis.tree.util.TreeSize(c, p)
    }

    private fun createNode(ain: AbstractInsnNode, tree: NodeTree, size: kt.osrs.analysis.tree.util.TreeSize): AbstractNode {
        val opcode = ain.opcode()
        if (ain is IntInsnNode) {
            return NumberNode(tree, ain, size.collapsing, size.producing)
        } else if (ain is VarInsnNode) {
            return VariableNode(tree, ain, size.collapsing, size.producing)
        } else if (ain is JumpInsnNode) {
            return JumpNode(tree, ain, size.collapsing, size.producing)
        } else if (ain is FieldInsnNode) {
            return FieldMemberNode(tree, ain, size.collapsing, size.producing)
        } else if (ain is MethodInsnNode) {
            return MethodMemberNode(tree, ain, size.collapsing, size.producing)
        } else if (ain is LdcInsnNode) {
            val cst = ain.cst
            return if (cst is Number) {
                NumberNode(tree, ain, size.collapsing, size.producing)
            } else {
                ConstantNode(tree, ain, size.collapsing, size.producing)
            }
        } else return if (ain is IincInsnNode) {
            IincNode(tree, ain, size.collapsing, size.producing)
        } else if (ain is TypeInsnNode) {
            TypeNode(tree, ain, size.collapsing, size.producing)
        } else {
            if (opcode >= ICONST_M1 && opcode <= DCONST_1) {
                NumberNode(tree, ain, size.collapsing, size.producing)
            } else if (opcode >= I2L && opcode <= I2S) {
                ConversionNode(tree, ain, size.collapsing, size.producing)
            } else if (opcode >= IADD && opcode <= LXOR) {
                ArithmeticNode(tree, ain, size.collapsing, size.producing)
            } else {
                AbstractNode(tree, ain, size.collapsing, size.producing)
            }
        }
    }

    private fun iterate(nodes: List<AbstractNode>): AbstractNode? {
        if (treeIndex < 0) {
            return null
        }
        val node = nodes[treeIndex--]
        if (node.collapsed == 0) {
            return node
        }
        var c = node.collapsed
        while (c != 0) {
            val n = iterate(nodes) ?: break
            val op = n.opcode()
            if (op == MONITOREXIT && node.opcode() == ATHROW) {
                n.producing = 1
            }
            node.addFirst(n)
            val cr = c - n.producing
            if (cr < 0) {
                node.producing += -cr
                n.producing = 0
                break
            }
            c -= n.producing
            n.producing = 0
        }
        return node
    }

    fun build(block: Block): NodeTree {
        val tree = NodeTree(block)
        val nodes = ArrayList<AbstractNode>()
        var start = System.nanoTime()
        nodes.addAll(block.instructions.map { ain -> createNode(ain, tree, getTreeSize(ain)) })
        var end = System.nanoTime()
        create += end - start
        treeIndex = nodes.size - 1
        var node: AbstractNode? = iterate(nodes)
        start = System.nanoTime()
        while (node != null) {
            tree.addFirst(node)
            node = iterate(nodes)
        }
        end = System.nanoTime()
        iterate += end - start
        return tree
    }

    fun build(mn: MethodNode): NodeTree {
        val tree = NodeTree(mn)
        val nodes = ArrayList<AbstractNode>()
        var start = System.nanoTime()
        for (ain in mn.instructions.toArray()) {
            nodes.add(createNode(ain, tree, getTreeSize(ain)))
        }
        var end = System.nanoTime()
        create += end - start
        treeIndex = nodes.size - 1
        var node: AbstractNode? = iterate(nodes)
        start = System.nanoTime()
        while (node != null) {
            tree.addFirst(node)
            node = iterate(nodes)
        }
        end = System.nanoTime()
        iterate += end - start
        return tree
    }

}
