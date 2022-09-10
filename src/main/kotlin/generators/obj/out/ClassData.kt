package generators.obj.out

open class ClassData(val namespace : String) {
    companion object {
        val emptyClassData = ClassData()
    }
    var classDefinition = StringBuilder()
}

