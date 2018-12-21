package kt.osrs.analysis.tree.flow

import kt.osrs.analysis.tree.NodeTree
import kt.osrs.analysis.tree.util.TreeBuilder
import org.objectweb.asm.Label
import org.objectweb.asm.commons.util.Assembly
import org.objectweb.asm.tree.AbstractInsnNode
import org.objectweb.asm.tree.LabelNode
import org.objectweb.asm.tree.MethodNode
import java.util.*

/**
 * @author Tyler Sedlar
 */
class Block
/**
 * Constructs a block for the given label.
 *
 * @param label The label in which to create a block from.
 */
(val label: Label?) : Comparable<Block> {

    var owner: MethodNode? = null
    val instructions: MutableList<AbstractInsnNode> = LinkedList()
    val preds: MutableList<Block> = ArrayList()

    var next: Block? = null
    var target: Block? = null

    var stack = Stack<AbstractInsnNode>()

    private var tree: NodeTree? = null

    var index = -1

    private val lastTree: NodeTree? = null

    /**
     * Checks if the block is empty.
     *
     * @return true if the block is empty, otherwise false.
     */
    val isEmpty: Boolean
        get() = preds.isEmpty() && instructions.size <= 1

    init {
        if (label != null) {
            this.instructions.add(LabelNode(label))
        }
    }

    /**
     * Constructs a NodeTree for the current block.
     */
    fun tree(): NodeTree {
        if (tree == null)
            tree = TreeBuilder.build(this)
        return tree!!
    }

    fun follow(handler: BlockHandler): Boolean {
        val followed = ArrayList<Block>()
        var followIndex = 0
        var next: Block? = this
        followed.add(next!!)
        while (next!!.next != null) {
            next = next.next
            if (followed.contains(next)) {
                break
            }
            followed.add(next!!)
            if (!handler.handle(followIndex++, next)) {
                return false
            }
        }
        return true
    }

    private fun addInstructionsTo(block: Block, list: MutableList<AbstractInsnNode>) {
        for (ain in block.instructions) {
            if (ain is LabelNode) {
                continue
            }
            list.add(ain)
        }
    }


    fun followedBlock(): Block {
        val instructions = LinkedList<AbstractInsnNode>()
        addInstructionsTo(this, instructions)
        follow(object : BlockHandler {
            override fun handle(followIndex: Int, block: kt.osrs.analysis.tree.flow.Block): Boolean{
                addInstructionsTo(block, instructions)
                return true
            }
        })
        val block = Block(null)
        block.instructions.addAll(instructions)
        return block
    }

    override fun compareTo(block: Block): Int {
        return if (index > block.index) 1 else -1
    }

    override fun toString(): String {
        var s = "Block #$index\r\n"
        for (ain in instructions) {
            s += " " + Assembly.toString(ain) + "\r\n"
        }
        return s
    }

    /**
     * Gets the amount of times the given opcodes has been matched
     *
     * @param opcode The opcode to match
     * @return The amount of times the given opcode has been matched.
     */
    fun count(opcode: Int): Int {
        var count = 0
        for (ain in instructions) {
            if (ain.opcode() == opcode) {
                count++
            }
        }
        return count
    }

    /**
     * Gets the matched instruction at the given index
     *
     * @param opcode The opcode of the instruction to match
     * @param index  The index to match at
     * @return The matched instruction at the given index
     */
    @JvmOverloads
    operator fun get(opcode: Int, index: Int = 0): AbstractInsnNode? {
        var i = 0
        for (ain in instructions) {
            if (ain.opcode() == opcode) {
                if (i == index) {
                    return ain
                }
                i++
            }
        }
        return null
    }

}
/**
 * Gets the first matched instruction
 *
 * @param opcode The opcode of the instruction to match
 * @return The first matched instruction
 */
