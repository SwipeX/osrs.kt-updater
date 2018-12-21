package kt.osrs.analysis.tree.flow.graph

import kt.osrs.analysis.tree.flow.Block
import org.objectweb.asm.tree.MethodNode

/**
 * @author Tyler Sedlar
 */
class FlowGraph(private val mn: MethodNode) : Digraph<Block, Block>() {

    fun method(): MethodNode {
        return mn
    }
}
