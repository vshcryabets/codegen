package generators.cpp

class CppClassData : generators.obj.out.ClassData() {
    override fun getIncludes() = StringBuilder().apply {
        includes.forEach {
            this.append("#include $it\n")
        }
    }
}