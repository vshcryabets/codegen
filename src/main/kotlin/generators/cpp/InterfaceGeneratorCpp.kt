package generators.cpp

import generators.obj.FileGenerator
import generators.obj.Generator
import generators.obj.input.InterfaceDescription
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

    override fun processBlock(files: List<FileData>, desc: InterfaceDescription): CppClassData {
        val header = files.find { it is CppHeaderFile }
            ?: throw java.lang.IllegalStateException("Can't find Header file for C++")

        //        val definition = CppClassData(desc.name, header)
        return header.addSub(CppClassData(desc.name, header)).apply {
            addBlockDefaults(desc, this)
        }
    }
}