package kt.osrs.analysis.tree.flow

interface BlockHandler {

    fun handle(followIndex: Int, block: kt.osrs.analysis.tree.flow.Block): Boolean
}