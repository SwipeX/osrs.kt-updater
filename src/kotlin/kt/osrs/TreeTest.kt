package kt.osrs

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
    val clazz = classes["es"]!!
    val method = classes["es"]?.getMethod("ig", "(Lbk;III)V")!!
    val flowgraph = graphs[clazz]!![method]!!
    flowgraph.forEachIndexed { index, block ->
        run {
            val node = block.tree().branch(ASTORE, INVOKEVIRTUAL, INVOKEVIRTUAL, INVOKEVIRTUAL, INVOKEVIRTUAL, AALOAD,GETFIELD)
            if (node != null && !node.isEmpty())
                println("Found player.Actions @ ${node.first()}")
        }
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