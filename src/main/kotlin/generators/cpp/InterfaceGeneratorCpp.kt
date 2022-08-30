package generators.cpp

import generators.obj.InterfaceDescription

class InterfaceGeneratorCpp  {



    fun prepareHeader(desc: InterfaceDescription) = ClassHeader().apply {
        headers.append("#pragma once\n")

        if (desc.namespace.isNotEmpty()) {
            classDefinition.append("namespace ${desc.namespace} {\n");
        }
        classDefinition.append("\n");
        classDefinition.append("class ${desc.name} {\n");
        classDefinition.append("public:\n");
        desc.publicMethods.forEach {
            classDefinition.append("    virtual ");
            classDefinition.append("${Types.typeTo(this, it.result)} ${it.name}(")
            it.arguments.forEach {
                classDefinition.append("${Types.typeTo(this, it.datatype)} ${it.name}, ")
            }
            classDefinition.append(")")
            classDefinition.append("\n")
        }
        classDefinition.append("};");

        if (desc.namespace.isNotEmpty()) {
            end.append("}");
        }
    }

    fun build(desc: InterfaceDescription) {
        val result = prepareHeader(desc)
        println(result.headers)
        println(result.getIncludes())
        println(result.classDefinition)
        println(result.end)
    }
}