package kt.osrs

import kt.osrs.analysis.tree.dsl.NodeSequence


/**
 * Created by IntelliJ IDEA.
 * User: Benti
 * Date: 10. 01. 2019
 * Time: 22:27
 */

fun main(args: Array<String>) {
    NodeSequence {
        vn()
    }.apply {
        tree?.apply {
            println(opcode)
        }
    }
}