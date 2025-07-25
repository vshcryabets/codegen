import ce.defs.*

namespace("ce.defs").apply {

    enum("MetaEngine").apply {
        add("KTS")
        add("GROOVY")
    }

    val targetEnum = enum("Target").apply {
        defaultType(DataType.string())
        add("Other", "other")
        add("Kotlin", "kotlin")
        add("C", "c")
        add("Cpp", "cpp")
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
