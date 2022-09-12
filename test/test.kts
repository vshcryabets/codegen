import ce.defs.*

namespace(
    when (target()) {
        ce.defs.Target.Kotlin, ce.defs.Target.Java -> {
            "com.goldman"
        }
        else -> {
            "goldman"
        }
    })

enum("GoldErrors2").apply {
    addBlockComment(
        """
          Long line class comment.
          Line 2
        """.trimIndent()
    )
    defaultType(DataType.int16)
    add("OK", 0)
    add("BUSY")
}

enum("GoldErrors").apply {
    defaultType(DataType.int16)
    add("OK", 0)
    add("BUSY")
    add("AUTHERR")
    add("PASSLEN")
    add("PASSWRONG", 4)
}

setOutputFileName("GoldObjects")

enum("GoldErrors3").apply {
    addBlockComment(
        """
          Long line class comment.
          Line 2
        """.trimIndent()
    )
    defaultType(DataType.int16)
    add("OK", 0)
    add("BUSY")
}

enum("GoldErrors4").apply {
    defaultType(DataType.int16)
    add("OK", 0)
    add("BUSY")
    add("AUTHERR")
    add("PASSLEN")
    add("PASSWRONG", 4)
}


