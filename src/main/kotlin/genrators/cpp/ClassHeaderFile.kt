package genrators.cpp

class ClassHeaderFile {
    var headers = StringBuilder()
    var classDefinition = StringBuilder()
    var end = StringBuilder()

    private val includes = HashSet<String>()

    fun addInclude(name: String) {
        includes.add(name)
    }

    fun getIncludes() = StringBuilder().apply {
        includes.forEach {
            this.append("#include $it\n")
        }
    }
}