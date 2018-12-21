package kt.osrs.analysis.tree.flow

import kt.osrs.analysis.tree.flow.graph.Digraph
import org.objectweb.asm.Label
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes.*
import org.objectweb.asm.tree.*
import java.util.*

/**
 * @author Tyler Sedlar
 */
class FlowVisitor : MethodVisitor() {

    private var mn: MethodNode? = null
    private var current: kt.osrs.analysis.tree.flow.Block? = kt.osrs.analysis.tree.flow.Block(Label())
    val blocks: MutableList<kt.osrs.analysis.tree.flow.Block> = mutableListOf()
    val graph = Digraph<kt.osrs.analysis.tree.flow.Block, kt.osrs.analysis.tree.flow.Block>()

    fun accept(mn: MethodNode) {
        current = kt.osrs.analysis.tree.flow.Block(Label())
        blocks.clear()
        blocks.add(current!!)
        graph.flush()
        this.mn = mn
        mn.accept(this)
    }

    /**
     * Constructs blocks for all given labels.
     *
     * @param labels The labels in which to construct blocks for.
     * @return The constructed blocks.
     */
    private fun constructAll(labels: List<LabelNode>): MutableList<Block> {
        val blocks = mutableListOf<Block>()
        for (i in blocks.indices) {
            blocks[i] = construct(labels[i])
        }
        return blocks
    }

    /**
     * Constructs a block for the given label.
     *
     * @param ln  The label to get a block from.
     * @param add true to add the block to the next preds, otherwise false.
     * @return A block for the given label.
     */
    private fun construct(ln: LabelNode, add: Boolean = true): kt.osrs.analysis.tree.flow.Block {
        val label = ln.label
        if (label.info !is kt.osrs.analysis.tree.flow.Block) {
            label.info = kt.osrs.analysis.tree.flow.Block(label)
            if (add) {
                current!!.next = label.info as kt.osrs.analysis.tree.flow.Block
                current!!.next!!.preds.add(current!!.next!!)
            }
            blocks.add(label.info as kt.osrs.analysis.tree.flow.Block)
        }
        return label.info as kt.osrs.analysis.tree.flow.Block
    }

    override fun visitInsn(`in`: InsnNode) {
        current!!.instructions.add(`in`)
        when (`in`.opcode()) {
            RETURN, IRETURN, ARETURN, FRETURN, DRETURN, LRETURN, ATHROW -> {
                current = construct(LabelNode(Label()), false)
            }
        }
    }

    override fun visitIntInsn(iin: IntInsnNode) {
        current!!.instructions.add(iin)
    }

    override fun visitVarInsn(vin: VarInsnNode) {
        current!!.instructions.add(vin)
    }

    override fun visitTypeInsn(tin: TypeInsnNode) {
        current!!.instructions.add(tin)
    }

    override fun visitFieldInsn(fin: FieldInsnNode) {
        current!!.instructions.add(fin)
    }

    override fun visitMethodInsn(min: MethodInsnNode) {
        current!!.instructions.add(min)
    }

    override fun visitInvokeDynamicInsn(idin: InvokeDynamicInsnNode) {
        current!!.instructions.add(idin)
    }

    override fun visitJumpInsn(jin: JumpInsnNode) {
        val opcode = jin.opcode()
        current!!.target = construct(jin.label)
        current!!.target!!.preds.add(current!!.target!!)
        if (opcode != GOTO) {
            current!!.instructions.add(jin)
        }
        val stack = current!!.stack
        current = construct(LabelNode(Label()), opcode != GOTO)
        current!!.stack = stack
    }

    override fun visitLabel(label: Label?) {
        if (label == null || label.info == null) {
            return
        }
        val stack = if (current == null) Stack() else current!!.stack
        current = construct(LabelNode(label))
        current!!.stack = stack
    }

    override fun visitLdcInsn(ldc: LdcInsnNode) {
        current!!.instructions.add(ldc)
    }

    override fun visitIincInsn(iin: IincInsnNode) {
        current!!.instructions.add(iin)
    }

    override fun visitTableSwitchInsn(tsin: TableSwitchInsnNode) {
        construct(tsin.dflt)
        constructAll(tsin.labels)
        current!!.instructions.add(tsin)
    }

    override fun visitLookupSwitchInsn(lsin: LookupSwitchInsnNode) {
        construct(lsin.dflt)
        constructAll(lsin.labels)
        current!!.instructions.add(lsin)
    }

    override fun visitMultiANewArrayInsn(manain: MultiANewArrayInsnNode) {
        current!!.instructions.add(manain)
    }

    override fun visitEnd() {
        val empty = ArrayList<kt.osrs.analysis.tree.flow.Block>()
        for (block in blocks) {
            block.owner = mn
            if (block.isEmpty) {
                empty.add(block)
            }
        }
        blocks.removeAll(empty)
        blocks.sortWith(Comparator { b1, b2 -> mn!!.instructions.indexOf(LabelNode(b1.label)) - mn!!.instructions.indexOf(LabelNode(b2.label)) })
        for (block in blocks) {
            block.index = blocks.indexOf(block)
            if (!graph.containsVertex(block)) {
                graph.addVertex(block)
            }
            if (block.target != null && block.target !== block) {
                if (!graph.containsVertex(block.target!!)) {
                    graph.addVertex(block.target!!)
                }
                graph.addEdge(block, block.target!!)
            }
            if (block.next != null) {
                if (!graph.containsVertex(block.next!!)) {
                    graph.addVertex(block.next!!)
                }
                graph.addEdge(block, block.next!!)
            }
        }
    }
}
