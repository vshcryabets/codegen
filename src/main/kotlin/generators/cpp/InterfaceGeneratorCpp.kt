package generators.cpp

import generators.obj.FileGenerator
import generators.obj.Generator
import generators.obj.input.InterfaceDescription
import generators.obj.input.Node
import generators.obj.out.FileData

class InterfaceGeneratorCpp(fileGenerator: FileGenerator)
    : Generator<InterfaceDescription, CppClassData>(fileGenerator) {

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

    override fun processBlock(file: FileData, parent: Node, desc: InterfaceDescription): CppClassData {
        val result = CppClassData(desc.name, parent)
        val headerData = CppHeaderData(desc.name, result)
        result.subs.add(headerData)
        headerData.apply {
        }
        //prepareHeader(desc)
//        println(result.headers)
//        println(result.getIncludes())
//        println(result.classDefinition)
//        println(result.end)
        return result
    }
}