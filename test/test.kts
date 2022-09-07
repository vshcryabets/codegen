import ce.defs.*

namespace("goldman")

namespace(
    when (target()) {
        Target.Kotlin, Target.Java -> {
            "com.goldman"
        }
        else -> {
            setOutputFileName("test")
            "goldman"
        }
    })

arrayOf(
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
    },
    enum("GoldErrors").apply {
        defaultType(DataType.int16)
        add("OK", 0)
        add("BUSY")
        add("AUTHERR")
        add("PASSLEN")
        add("PASSWRONG", 4)
    }
)