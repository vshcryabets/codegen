package generators.obj.out.nodes

import generators.obj.out.OutNode

class ImportsBlock : OutNode() {
    val includes = HashSet<String>()

    fun addInclude(name: String) {
        includes.add(name)
    }
}