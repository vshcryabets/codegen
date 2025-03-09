import ce.defs.*
import generators.obj.input.DataField

namespace("com.goldman").apply {
    getNamespace("data").apply {
        val currency = enum("CryptoCurrency").apply {
            add("BTC")
            add("ETH")
            add("BCH")
        }

        val money = dataClass("Money").apply {
            field("sum", DataType.int32)
            field("currency", DataType.custom(currency))
            field("name", DataType.string(canBeNull = true))
            field("attached", DataType.bool, false)
        }
        money.addstaticfield("EMPTY", DataType.custom(money), money.instance())

        dataClass("GoldBuffer").apply {
            // add fields using DataClass::field method
            field("blockCount", DataType.int32)
            field("lastBlockSize", DataType.int32, 10)
            // using direct method
            field("blockSize", DataType.int32)
        }
    }
}
