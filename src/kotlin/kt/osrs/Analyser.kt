package kt.osrs

import kt.osrs.ClassAnalyser.classes
import kt.osrs.ClassAnalyser.graphs

fun main(args: Array<String>) {
    //this pairs the event to the memberAnalyser
    ClassAnalyser.enableMemberAnalysis()
    //this kicks off the process
    ClassAnalyser.identify()

    val thing = graphs[classes!!["client"]]!![classes!!["client"]!!.getMethodByName("init")]?.asSequence()?.forEach {
        val tree = it.tree()
      //  println(tree)
    }

}






