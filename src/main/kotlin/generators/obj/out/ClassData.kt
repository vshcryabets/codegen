package generators.obj.out

abstract class ClassData {
    var namespace : String = ""
    var headers = StringBuilder()
    var classDefinition = StringBuilder()
    var end = StringBuilder()
    var fileName : String = ""
    var customBaseFolder : String = ""

    protected val includes = HashSet<String>()

    fun addInclude(name: String) {
        includes.add(name)
    }

    abstract fun getIncludes() : StringBuilder
}