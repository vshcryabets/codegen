package generators.kotlin

class ClassData : generators.obj.file.ClassData() {
    override fun getIncludes() = StringBuilder().apply {
        includes.forEach {
            this.append("import $it\n")
        }
    }
}