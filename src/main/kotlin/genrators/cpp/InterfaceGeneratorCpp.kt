package genrators.cpp

import genrators.obj.DataType
import genrators.obj.InterfaceDescription
import javax.xml.crypto.Data

class InterfaceGeneratorCpp  {

    fun typeTo(file: ClassHeaderFile,
               type: DataType) : String =
        when (type) {
            DataType.VOID -> "void"
            DataType.uint16 -> "uint16_t"
            DataType.string -> {
                file.addInclude("<string>")
                "std::string"
            }
            else -> "QQQQ"
        }

    fun prepareHeader(desc: InterfaceDescription) = ClassHeaderFile().apply {
        headers.append("#pragma once\n")

        if (desc.namespace.isNotEmpty()) {
            classDefinition.append("namespace ${desc.namespace} {\n");
        }
        classDefinition.append("\n");
        classDefinition.append("class ${desc.name} {\n");
        classDefinition.append("public:\n");
        desc.publicMethods.forEach {
            classDefinition.append("    virtual ");
            classDefinition.append("${typeTo(this, it.result)} ${it.name}(")
            it.arguments.forEach {
                classDefinition.append("${typeTo(this, it.datatype)} ${it.name}, ")
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