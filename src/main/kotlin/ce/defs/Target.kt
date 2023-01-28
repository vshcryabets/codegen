package ce.defs

enum class Target(val strName: String) {
    Other("other"),
    Kotlin("kotlin"),
    C("c"),
    Cxx("cxx"),
    Swift("swift"),
    Java("java"),
    Rust("rust"),
    Go("go"),
    Python("python"), ;

    companion object {
        fun findByName(name: String): Target =
            Target.values().find {it.name.equals(name, true) } ?: Other
    }
}