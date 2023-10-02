package ce.entrypoints

fun main(args: Array<String>) {
    buildConstants()
}

fun buildConstants() {
    println("namespace(\"com.goldman.dt1\").apply {")
    val nameA = listOf("Grumpy", "Silly", "Wild", "Red", "Brown", "Tasty", "Wise", "Windy", "Cloudy", "Noble", "Angry", "Crazy")
    val nameB = listOf("Cat", "Dog", "Fish", "Cake", "Door", "Car", "Lion", "Panther", "Planet", "Sun")

    nameB.forEachIndexed { index, mainName ->
        nameA.forEachIndexed { index2, secondName ->
            val count = (index + index2) % nameA.size + 1
            val blockName = "$secondName$mainName"
            println("\tconstantsBlock(\"$blockName\").apply {")
            println("\t\taddBlockComment(\"$blockName constants definition block\")")
            for (i in 0..count - 1) {

            }
            println("\t}\n")
        }
    }
    println("}")
}
