package kt.osrs.analysis.tree.node

import kt.osrs.analysis.tree.NodeTree
import kt.osrs.analysis.tree.NodeVisitor
import kt.osrs.analysis.tree.Tree
import org.objectweb.asm.Opcodes
import org.objectweb.asm.commons.util.Assembly
import org.objectweb.asm.tree.*
import org.objectweb.asm.tree.AbstractInsnNode.*
import java.util.*

open class AbstractNode(private val tree: NodeTree?, private var insn: AbstractInsnNode?, var collapsed: Int, producing: Int) :
        Tree<AbstractNode>(), Opcodes {
    var producing: Int = 0
    private var produceCount: Int = 0
    var isHandler: Boolean = false

    private var instructions: Array<AbstractInsnNode> = arrayOf()

    init {
        this.produceCount = producing
        this.producing = producing
    }

    fun caller(): ClassNode {
        return tree?.method()?.owner!!
    }

    open fun accept(nv: NodeVisitor) {
        nv.visitAny(this)
        when (insn!!.type()) {
            INSN -> {
                if (opcode() >= Opcodes.ICONST_M1 && opcode() <= Opcodes.DCONST_1) {
                    nv.visitNumber(this as kt.osrs.analysis.tree.node.NumberNode)
                } else if (opcode() >= Opcodes.I2L && opcode() <= Opcodes.I2S) {
                    nv.visitConversion(this as ConversionNode)
                } else if (opcode() >= Opcodes.IADD && opcode() <= Opcodes.LXOR) {
                    nv.visitOperation(this as ArithmeticNode)
                } else {
                    nv.visit(this)
                }
            }
            INT_INSN -> {
                nv.visitNumber(this as kt.osrs.analysis.tree.node.NumberNode)
            }
            VAR_INSN -> {
                nv.visitVariable(this as VariableNode)
            }
            TYPE_INSN -> {
                nv.visitType(this as kt.osrs.analysis.tree.node.TypeNode)
            }
            FIELD_INSN -> {
                nv.visitField(this as FieldMemberNode)
            }
            METHOD_INSN -> {
                nv.visitMethod(this as MethodMemberNode)
            }
            JUMP_INSN -> {
                nv.visitJump(this as kt.osrs.analysis.tree.node.JumpNode)
            }
            LABEL -> {
                nv.visitLabel(this)
            }
            LDC_INSN -> {
                val cst = (insn() as LdcInsnNode).cst
                if (cst != null && cst is Number) {
                    nv.visitNumber(this as kt.osrs.analysis.tree.node.NumberNode)
                } else {
                    nv.visitConstant(this as ConstantNode)
                }
            }
            IINC_INSN -> {
                nv.visitIinc(this as IncNode)
            }
            TABLESWITCH_INSN -> {
                nv.visitTableSwitch(this)
            }
            LOOKUPSWITCH_INSN -> {
                nv.visitLookupSwitch(this)
            }
            MULTIANEWARRAY_INSN -> {
                nv.visitMultiANewArray(this)
            }
            FRAME -> {
                nv.visitFrame(this)
            }
            LINE -> {
                nv.visitLine(this)
            }
        }
    }

    open fun collapse(): Array<AbstractInsnNode> {
        if (instructions.isNotEmpty()) {
            return instructions
        }
        instructions = emptyArray()
        var i = 0
        for (n in this) {
            val nodes = n.collapse()
            instructions = instructions.plus(nodes)
            i += nodes.size
        }
        if (instructions.size - i != 1) {
            throw RuntimeException()
        }
        instructions[i] = insn()!!
        return instructions
    }

    override fun equals(obj: Any?): Boolean {
        return obj is AbstractNode && Assembly.instructionsEqual(obj.insn(), insn())
    }

    open fun insn(): AbstractInsnNode? {
        return insn
    }

    open fun method(): MethodNode {
        return tree!!.method()
    }

    fun opcode(): Int {
        return if (insn != null) insn!!.opcode() else -1
    }

    fun pop() {
        parent()!!.remove(this)
    }

    fun hasFirst(): Boolean {
        return first() != null
    }

    fun first(): AbstractNode? {
        return child(0)
    }

    fun child(idx: Int): AbstractNode? {
        var i = 0
        for (n in this) {
            if (i == idx) {
                return n
            }
            i++
        }
        return null
    }

    fun producing(): Array<AbstractNode> {
        val nodes = arrayOfNulls<AbstractNode>(size)
        var i = 0
        for (n in this) {
            if (n.produceCount > 0) {
                nodes[i++] = n
            }
        }
        return Arrays.copyOf(nodes, i)
    }

    fun delete() {
        parent()!!.remove(this)
    }

    fun setInstruction(insn: AbstractInsnNode) {
        this.insn = insn
    }

    override fun toString(): String {
        return toString(1)
    }

    open fun toString(tab: Int): String {
        val sb = StringBuilder()
        sb.append(Assembly.toString(insn))
        for (n in this) {
            sb.append('\n')
            for (i in 0 until tab) {
                sb.append('\t')
            }
            sb.append(n.toString(tab + 1))
        }
        return sb.toString()
    }

    fun total(): Int {
        var size = 1
        for (n in this) {
            size += n.total()
        }
        return size
    }

    fun children(): Int {
        return producing().size
    }

    fun tree(): NodeTree {
        return tree!!
    }

    fun index(): Int {
        return method().instructions.indexOf(insn())//insn.insnIndex;
    }

    fun first(opcode: Int): AbstractNode? {
        for (n in this) {
            if (n.opcode() == opcode) {
                return n
            }
        }
        return null
    }

    fun find(opcode: Int, index: Int): AbstractNode? {
        var i = 0
        for (n in this) {
            if (n.opcode() == opcode) {
                if (i++ == index) {
                    return n
                }
            }
        }
        return null
    }

    fun last(opcode: Int): AbstractNode? {
        var last: AbstractNode? = null
        for (n in this) {
            if (n.opcode() == opcode) {
                last = n
            }
        }
        return last
    }

    fun <T : AbstractNode> first(clazz: Class<out AbstractNode>): T? {
        for (n in this) {
            if (n.javaClass == clazz) {
                return n as T
            }
        }
        return null
    }

    fun firstNumber(): kt.osrs.analysis.tree.node.NumberNode? {
        return first(kt.osrs.analysis.tree.node.NumberNode::class.java)
    }

    fun firstOperation(): ArithmeticNode? {
        return first(ArithmeticNode::class.java)
    }

    fun firstReference(): ReferenceNode? {
        return first(ReferenceNode::class.java)
    }

    fun firstField(): FieldMemberNode? {
        for (n in this) {
            if (n is ReferenceNode) {
                if (n.insn() is FieldInsnNode) {
                    return n as FieldMemberNode
                }
            }
        }
        return null
    }

    fun firstMethod(): MethodMemberNode? {
        for (n in this) {
            if (n is ReferenceNode) {
                if (n.insn() is MethodInsnNode) {
                    return n as MethodMemberNode
                }
            }
        }
        return null
    }

    fun firstVariable(): VariableNode? {
        return first(VariableNode::class.java)
    }

    fun firstConstant(): ConstantNode? {
        return first(ConstantNode::class.java)
    }

    fun firstType(): kt.osrs.analysis.tree.node.TypeNode? {
        return first(kt.osrs.analysis.tree.node.TypeNode::class.java)
    }

    fun firstJump(): kt.osrs.analysis.tree.node.JumpNode? {
        return first(kt.osrs.analysis.tree.node.JumpNode::class.java)
    }

    fun <T : AbstractNode> next(clazz: Class<out AbstractNode>, max: Int): T? {
        var i = 0
        var node: AbstractNode? = next()
        while (node != null && i++ < max) {
            if (node.javaClass == clazz) {
                return node as T?
            }
            node = node.next()
        }
        return null
    }

    fun nextNumber(): kt.osrs.analysis.tree.node.NumberNode? {
        return next(NumberNode::class.java, 3)
    }

    fun nextOperation(): ArithmeticNode? {
        return next(ArithmeticNode::class.java, 3)
    }

    @JvmOverloads
    fun nextField(max: Int = 1): FieldMemberNode? {
        return next(FieldMemberNode::class.java, max)
    }

    @JvmOverloads
    fun nextMethod(max: Int = 1): MethodMemberNode? {
        return next(MethodMemberNode::class.java, max)
    }

    @JvmOverloads
    fun nextJump(max: Int = 1): kt.osrs.analysis.tree.node.JumpNode? {
        return next(kt.osrs.analysis.tree.node.JumpNode::class.java, max)
    }

    @JvmOverloads
    fun nextVariable(max: Int = 1): VariableNode? {
        return next(VariableNode::class.java, max)
    }

    @JvmOverloads
    fun nextConstant(max: Int = 1): ConstantNode? {
        return next(ConstantNode::class.java, max)
    }

    @JvmOverloads
    fun nextType(max: Int = 1): kt.osrs.analysis.tree.node.TypeNode? {
        return next(kt.osrs.analysis.tree.node.TypeNode::class.java, max)
    }

    @JvmOverloads
    fun next(opcode: Int, max: Int = 5): AbstractNode? {
        var i = 0
        var node: AbstractNode? = next()
        while (node != null && i++ < max) {
            if (node!!.opcode() == opcode) {
                return node
            }
            node = node.next()
        }
        return null
    }

    @JvmOverloads
    fun previous(opcode: Int, max: Int = 5): AbstractNode? {
        var i = 0
        var node: AbstractNode? = next()
        while (node != null && i++ < max) {
            if (node.opcode() == opcode) {
                return node
            }
            node = node.previous()
        }
        return null
    }

    fun children(opcode: Int): MutableList<AbstractNode>? {
        val children = filter { n -> n.opcode() == opcode }.toMutableList()
        return if (!children.isEmpty()) children else null
    }

    fun branch(vararg opcodes: Int): MutableList<AbstractNode>? {
        val children = children(opcodes[0]) ?: return null
        if (opcodes.size == 1)
            return children
        for (i in 1 until opcodes.size) {
            val next = mutableListOf<AbstractNode>()
            children.forEach { it.children(opcodes[i])?.let { nodes -> next.addAll(nodes) } }
            if (next.isEmpty()) return null
            children.clear()
            children.addAll(next)
        }
        return children
    }

    fun leafVariable(opcode: Int, variable: Int): Boolean {
        val child = leaf(opcode)
        val parent = parent(opcode)
        return (child != null && (child as VariableNode).variable() == variable) ||
                (parent != null && (parent as VariableNode).variable() == variable)
    }

    fun leaf(vararg opcodes: Int): AbstractNode? {
        if (opcodes.size == 1 && insn?.opcode() == opcodes[0])
            return this
        val nodes = branch(*opcodes)
        return if (nodes != null) nodes[0] else null
    }

    fun parent(vararg opcodes: Int): AbstractNode? {
        var node: AbstractNode? = this
        for (opcode in opcodes) {
            node = node!!.parent()
            if (node == null || node.opcode() != opcode) {
                return null
            }
        }
        return node
    }

    fun adjacent(opcode: Int): AbstractNode? {
        val parent = parent()
        parent?.let {
            val children = parent.children(opcode)
            children?.let { list ->
                return list[0]
            }
        }
        return null
    }

    fun hasChild(opcode: Int): Boolean {
        return first(opcode) != null
    }

    fun opcodeString(): String {
        return try {
            Assembly.OPCODES[opcode()]
        } catch (e: IndexOutOfBoundsException) {
            try {
                insn()!!.javaClass.simpleName
            } catch (err: Exception) {
                insn()!!.toString()
            }
        }
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + tree.hashCode()
        result = 31 * result + (insn?.hashCode() ?: 0)
        result = 31 * result + collapsed
        result = 31 * result + producing
        result = 31 * result + produceCount
        result = 31 * result + isHandler.hashCode()
        result = 31 * result + instructions.contentHashCode()
        return result
    }
}
