package kt.osrs

import kt.osrs.analysis.tree.NodeVisitor
import kt.osrs.analysis.tree.node.FieldMemberNode
import org.objectweb.asm.commons.util.JarArchive
import org.objectweb.asm.tree.ClassNode
import java.io.File
import java.util.*

val color = "\u001B[36;1m"
val red = "\u001B[33;1m"
val blue = "\u001B[34;1m"
val magenta = "\u001B[31;1m"
val reset = "\u001B[0m"

fun main(args: Array<String>) {
    val scanner = Scanner(System.`in`)
    print("What are we looking for? ")
    val field = scanner.nextLine()
    val dotSplit = field.split(".")
    val owner = dotSplit[0]
    val name = dotSplit[1]

    val deob = "./jars/deob.jar"
    val archive = JarArchive(File(deob))
    val classes: MutableMap<String, ClassNode>? = archive.build()
    val graphs = flowGraphs(classes!!)
    classes.values.forEach {
        val graphz = graphs[it]
        it.methods.forEach {
            val graph = graphz!![it]
            graph?.forEach {
              it.tree().accept(object: NodeVisitor() {
                  override fun visitField(fmn: FieldMemberNode) {
                      if(fmn.owner() == owner && fmn.name() == name)
                          println("Location for $red$owner.$name$reset found in $blue${it.owner?.handle}$reset \n ${it.tree()}")
                  }
              })
            }
        }
    }
}