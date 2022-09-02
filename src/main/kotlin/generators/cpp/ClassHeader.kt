package generators.cpp

open class ClassHeader : generators.obj.out.ClassHeader() {
    override fun getIncludes() = StringBuilder().apply {
        includes.forEach {
            this.append("#include $it\n")
        }
    }
}