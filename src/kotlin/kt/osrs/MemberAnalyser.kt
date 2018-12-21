package kt.osrs

import kt.osrs.ClassAnalyser.classes
import kt.osrs.ClassAnalyser.log
import kt.osrs.ClassAnalyser.rankings
import kt.osrs.analysis.ClassIdentity
import kt.osrs.analysis.rank.usage.*
import org.objectweb.asm.tree.FieldInsnNode
import org.objectweb.asm.tree.FieldNode

object MemberAnalyser {
    fun identify(id: ClassIdentity, name: String) {
        // Searching for members
        if (id.members.isNotEmpty()) {
            //TODO instruction kt.osrs.analysis.tree search
            id.members.forEach { memberIdentity ->
                memberIdentity.usageDefinition?.apply {
                    var mappings: MutableMap<FieldNode, MutableList<MemberUsage<FieldInsnNode>>> = rankings!![name]?.fieldRankings!!

                    usages.filter { usage ->
                        val clazz = interpolate(usage.clazz)
                        val descriptor = interpolate(usage.desc)
                        val opcodes = usage.opcodes
                        val static = clazz == "Any" || opcodes.any { it.opcode == GETSTATIC || it.opcode == PUTSTATIC }
                        if (static) {
                            mappings = rankings!!.values.map { it.fieldRankings }.reduce { map, acc -> acc.apply { acc.putAll(map) } }
                        }
                        var usageMatch = false

                        for ((fn, usageList) in mappings) {
                            usageMatch = usageList.filter { static || it.caller.owner.name == clazz }
                                .filter { descriptor.isEmpty() || it.caller.desc == descriptor }
                                .let { list ->
                                    val groupings = list.groupBy { it.member.opcode() }
                                    // if (fn.handle.toString().contains("bc.be")) {
                                    //     println("GROUPINGS FOR ${fn.handle}")
                                    //     println("\t\t----------------------------------------")
                                    //     println("\t\t$groupings")
                                    //     println("\t\tUsages: $usages")
                                    //     println("\t\t----------------------------------------")
                                    // }
                                    opcodes.all { groupings.containsKey(it.opcode) && equality(it.occurs, it.equality, groupings[it.opcode]?.size!!) }
                                }
                            if (usageMatch) {
                                usage.foundName = fn.name
                                usage.foundOwnerName = fn.owner.name
                                break
                            }
                        }
                        usageMatch
                    }.groupBy { "${it.foundOwnerName}.${it.foundName}" }.apply {
                        if (size == 1) {
                            forEach { name, usageList ->
                                if (usageList.containsAll(usages)) {
                                    memberIdentity.apply {
                                        name.split(".").apply {
                                            foundName = this[1]
                                            foundOwnerName = this[0]
                                        }

                                    }

                                }
                            }
                        } else {
                            if (isEmpty()) {
                                log("*** Nothing found for $memberIdentity")
                            } else {
                                log("*** Multiple occurrences found for $memberIdentity [$this]")
                            }
                        }
                    }

                    /*
                    val clazz = interpolate(clazz)
                    val descriptor = interpolate(methodDescriptor)
                    val opcodes = opcodes
                    val static = clazz == "Any" || opcodes.any { it.opcode == GETSTATIC || it.opcode == PUTSTATIC }
                    //decide if we want ALL fields or just fields in this clazz
                    variable mappings: MutableMap<FieldNode, MutableList<MemberUsage<FieldInsnNode>>> = rankings!![name]?.fieldRankings!!
                    if (static) {
                        mappings = rankings!!.values.map { it.fieldRankings }
                            .reduce { map, acc -> acc.apply { acc.putAll(map) } }
                    }
                    //loop through each one and see if it matches critria
                    mappings.filterKeys { it.desc == memberIdentity.desc }.forEach { fn, usage ->
                        usage.filter { static || it.caller.owner.name == clazz }
                            .filter { descriptor.isEmpty() || it.caller.desc == descriptor }.apply {
                                if (opcodes.isEmpty()) {
                                    //println("EMPTY OPCODES FOR $memberIdentity")
                                } else {
                                    val groupings = groupBy { it.member.opcode }
                                    if (opcodes.all { groupings.containsKey(it.opcode) && equality(it.occurs, it.equality, groupings[it.opcode]?.count()!!) }) {
                                        memberIdentity.apply {
                                            foundName = fn.name
                                            foundOwnerName = fn.owner.name
                                            return@forEach
                                        }
                                    }
                                }
                            }
                    }
                    */
                } ?: classes?.get(name)?.apply {
                    //DESC ONLY SEARCH -- AKA LAME
                    val matches = fields.filter { f -> f.desc == interpolate(memberIdentity.desc!!) }
                    if (matches.count() == 1) {
                        memberIdentity.foundName = matches.first().name
                        memberIdentity.foundOwnerName = name
                        return@apply
                    }
                }
            }
        }
    }

    fun match(node: FieldNode, usage: Usage, usageList: MutableList<MemberUsage<FieldInsnNode>>) = usage.let {

    }


    fun equality(count: Int, equality: Equality, counted: Int) = count == 0 || when (equality) {
        Equality.EXACT -> count == counted
        Equality.MIN -> counted >= count
        Equality.MAX -> counted <= count
    }
}
