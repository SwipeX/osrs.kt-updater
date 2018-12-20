package kt.osrs.analysis.rank

import kt.osrs.event.Stopwatch
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.FieldInsnNode
import org.objectweb.asm.tree.MethodInsnNode

fun build(classes: Iterable<ClassNode>): MutableMap<String, ClassRanking> {
    val rankMap = classes.map { it.name!! to ClassRanking(it) }.toMap()
    println("Rankings generated in: ${Stopwatch.elapse {
        classes.flatMap { it.methods }.forEach { mn ->
            mn.accept(object : MethodVisitor() {
                override fun visitFieldInsn(fin: FieldInsnNode) {
                    val clazz = classes.firstOrNull { it.name == fin.owner } ?: return
                    val owner = owner(classes, clazz, fin)
                    rankMap[owner.name]?.apply {
                        add(classes, mn, fin)
                    }
                }

                override fun visitMethodInsn(min: MethodInsnNode) {
                    val clazz = classes.firstOrNull { it.name == min.owner } ?: return
                    val owner = owner(classes, clazz, min)
                    rankMap[owner.name]?.apply {
                        add(classes, mn, min)
                    }
                }
            })
        }
    }}ms")
    return rankMap as MutableMap<String, ClassRanking>
}

/**
 * Find the ClassNode that owns a specific field.
 */
internal fun owner(classes: Iterable<ClassNode>, clazz: ClassNode, fin: FieldInsnNode): ClassNode {
    clazz.apply {
        if (getField(fin.name, fin.desc, false) == null) {
            val superClazz = classes.firstOrNull { it.name == superName }
            if (superClazz != null) return owner(classes, superClazz, fin)
        } else {
            return this
        }
    }
    return clazz
}

internal fun owner(classes: Iterable<ClassNode>, clazz: ClassNode, min: MethodInsnNode): ClassNode {
    clazz.apply {
        if (getField(min.name, min.desc, false) == null) {
            val superClazz = classes.firstOrNull { it.name == superName }
            if (superClazz != null) return owner(classes, superClazz, min)
        } else {
            return this
        }
    }
    return clazz
}