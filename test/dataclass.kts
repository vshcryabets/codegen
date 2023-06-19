import ce.defs.*
import generators.obj.input.DataField

namespace("com.goldman.data").apply {
    val currency = enum("CryptoCurrency").apply {
        add("BTC")
        add("ETH")
        add("BCH")
    }

    dataClass("Money").apply {
        field("sum", DataType.int32)
        field("currency", DataType.custom(currency))
        field("name", DataType.string(canBeNull = true))
    }

    dataClass("GoldBuffer").apply {
        // add fields using DataClass::field method
        field("blockCount", DataType.int32)
        field("lastBlockSize", DataType.int32, 10)
        // using direct method
        addSub(DataField("blockSize", type = DataType.int32))
    }
}
