package generators.cpp

import ce.domain.usecase.add.AddBlockDefaultsUseCase
import generators.obj.FileGenerator
import generators.obj.TransformBlockUseCase
import generators.obj.input.InterfaceDescription
import generators.obj.out.FileData

class InterfaceGeneratorCpp(
    fileGenerator: FileGenerator,
    private val addBlockDefaultsUseCase: AddBlockDefaultsUseCase,
)
    : TransformBlockUseCase<InterfaceDescription> {

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

    override fun invoke(files: List<FileData>, desc: InterfaceDescription) {
        val header = files.find { it is CppHeaderFile }
            ?: throw java.lang.IllegalStateException("Can't find Header file for C++")

        //        val definition = CppClassData(desc.name, header)
        header.addSub(CppClassData(desc.name, header)).apply {
            addBlockDefaultsUseCase(desc, this)
        }
    }
}