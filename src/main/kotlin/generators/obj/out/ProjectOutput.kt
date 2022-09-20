package generators.obj.out

import generators.obj.input.NamespaceMap

class ProjectOutput(
    val namespaceMap : NamespaceMap
) {
    val files = mutableMapOf<String, FileData>()
}