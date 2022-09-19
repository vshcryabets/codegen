package generators.obj.out

open class OutNode : OutLeaf() {
    val leafs = mutableListOf<OutLeaf>()

    fun <T : OutNode> findSub(clazz : Class<T>) : T {
        leafs.forEach {
            if (it.javaClass == clazz) {
                return it as T
            }
        }
        val newNode = clazz.getDeclaredConstructor().newInstance()
        leafs.add(newNode)
        return newNode
    }

    fun addLeaf(leaf: OutLeaf) {
        leafs.add(leaf)
    }
}