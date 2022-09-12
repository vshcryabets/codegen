package generators.obj.input

open class Block(
    val name: String,
    val namespace: String,
    val fields: List<ClassField>
) {
    val classComment = StringBuilder()
    var sourceFile = ""
    var outputFile = ""
    var objectBaseFolder = ""

    fun addBlockComment(value : String) {
        classComment.append(value)
    }
}
