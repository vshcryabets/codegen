import ce.defs.*

namespace("com.goldman.data").apply {
    dataClass("GoldBuffer").apply {
        field("blockSize", DataType.int32)
        field("blockCount", DataType.int32)
        field("lastBlockSize", DataType.int32, 10)
    }
}
