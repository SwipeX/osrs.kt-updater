package kt.osrs.analysis

abstract class Identity {
    var name = ""
    var foundName = ""

    override fun toString() = "[Identity] $name -> $foundName"
}