package kt.osrs

import kt.osrs.analysis.ClassIdentity

val rawClasses: MutableMap<String, ClassIdentity> = mutableMapOf()
val locatedClasses: MutableMap<String, ClassIdentity> = mutableMapOf()
private val pattern = "\\{(.*?)}".toRegex()
private val objectPattern = "L(.*?);".toRegex()

fun extrapolate(s: String): String {
    var ex = s
    if (ex.contains(";")) {
        objectPattern.findAll(ex).forEach {
            var innerText = it.groupValues[0]
            innerText = innerText.substring(1, innerText.length - 1)
            if (rawClasses.containsKey(innerText)) {
                ex = ex.replace(it.groupValues[0], "L{${rawClasses[innerText]?.name}};")
            }
        }
        return ex
    }
    return if (rawClasses.containsKey(ex)) "{${rawClasses[ex]?.name}}" else ex
}

fun interpolate(s: String): String {
    val sb = StringBuilder()
    pattern.findAll(s).forEach {
        var innerText = it.groupValues[0]
        innerText = innerText.substring(1, innerText.length - 1)
        if (locatedClasses.containsKey(innerText)) {
            sb.append(s.substring(0, it.range.start))
                .append(locatedClasses[innerText]?.foundName)
                .append(s.substring(it.range.last + 1))
        }
    }
    return if (sb.isBlank()) s else sb.toString()
}
