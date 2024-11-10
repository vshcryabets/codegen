import ce.defs.*

namespace("ce.defs").apply {

    val targetEnum = enum("Target").apply {
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

    dataClass("TargetConfiguration").apply {
        field("outputFolder", DataType.string(), "")
        field("type", DataType.userClassTest2(targetEnum))
    }
}
