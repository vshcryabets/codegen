import ce.defs.*

namespace(
    when (target()) {
        ce.defs.Target.Kotlin, ce.defs.Target.Java -> "com.goldman"
        else -> "goldman"
    }
)

constantsBlock("GoldConstants").apply {
    addBlockComment("Constants definition block")
    defaultType(DataType.float64)
    add("MinCoefficient", 1.0)
    add("MaxCoefficient", 50.0)
    add("DefaultCoefficient", 14.0)
}