package kt.osrs

import kt.osrs.analysis.tree.NodeTree
import kt.osrs.analysis.tree.util.TreeBuilder
import org.objectweb.asm.commons.util.JarArchive
import org.objectweb.asm.tree.ClassNode
import java.io.File

fun main(args: Array<String>) {
    val deob = "./jars/deob.jar"
    val archive = JarArchive(File(deob))
    val classes: MutableMap<String, ClassNode>? = archive.build()
    val method = classes!!["es"]?.getMethod("ig", "(Lbk;III)V")!!
    var tree: NodeTree = TreeBuilder.build(method)
    println(tree)
}