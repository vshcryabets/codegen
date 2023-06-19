import ce.defs.*

namespace(
    when (target()) {
        ce.defs.Target.Kotlin, ce.defs.Target.Java -> {
            "com.goldman"
        }
        else -> {
            "goldman"
        }
    }).apply {

    enum("GoldErrors").apply {
        defaultType(DataType.int16)
        add("OK", 0)
        add("BUSY")
        add("AUTHERR")
        add("PASSLEN")
        add("PASSWRONG", 4)
    }
}