package generators.cpp

import ce.settings.CodeStyle
import generators.obj.Generator
import generators.obj.InterfaceDescription
import generators.obj.out.ClassData
import generators.obj.out.FileData

class InterfaceGeneratorCpp(style: CodeStyle,) : Generator<InterfaceDescription, CppClassData>(style) {

//    fun prepareHeader(desc: InterfaceDescription) = CppClassData().apply {
//        headers.append("#pragma once\n")
//
//        if (desc.namespace.isNotEmpty()) {
//            classDefinition.append("namespace ${desc.namespace} {\n");
//        }
//        classDefinition.append("\n");
//        classDefinition.append("class ${desc.name} {\n");
//        classDefinition.append("public:\n");
//        desc.publicMethods.forEach {
//            classDefinition.append("    virtual ");
//            classDefinition.append("${Types.typeTo(this, it.result)} ${it.name}(")
//            it.arguments.forEach {
//                classDefinition.append("${Types.typeTo(this, it.datatype)} ${it.name}, ")
//            }
//            classDefinition.append(")")
//            classDefinition.append("\n")
//        }
//        classDefinition.append("};");
//
//        if (desc.namespace.isNotEmpty()) {
//            end.append("}");
//        }
//    }

    override fun buildBlock(file: FileData, desc: InterfaceDescription): CppClassData {
        val result = super.buildBlock(file, desc)
            //prepareHeader(desc)
//        println(result.headers)
//        println(result.getIncludes())
//        println(result.classDefinition)
//        println(result.end)
        return result
    }

    override fun createClassData(): CppClassData = CppClassData()
}