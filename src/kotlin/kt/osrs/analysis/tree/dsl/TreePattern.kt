package kt.osrs.analysis.tree.dsl

class TreePattern(init: TreePattern.() -> Unit) {
    var opcodes: Array<Int> = emptyArray()
    init {
        apply(init)
    }
}