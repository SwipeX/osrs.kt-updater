package kt.osrs.analysis.tree.node

import kt.osrs.analysis.tree.NodeTree
import org.objectweb.asm.commons.util.Assembly
import org.objectweb.asm.tree.JumpInsnNode

class JumpNode(tree: NodeTree, insn: JumpInsnNode, collapsed: Int, producing: Int) :
    AbstractNode(tree, insn, collapsed, producing) {

    private var target: TargetNode? = null

    override fun insn(): JumpInsnNode? {
        return super.insn() as JumpInsnNode
    }

    fun resolve(): AbstractNode? {
        return target!!.resolve()
    }

    fun setTarget(target: TargetNode?) {
        if (this.target != null) {
            this.target!!.removeTargeter(this)
        }
        if (target != null) {
            target.addTargeter(this)
            insn()!!.label = target.label()
        } else {
            insn()!!.label = null
        }
        this.target = target
    }

    fun target(): TargetNode? {
        return target
    }

    override fun toString(tab: Int): String {
        val sb = StringBuilder()
        sb.append(Assembly.toString(insn()))
        sb.append(' ').append('>').append(' ')
        sb.append(target)
        for (n in this) {
            sb.append('\n')
            for (i in 0 until tab) {
                sb.append('\t')
            }
            sb.append(n.toString(tab + 1))
        }
        return sb.toString()
    }
}