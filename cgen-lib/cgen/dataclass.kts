import ce.defs.*

namespace("ce.defs.domain").apply {

    dataClass("DirsConfiguration").apply {
        field("workingDir", DataType.string, "")
        field("tempDir", DataType.string, "")
    }
}
