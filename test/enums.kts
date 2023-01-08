import ce.defs.*

namespace(
    when (target()) {
        ce.defs.Target.Kotlin, ce.defs.Target.Java -> "com.goldman"
        else -> "goldman"
    }
)

setOutputFileName("GoldEnums")

enum("PhoneState").apply {
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

enum("Seasons").apply {
    add("winter")
    add("summer")
    add("spring")
    add("autumn")
}