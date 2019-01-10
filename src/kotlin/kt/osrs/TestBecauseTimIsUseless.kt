package kt.osrs

import kt.osrs.analysis.tree.dsl.NodeSequence
import kt.osrs.analysis.tree.dsl.TreeNode


/**
 * Created by IntelliJ IDEA.
 * User: Benti
 * Date: 10. 01. 2019
 * Time: 22:27
 */

fun main(args: Array<String>) {

    NodeSequence {
        vn {
            fmn() and vn() // TODO FIX AND (CURRENTLY GOES FOR INFIX THIS WHICH IS THE ONE INFRONT [FMN]
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