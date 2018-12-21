package kt.osrs.analysis.tree.flow.graph

import org.objectweb.asm.Handle

/**
 * @author Tyler Sedlar
 */
class CallGraph : Digraph<Handle, Handle>() {

    fun addMethodCall(source: Handle, target: Handle) {
        addVertex(target)
        addEdge(source, target)
    }
}
