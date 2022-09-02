package generators.obj.input

open class ClassDescription(
    val name: String,
    val namespace: String,
    val fields: List<ClassField>
) {
    val classComment = StringBuilder()

    fun addClassComment(value : String) {
        classComment.append('\n')
        classComment.append(value)
    }
}
