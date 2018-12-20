package kt.osrs.analysis.rank

import kt.osrs.analysis.rank.usage.MemberUsage
import kt.osrs.interpolate
import org.objectweb.asm.tree.*

class ClassRanking(val node: ClassNode) {
    val fieldRankings: MutableMap<FieldNode, MutableList<MemberUsage<FieldInsnNode>>> = mutableMapOf()
    val methodRankings: MutableMap<MethodNode, MutableList<MemberUsage<MethodInsnNode>>> = mutableMapOf()

    fun add(classes: Iterable<ClassNode>, mn: MethodNode, fin: FieldInsnNode) {
        val clazz = classes.firstOrNull { it.name == fin.owner }
        val owner = kt.osrs.analysis.rank.owner(classes, clazz!!, fin)
        val fn = owner.getField(fin.name,fin.desc)
        val ranking = if (fieldRankings.containsKey(fn)) fieldRankings[fn] else mutableListOf()
        ranking?.add(MemberUsage(mn, fin))
        fn?.let {
            ranking?.let { r ->
                fieldRankings.put(it, r)
            }
        }
    }

    fun add(classes: Iterable<ClassNode>, mn: MethodNode, min: MethodInsnNode) {
        val node = classes.firstOrNull { it.name == min.owner }?.getMethod(min.name, min.desc)
        val ranking = if (methodRankings.containsKey(node)) methodRankings[node] else mutableListOf()
        ranking?.add(MemberUsage(mn, min))
        node?.let {
            ranking?.let { r ->
                methodRankings.put(it, r)
            }
        }
    }

    fun usages(fn: FieldNode, opcode: Int) = fieldRankings[fn]?.filter { it.member.opcode() == opcode }
    fun usages(fn: FieldNode, name: String, opcode: Int) = fieldRankings[fn]?.filter { it.caller.owner.name == interpolate(name) && it.member.opcode() == opcode }
    fun usages(fn: FieldNode, opcode: Int, methodDesc: String) = fieldRankings[fn]?.filter { it.caller.desc == interpolate(methodDesc) && it.member.opcode() == opcode }

    fun usages(mn: MethodNode, opcode: Int) = methodRankings[mn]?.filter { it.member.opcode() == opcode }
    fun usages(mn: MethodNode, name: String, opcode: Int) = methodRankings[mn]?.filter { it.caller.owner.name == interpolate(name) && it.member.opcode() == opcode }
    fun usages(mn: MethodNode, opcode: Int, methodDesc: String) = methodRankings[mn]?.filter { it.caller.desc == interpolate(methodDesc) && it.member.opcode() == opcode }
}

