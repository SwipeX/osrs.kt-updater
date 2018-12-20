package kt.osrs.analysis.model

import kt.osrs.analysis.ClassIdentity

abstract class Identifiable {
    abstract val identity: ClassIdentity
    abstract val executeIndex: Int
}