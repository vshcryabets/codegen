package genrators.kotlin

import genrators.kotlin.Types
import genrators.obj.ClassDescription
import genrators.obj.ConstantsEnum

class ConstantsObjectGenrator {
    fun build(desc: ConstantsEnum)  {
        val result = genrators.kotlin.ClassData().apply {
            headers.append("package ${desc.namespace}\n");

            classDefinition.append("object ${desc.name} {\n");
            desc.constants.forEach {
                classDefinition.append("    ");
                classDefinition.append("const val ");
                classDefinition.append(it.name);
                classDefinition.append(" : ${Types.typeTo(this, it.type)} = ")
                classDefinition.append('\n')
            }
            classDefinition.append("}\n");
        }

        println(result.headers)
        println(result.getIncludes())
        println(result.classDefinition)
        println(result.end)
    }
}