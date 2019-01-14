package kt.osrs

fun main(args: Array<String>) {
    //this pairs the event to the memberAnalyser
    ClassAnalyser.enableMemberAnalysis()
    //this kicks off the process
    ClassAnalyser.identify()
}






