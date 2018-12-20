package kt.osrs.analysis

class ClassIdentity : Identity() {

    var staticDefinition: StaticDefinition? = null
    var members: MutableList<MemberIdentity> = mutableListOf()
    fun memberIdentity(init: MemberIdentity.() -> Unit) = members.add(MemberIdentity(init))
    fun staticDefinition(init: StaticDefinition.() -> Unit) = StaticDefinition(init).apply {
        staticDefinition = this
    }
}

fun classIdentity(init: ClassIdentity.() -> Unit) = ClassIdentity().apply(init)
