package kt.osrs

import kt.osrs.analysis.tree.dsl.NodeSequence
import kt.osrs.analysis.tree.dsl.TreeNode
import org.objectweb.asm.Opcodes


/**
 * Created by IntelliJ IDEA.
 * User: Benti
 * Date: 10. 01. 2019
 * Time: 22:27
 */

fun main(args: Array<String>) {

    NodeSequence {
        jn {
            fmn {
                vn(Opcodes.ALOAD, 0)
            } and vn(Opcodes.ILOAD, 3)
        }
    }.apply {
        tree?.apply {
            println(type)
            printChildren(this)
        }
    }
}

fun printChildren(node: TreeNode, tab: Int = 0) {
    getChildren(node).forEach {
        var space = ""
        for (a in 0..tab) space = space.plus("\t")
        println(space + "| -> ${it.type}")
        printChildren(it, tab + 1)
    }
}

fun test(v: () -> Array<Int>) {

}

fun getChildren(node: TreeNode) = node.children