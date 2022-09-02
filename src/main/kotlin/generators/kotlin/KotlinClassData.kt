package generators.kotlin

class KotlinClassData : generators.obj.out.ClassData() {
    override fun getIncludes() = StringBuilder().apply {
        includes.forEach {
            this.append("import $it\n")
        }
    }
}