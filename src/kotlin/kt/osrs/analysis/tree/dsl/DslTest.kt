package kt.osrs.analysis.tree.dsl

class DslTest {
    fun ye() {
        NodeSequence {
            fmn() + vn {
                this[fmn(), vn()]
            } + vn() - vn {
                fmn()
            }
        }
    }
}