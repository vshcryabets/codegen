package generators.obj.input

open class Block(
    name: String,
    val namespace: String
) : InLeaf(name) {
    val leafs: MutableList<InLeaf> = mutableListOf()
    val nodes: MutableList<Block> = mutableListOf()
    val classComment = StringBuilder()
    var sourceFile = ""
    var outputFile = ""
    var objectBaseFolder = ""

    fun addBlockComment(value : String) {
        classComment.append(value)
    }
}
