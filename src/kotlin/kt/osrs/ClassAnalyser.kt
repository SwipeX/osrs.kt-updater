package kt.osrs

import kt.osrs.analysis.ClassIdentity
import kt.osrs.analysis.model.Identifiable
import kt.osrs.analysis.rank.ClassRanking
import kt.osrs.analysis.rank.build
import kt.osrs.analysis.tree.flow.FlowVisitor
import kt.osrs.analysis.tree.flow.graph.FlowGraph
import kt.osrs.event.Event
import kt.osrs.event.Stopwatch
import org.objectweb.asm.Opcodes
import org.objectweb.asm.commons.util.JarArchive
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.MethodNode
import org.reflections.Reflections
import java.io.File

object ClassAnalyser {
    val deob = "./jars/deob.jar"
    val archive = JarArchive(File(deob))
    val classes: MutableMap<String, ClassNode>? = archive.build()
    val graphs = flowGraphs(classes!!)
    val identifiers: List<Identifiable> = getAllFromReflection()
    var rankings: MutableMap<String, ClassRanking>? = build(classes!!.values)
    val identified = Event<IdentifiedEvent>()
    var debug = true

    fun identify() {
        identified += { (c, n) -> addToMap(c, n) }
        identifiers.map { it.identity }.forEach { id ->
            //remove already identified classes
            var values = classes!!.values.filter { v -> rawClasses.none { it.key == v.name } }
            //must have a static definition to proceed
            val def = id.staticDefinition

            def?.apply {
                //filter on superName
                superName?.apply {
                    values = values.filter { it.superName == interpolate(this) }
                }
                //filter on access
                access?.apply {
                    values = values.filter { (it.access and access!!) == this }
                }

                //filter on interfaces
                if (interfaces.isNotEmpty()) values = values.filter { it.interfaces.containsAll(interfaces) }

                //filter against field counts
                values = values.filter { node ->
                    val fieldCounts = node.fields
                        .filter { f -> (f.access and Opcodes.ACC_STATIC) != Opcodes.ACC_STATIC }
                        .takeIf { count == 0 || count == it.count() }
                        ?.groupBy { it.desc }?.mapValues { it.value.count() }
                    interpolateMap(node).all { fieldCounts?.get(it.key) == it.value }
                }
                //filter for method desc existing in class
                if (methods.isNotEmpty()) {
                    values = values.filter { node ->
                        node.methods.filter { (it.access and Opcodes.ACC_STATIC) != Opcodes.ACC_STATIC }
                            .map { it.desc }.containsAll(interpolateMethods(node))
                    }
                }
            }

            when {
                values.size == 1 -> identified(IdentifiedEvent(id, values.first().name))
                values.size > 1 -> log("*** Found multiple classes for id: ${id.name} will be disabled. Collisions: " + values.map { it.name })
                values.isEmpty() -> log("*** No values were applicable for id: ${id.name}")
            }
        }

        // Identify members.
        val color = "\u001B[36;1m"
        val red = "\u001B[33;1m"
        val reset = "\u001B[0m"

        locatedClasses.forEach { name, identity ->
            MemberAnalyser.identify(identity, identity.foundName)
            log("$name $color[${identity.foundName}]$reset [Index: ${identifiers.firstOrNull { it.identity == identity }?.executeIndex}]")
            identity.members.filter { it.foundName.isNotEmpty() }.forEach {
                log("\t$color|... $reset${it.name} $red[${it.foundOwnerName}.${it.foundName}]$reset")
            }
           //log("...............................")
        }

        log("Identified ${locatedClasses.size} classes [ Current Last Index: ${identifiers.lastOrNull()?.executeIndex}]")
    }

    fun enableMemberAnalysis() {
        //identified += { (c, n) -> MemberAnalyser.identify(c, n) }
    }

    fun log(s: String) {
        if (debug) println(s)
    }

    private fun addToMap(id: ClassIdentity, name: String) {
        locatedClasses[id.name] = id
        rawClasses[name] = id
        id.foundName = name
        //log("${id.name} [$name] [Index: ${identifiers.firstOrNull { it.identity == id }?.executeIndex}]")
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

    data class IdentifiedEvent(val id: ClassIdentity, val foundClass: String)

    private fun getAllFromReflection(): List<Identifiable> {
        val reflections = Reflections("kt.osrs.analysis.model")
        val classes = reflections.getSubTypesOf(Identifiable::class.java).toList()
        return classes.map { it.newInstance() }.sortedBy { it.executeIndex }
    }
}