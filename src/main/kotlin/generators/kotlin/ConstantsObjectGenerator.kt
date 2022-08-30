package generators.kotlin

import generators.obj.ConstantsEnum

class ConstantsObjectGenerator {

    fun build(desc: ConstantsEnum) : ClassData  {
        val result = ClassData().apply {
            fileName = "${desc.name}.kt"
            headers.append("package ${desc.namespace}\n");

            classDefinition.append("object ${desc.name} {\n");
            var previous : Any? = null
            desc.constants.forEach {
                if (it.value == null && previous != null) {
                    it.value = previous!! as Int + 1;
                }

                if (it.value != null) {
                    previous = it.value
                }

                classDefinition.append("    ");
                classDefinition.append("const val ");
                classDefinition.append(it.name);
                classDefinition.append(" : ${Types.typeTo(this, it.type)}")
                classDefinition.append(" = ${Types.toValue(this, it.type, it.value)}")
                classDefinition.append('\n')
            }
            classDefinition.append("}\n");
        }
        return result
    }
}