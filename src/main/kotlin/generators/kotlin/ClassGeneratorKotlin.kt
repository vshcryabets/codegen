package generators.kotlin

import generators.obj.ClassDescription

class ClassGeneratorKotlin : ClassGenerator {
    override fun build(desc: ClassDescription) {
        if (desc.namespace.isNotEmpty()) {
            println("package ${desc.namespace}");
        }
        println("");
        println("class ${desc.name} {");
        println("}");
    }
}