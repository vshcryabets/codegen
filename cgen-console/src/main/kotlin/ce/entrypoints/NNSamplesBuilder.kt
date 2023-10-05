package ce.entrypoints

import java.io.File

fun main(args: Array<String>) {
    buildConstants(args[0])
}

fun buildConstants(basePath: String) {


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
//        Pair("float128", 23.751)
    )
    nameB.forEachIndexed { index, mainName ->
        nameA.forEachIndexed { index2, secondName ->
            val blockName = "$secondName$mainName"
            val filename = "$blockName.kts"
            val writter = File(basePath + File.separator + filename).printWriter()
            println("\"constants/$filename\",")
            writter.println("import ce.defs.*")
            writter.println("import generators.obj.input.*")
            writter.println("""
                when (target()) {
                    ce.defs.Target.Kotlin -> setOutputBasePath("../kotlin/")
                    ce.defs.Target.Cxx -> setOutputBasePath("../cxx/")
                    else -> {}
                }
            """.trimIndent())
            writter.println("namespace(\"com.goldman.dt1\"). apply {")
            val idx = index + index2
            val count = idx % nameA.size + 1
            writter.println("\tconstantsBlock(\"$blockName\").apply {")
            writter.println("\t\taddBlockComment(\"$blockName constants definition block\")")
            val type = dataTypes[idx % dataTypes.size]
            writter.println("\t\tdefaultType(DataType.${type.first})")
            for (i in 0..count - 1) {
                writter.print("\t\t")
                writter.println("add(\"${nameA[i]}\", ${type.second})")
            }
            writter.println("\t}\n")
            writter.println("}")
            writter.close()
        }
    }
}
