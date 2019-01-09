package kt.osrs

import kt.osrs.analysis.tree.NodeVisitor
import kt.osrs.analysis.tree.flow.BlockVisitor
import kt.osrs.analysis.tree.flow.FlowVisitor
import kt.osrs.analysis.tree.flow.graph.FlowGraph
import kt.osrs.analysis.tree.node.FieldMemberNode
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



    classes.values.forEach { classNode ->
        val graphz = graphs[classNode]
        classNode.methods.forEach { methodNode ->
            val graph = graphz!![methodNode]
            graph?.forEach {
                playerActions.visit(it)
                exchangeItemID.visit(it)
                exchangeItemPrice.visit(it)
                exchangeItemQuantity.visit(it)
                charAnimation.visit(it)
                players.visit(it)
                npcs.visit(it)
            }
        }
    }
}

val playerActions = object : BlockVisitor() {
    override fun visit(block: kt.osrs.analysis.tree.flow.Block) {
        val node = block.tree().leaf(ASTORE, INVOKEVIRTUAL, INVOKEVIRTUAL, INVOKEVIRTUAL, INVOKEVIRTUAL, AALOAD, GETFIELD)
        if (node != null)
            println("Found player.Actions @ $node")
    }
}

val players = object : BlockVisitor() {
    override fun visit(block: kt.osrs.analysis.tree.flow.Block) {
        val node = block.tree().leaf(IF_ACMPEQ, AALOAD, GETSTATIC)
        if (node != null && (node as FieldMemberNode).desc() == "[Lbk;")
            println("Found players @ $node")
    }
}

val npcs = object : BlockVisitor() {
    override fun visit(block: kt.osrs.analysis.tree.flow.Block) {
        val node = block.tree().leaf(IFNULL, AALOAD, GETSTATIC)
        if (node != null && (node as FieldMemberNode).desc() == "[Lbo;")
            println("Found npcs @ $node")
    }
}

val charAnimation = object : BlockVisitor() {
    override fun visit(block: kt.osrs.analysis.tree.flow.Block) {
        val node = block.tree().leaf(-1, PUTFIELD)
        if (node == null) return
        val fmn = node as FieldMemberNode
        if (node != null && fmn.owner().equals("c") && fmn.desc().equals("I") && fmn.leafVariable(ILOAD, 3))
            println("Found THJE THING @ $node")
    }
}
val exchangeItemID = object : BlockVisitor() {
    override fun visit(block: kt.osrs.analysis.tree.flow.Block) {
        block.tree().accept(object : NodeVisitor() {
            override fun visitField(fmn: FieldMemberNode) {
                if (fmn.opcode() == PUTFIELD && fmn.owner().equals("c") && fmn.desc().equals("I") && fmn.leafVariable(ILOAD, 3)) { //check against {ExchangeOffer} and type
                    println("Found exchange.itemID $fmn")
                }
            }
        })
    }
}
val exchangeItemPrice = object : BlockVisitor() {
    override fun visit(block: kt.osrs.analysis.tree.flow.Block) {
        block.tree().accept(object : NodeVisitor() {
            override fun visitField(fmn: FieldMemberNode) {
                if (fmn.opcode() == PUTFIELD && fmn.owner().equals("c") && fmn.desc().equals("I") && fmn.leafVariable(ILOAD, 4)) { //check against {ExchangeOffer} and type
                    println("Found exchange.price $fmn")
                }
            }
        })
    }
}
val exchangeItemQuantity = object : BlockVisitor() {
    override fun visit(block: kt.osrs.analysis.tree.flow.Block) {
        block.tree().accept(object : NodeVisitor() {
            override fun visitField(fmn: FieldMemberNode) {
                if (fmn.opcode() == PUTFIELD && fmn.owner().equals("c") && fmn.desc().equals("I") && fmn.leafVariable(ILOAD, 5)) { //check against {ExchangeOffer} and type
                    println("Found exchange.quantity $fmn")
                }
            }
        })
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