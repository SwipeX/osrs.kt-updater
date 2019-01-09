package kt.osrs.idk_what_to_call_it

import kt.osrs.analysis.tree.node.AbstractNode
import org.objectweb.asm.Opcodes

/**
 * Created by IntelliJ IDEA.
 * User: Mihael Bercic
 * Date: 7. 01. 2019
 * Time: 21:28
 */
// RENAME
class CustomOpcode(val opcode: Int, val variable: String) {

    fun equals(an:AbstractNode) = an.opcode() == opcode

}