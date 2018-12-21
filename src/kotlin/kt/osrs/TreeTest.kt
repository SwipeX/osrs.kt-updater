package kt.osrs

import kt.osrs.analysis.tree.flow.BlockVisitor
import kt.osrs.analysis.tree.flow.FlowVisitor
import kt.osrs.analysis.tree.flow.graph.FlowGraph
import kt.osrs.event.Stopwatch
import org.objectweb.asm.Opcodes.*
import org.objectweb.asm.commons.util.JarArchive
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.MethodNode
import java.io.File

fun main(args: Array<String>) {
    val deob = "./jars/deob.jar"
    val archive = JarArchive(File(deob))
    val classes: MutableMap<String, ClassNode>? = archive.build()
    val graphs = flowGraphs(classes!!)
    classes.values.forEach {
        val graphz = graphs[it]
        it.methods.forEach {
            val graph = graphz!![it]
            graph?.forEach {
                    playerActions.visit(it)
            }
        }
    }
}

val playerActions = object: BlockVisitor(){
    override fun visit(block: kt.osrs.analysis.tree.flow.Block) {
        val node = block.tree().leaf(ASTORE, INVOKEVIRTUAL, INVOKEVIRTUAL, INVOKEVIRTUAL, INVOKEVIRTUAL, AALOAD, GETFIELD)
        if (node != null)
            println("Found player.Actions @ $node")
    }
}

/**
 * Constructs a Mapping of ClassNode to a Mapping of MethodNode to FlowGraph
 */
fun flowGraphs(map: MutableMap<String, ClassNode>): MutableMap<ClassNode, MutableMap<MethodNode, FlowGraph>> {
    val graphs = mutableMapOf<ClassNode, MutableMap<MethodNode, FlowGraph>>()
    println("Graph generation: ${Stopwatch.elapse {
        map.values.forEach { classNode ->
            val temp = mutableMapOf<MethodNode, FlowGraph>()
            val visitor = FlowVisitor()
            classNode.methods.forEach {
                visitor.accept(it)
                val graph = FlowGraph(it)
                graph.forEach { it.tree() }
                graph.consume(visitor.graph)
                temp[it] = graph
            }
            graphs[classNode] = temp
        }
    }} ms")
    return graphs
}