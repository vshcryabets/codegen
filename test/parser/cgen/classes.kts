import ce.defs.*
import generators.obj.input.*

namespace("enums").apply {

    when (target()) {
        ce.defs.Target.Kotlin -> setOutputBasePath("../kotlin/")
        ce.defs.Target.Cxx -> setOutputBasePath("../cpp/")
        else -> {}
    }

    val currency = enum("CryptoCurrency").apply {
        add("BTC")
        add("ETH")
        add("BCH")
    }

    constantsBlock("ModeType").apply {
        addBlockComment("File mode types")
        defaultType(DataType.int32)
        add("OREAD", 0)
        add("OWRITE", 1)
        add("ORDWR", 2)
        add("OEXEC", 3)
        add("OTRUNC", 0x10)
    }

    dataClass("Money").apply {
        field("sum", DataType.int32)
        field("currency", DataType.custom(currency))
        field("name", DataType.string(canBeNull = true))
        field("attached", DataType.bool, false)
    }
}