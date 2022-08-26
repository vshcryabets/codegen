package genrators.obj.file

abstract class ClassData {
    var headers = StringBuilder()
    var classDefinition = StringBuilder()
    var end = StringBuilder()

    protected val includes = HashSet<String>()

    fun addInclude(name: String) {
        includes.add(name)
    }

    abstract fun getIncludes() : StringBuilder
}