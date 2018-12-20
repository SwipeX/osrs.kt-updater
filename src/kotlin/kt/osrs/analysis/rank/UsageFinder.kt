package kt.osrs.analysis.rank

import kt.osrs.ClassAnalyser
import kt.osrs.analysis.rank.usage.opcName
import kt.osrs.extrapolate
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.FieldNode
import java.util.*

val descRegex = "^.*(\\(.*?)\$".toRegex()

val color = "\u001B[36;1m"
val red = "\u001B[33;1m"
val blue = "\u001B[34;1m"
val magenta = "\u001B[31;1m"
val reset = "\u001B[0m"

fun main(args: Array<String>) {
    ClassAnalyser.debug = false
    ClassAnalyser.identify()
    val scanner = Scanner(System.`in`)
    print("What are we looking for? ")
    val field = scanner.nextLine()
    val dotSplit = field.split(".")
    val isClass = dotSplit.size == 1

    if (isClass) {
        val vs = field.split("vs").map { it.trim() }.map { findClassInfo(ClassAnalyser.classes!![it]!!) }
        StringBuilder().apply {
            vs.map { padString("Name: $blue${extrapolate(it.name)}$reset", blue, reset) }.forEach { append("|$it|\t\t") }
            append("\n")
            vs.map { padString("SuperName: $magenta${extrapolate(it.superName)}$reset", magenta, reset) }.forEach { append("|$it|\t\t") }
            append("\n")
            val max = vs.map { it.classFields }.map { it.size }.max() ?: -1
            val fieldMappings = vs.map { it.classFields.map { cf -> cf.key to cf.value.size } }
            for (i in 0 until max) {
                fieldMappings.forEach { fields ->
                    fields.getOrNull(i)?.apply {
                        append("|${padString("$red\"${extrapolate(first)}\"$reset occurs $blue$second$reset", blue, reset, red, reset)}|\t\t")
                    }
                }
                append("\n")
            }

            val maxMethods = vs.map { it.classMethods }.map { it.size }.max() ?: -1
            val methodMappings = vs.map { it.classMethods }
            for (i in 0 until maxMethods) {
                methodMappings.forEach { methods ->
                    methods.getOrNull(i)?.apply {
                        append("|${padString("${blue}hasMethod(\"${extrapolate(this)}\")$reset", blue, reset)}|\t\t")
                    }
                }
                append("\n")
            }
            println(this)
        }

    } else {
        dotSplit.forEach { matches ->
            val owner = dotSplit[0]
            val name = dotSplit[1]
            val isMethod = name.contains("(")
            ClassAnalyser.rankings?.filter { it.key == owner }?.values?.forEach {
                it.apply {
                    println("Searching for owning class: ${node.name}")
                    if (isMethod) {
                        val descriptor = descRegex.find(name)?.groupValues?.get(1)
                        descriptor?.apply {
                            methodRankings.filterKeys { it.name == name.split("(")[0] && descriptor == it.desc }.values.flatten().groupBy { it.caller }.forEach { t, u ->
                                println("${t.handle} calls \u001b[36;1m$owner.$name\u001B[1m x \u001B[1;31m${u.count()}\u001B[0m")
                            }
                        }
                    } else if (!isClass) {
                        fieldRankings.filterKeys { it.name == name }.values.flatten().groupBy { it.caller }.forEach { mn, list ->
                            list.groupBy { it.caller }
                                .values
                                .flatten()
                                .groupBy { it.member.opcode() }
                                .map { mapEntry ->
                                    mapEntry.value
                                        .map { "${extrapolate(mn.owner.name)}.${mn.name}${extrapolate(mn.desc)} ... $color${opcName(mapEntry.key)} $reset x $color${mapEntry.value.size}$reset @ [${mapEntry.value[0].member.owner}.${mapEntry.value[0].member.name}]" }
                                        .distinct()
                                        .joinToString()
                                }.forEach(System.out::println)
                        }
                    }
                }
            }


        }
    }
}

fun findClassInfo(node: ClassNode) = ClassInfo(node.name, extrapolate(node.superName)).apply {
    node.apply {
        classFields.putAll(fields.filter { it.access and Opcodes.ACC_STATIC == 0 }.groupBy { it.desc })
        classMethods.addAll(methods.filter { it.access and Opcodes.ACC_STATIC == 0 }.map { extrapolate(it.handle.toString()) })
    }
}

fun padString(s: String, vararg colorsUsed: String) = s.padStart(5 + s.length).padEnd(40 + colorsUsed.map { it.length }.sum())

data class ClassInfo(var name: String, var superName: String, val classFields: MutableMap<String, List<FieldNode>> = mutableMapOf(), val classMethods: MutableList<String> = mutableListOf())