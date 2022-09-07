package generators.obj.out

class FileData {
    var namespace : String = ""
    var headers = StringBuilder()
    var fileName : String = ""
    var fileFolder : String = ""
    var currentTabLevel : Int = 0
    var end = StringBuilder()
    val blocks = mutableMapOf<String, ClassData>()
    val includes = HashSet<String>()

    fun addInclude(name: String) {
        includes.add(name)
    }
}