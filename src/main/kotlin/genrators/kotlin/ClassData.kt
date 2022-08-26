package genrators.kotlin

class ClassData : genrators.obj.file.ClassData() {
    override fun getIncludes() = StringBuilder().apply {
        includes.forEach {
            this.append("import $it\n")
        }
    }
}