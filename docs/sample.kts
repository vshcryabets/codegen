import ce.defs.*
import  generators.obj.input.*

namespace("com.goldman.data").apply {
   declareInterface("DataReader").apply {
       addMethod(
           "getSize",
           OutputList().apply {
               output("size", DataType.int64)
           }, null
       )
       addMethod(
           "read",
           outputs = OutputList().apply {
               output("size", DataType.int64)
               outputReusable("array", DataType.array(DataType.uint8))
           },
           inputs = InputList().apply {
               argument("offset", DataType.int64)
               argument("size", DataType.int32)
           }
       )
   }


   dataClass("GoldBuffer").apply {
      field("blockCount", DataType.int32)
      field("lastBlockSize", DataType.int32, 10)
      field("blockSize", DataType.int32)
   }
}
