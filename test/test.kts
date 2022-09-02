import ce.defs.*

namespace("goldman")

arrayOf(
  enum("GoldErrors2").apply {
    addClassComment("""
      Long line class comment.
      Line 2
    """.trimIndent())
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