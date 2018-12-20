package kt.osrs.analysis.tree.util

/**
 * @author Tyler Sedlar
 */
class TreeSize(val collapsing: Int, val producing: Int) {

    override fun toString(): String {
        return "[$collapsing][$producing]"
    }
}