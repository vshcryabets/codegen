package ce.defs

enum class Target(val rawValue : String) {
    Other("other"),
    Kotlin("kotlin"),
    C("c"),
    Cxx("cxx"),
    Swift("swift"),
    Java("java"),
    Rust("rust"),
    Go("go"),
    Python("python"),
}
