import ce.defs.*
import generators.obj.input.*

namespace("sample")

constantsBlock("ModeType").apply {
    addBlockComment("File mode types")
    defaultType(DataType.int32)
    add("OREAD", 0)
    add("OWRITE", 1)
    add("ORDWR", 2)
    add("OEXEC", 3)
    add("OTRUNC", 0x10)
}
