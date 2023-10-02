package ce.entrypoints

import ce.defs.DataType

fun main(args: Array<String>) {
    buildConstants()
}

fun buildConstants() {
    println("namespace(\"com.goldman.dt1\").apply {")
    val nameA =
        listOf("Grumpy", "Silly", "Wild", "Red", "Brown", "Tasty", "Wise", "Windy", "Cloudy", "Noble", "Angry", "Crazy")
    val nameB = listOf("Cat", "Dog", "Fish", "Cake", "Door", "Car", "Lion", "Panther", "Planet", "Sun")
    val dataTypes = listOf(
        Pair("int8", -23),
        Pair("int16", -32),
        Pair("int32", -63),
        Pair("int64", -128),
        Pair("uint8", 23),
        Pair("uint16", 32),
        Pair("uint32", 128),
        Pair("uint64", 243),
        Pair("float32", 23.45f),
        Pair("float64", 23.91),
        Pair("float128", 23.751)
    )
    nameB.forEachIndexed { index, mainName ->
        nameA.forEachIndexed { index2, secondName ->
            val idx = index + index2
            val count = idx % nameA.size + 1
            val blockName = "$secondName$mainName"
            println("\tconstantsBlock(\"$blockName\").apply {")
            println("\t\taddBlockComment(\"$blockName constants definition block\")")
            val type = dataTypes[idx % dataTypes.size]
            println("\t\tdefaultType(DataType.${type.first})")
            for (i in 0..count - 1) {
                print("\t\t")
                println("add(\"${nameA[i]}\", ${type.second})")
            }
            println("\t}\n")
        }
    }
    println("}")
}
