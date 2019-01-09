package kt.osrs.analysis.tree.dsl

class DslTest {
    fun ye() {
        NodeSequence {
            fmn("{Player}") + vn {
                this[fmn(), vn()]
            } + vn() - vn {
                fmn()
            }
        }
    }
}