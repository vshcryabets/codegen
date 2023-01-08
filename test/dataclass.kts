import ce.defs.*
import generators.obj.input.DataField

namespace("com.goldman.data").apply {
    dataClass("GoldBuffer").apply {
        // add fields using DataClass::field method
        field("blockCount", DataType.int32)
        field("lastBlockSize", DataType.int32, 10)
        // using direct method
        addSub(DataField("blockSize", type = DataType.int32))
    }
}
