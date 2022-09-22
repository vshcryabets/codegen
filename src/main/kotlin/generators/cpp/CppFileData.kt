package generators.cpp

import generators.obj.out.FileData
import generators.obj.out.OutNode

class CppFileData(name: String, parent: OutNode) : FileData(name, parent) {
    val headerInitialComments = StringBuilder()
    var headerBegin = StringBuilder()
    var headerEnd = StringBuilder()
    val headerIncludes = HashSet<String>()

    fun addHeaderInclude(name: String) {
        headerIncludes.add(name)
    }
}