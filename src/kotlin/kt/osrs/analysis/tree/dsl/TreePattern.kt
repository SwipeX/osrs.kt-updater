package kt.osrs.analysis.tree.dsl

class TreePattern(init: TreePattern.() -> Unit) {
    //list of opcodes going down the tree
    val opcodes: MutableList<Int> = mutableListOf()
    //-1 will indicate the last index, anything else will
    var opcodeIndex = -1
    //for leaf element this will be opcode/var
    var leafElement: Pair<Int, Int> = Pair(-1, -1)

    init {
        apply(init)
    }

    fun opcodes(vararg opcodes: Int) = this.opcodes.addAll(opcodes.toTypedArray())
}