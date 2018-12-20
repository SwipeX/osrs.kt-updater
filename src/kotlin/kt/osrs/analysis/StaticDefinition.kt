package kt.osrs.analysis

import kt.osrs.interpolate
import org.objectweb.asm.tree.ClassNode

class StaticDefinition(init: StaticDefinition.() -> Unit) {
    var count = 0
    val map: MutableMap<String, Int> = mutableMapOf()
    val methods: MutableList<String> = mutableListOf()
    var access: Int? = null
    var superName: String? = null
    var interfaces: MutableList<String> = mutableListOf()

    init {
        apply(init)
    }

    fun hasMethod(desc: String) = methods.add(desc)

    infix fun String.occurs(i: Int) = map.put(this, i)

    fun interpolateMap(node: ClassNode) = map.mapKeys { interpolate(it.key.replace("{self}", node.name)) }
    fun interpolateMethods(node: ClassNode) = methods.map { interpolate(it.replace("{self}", node.name)) }
}