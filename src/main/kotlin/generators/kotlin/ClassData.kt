package generators.kotlin

class ClassData : generators.obj.out.ClassData() {
    override fun getIncludes() = StringBuilder().apply {
        includes.forEach {
            this.append("import $it\n")
        }
    }
}