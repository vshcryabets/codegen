package generators.cpp

open class ClassHeader : generators.obj.file.ClassHeader() {
    override fun getIncludes() = StringBuilder().apply {
        includes.forEach {
            this.append("#include $it\n")
        }
    }
}