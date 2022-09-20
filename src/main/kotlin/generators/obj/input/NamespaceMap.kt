package generators.obj.input

import ce.defs.DataType


open class NamespaceMap() : Block("nsmap", TreeRoot) {
    val replaceMap = mutableMapOf<String, String>()

    fun replace(src: String, dst : String) {
        replaceMap.put(src, dst)
    }

    fun clear() {
        replaceMap.clear()
    }
}
