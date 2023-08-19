package ce.defs

object TargetExt {
    fun findByName(name: String): Target =
        Target.values().find {it.name.equals(name, true) } ?: Target.Other
}