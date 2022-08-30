import ce.defs.*

namespace("goldman")
enum("GoldErrors").apply {
  defaultType(DataType.int16)
  add("OK", 0)
  add("BUSY")
  add("AUTHERR")
  add("PASSLEN")
  add("PASSWRONG", 4)
}
