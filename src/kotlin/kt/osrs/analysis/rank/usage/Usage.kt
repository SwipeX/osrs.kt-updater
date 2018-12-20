package kt.osrs.analysis.rank.usage

/**
 * Created by IntelliJ IDEA.
 * User: Mihael Bercic
 * Date: 17. 12. 2018
 * Time: 13:16
 */

class Usage(var clazz: String, var desc: String, val opcodes: MutableList<Opcode> = mutableListOf()) {
    var foundName = ""
    var foundOwnerName = ""
    override fun toString() = "In $clazz: $foundOwnerName.$foundName$desc => $opcodes"
}