package generators.obj.out

open class ClassData(val namespace : String) {
    companion object {
        val emptyClassData = ClassData("")
    }
    val classStart = StringBuilder()
    val classComment = StringBuilder()
    val classDefinition = StringBuilder()
    val classEnd = StringBuilder()
}

