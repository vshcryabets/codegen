package generators.cpp

import ce.settings.CodeStyle
import generators.obj.Generator
import generators.obj.InterfaceDescription
import generators.obj.out.ClassData

class InterfaceGeneratorCpp(style: CodeStyle,) : Generator<CppClassData>(style) {

    fun prepareHeader(desc: InterfaceDescription) = CppClassData().apply {
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

    override fun createClassData(): CppClassData = CppClassData()
}