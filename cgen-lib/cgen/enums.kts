import ce.defs.*

namespace("ce.defs").apply {

    enum("Target").apply {
        defaultType(DataType.string())
        add("Other", "other")
        add("Kotlin", "kotlin")
        add("C", "c")
        add("Cxx", "cxx")
        add("Swift", "swift")
        add("Java", "java")
        add("Rust", "rust")
        add("Go", "go")
        add("Python", "python")
        add("Meta", "meta")
    }
}
