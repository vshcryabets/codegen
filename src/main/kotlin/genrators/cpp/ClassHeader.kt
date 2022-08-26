package genrators.cpp

open class ClassHeader : genrators.obj.file.ClassHeader() {
    override fun getIncludes() = StringBuilder().apply {
        includes.forEach {
            this.append("#include $it\n")
        }
    }
}