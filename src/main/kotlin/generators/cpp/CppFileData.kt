package generators.cpp

import generators.obj.out.FileData

class CppFileData : FileData() {
    val headerInitialComments = StringBuilder()
    var headerBegin = StringBuilder()
    var headerEnd = StringBuilder()
    val headerIncludes = HashSet<String>()

    fun addHeaderInclude(name: String) {
        headerIncludes.add(name)
    }
}