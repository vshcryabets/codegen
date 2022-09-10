package generators.cpp

import ce.settings.CodeStyle
import ce.settings.Project
import generators.obj.FileGenerator
import generators.obj.Generator
import generators.obj.input.ConstantsEnum
import generators.obj.out.FileData

class CppEnumGenerator(
    fileGenerator: FileGenerator,
    private val project: Project
) : Generator<ConstantsEnum, CppClassData>(fileGenerator) {

    override fun buildBlock(file: FileData, desc: ConstantsEnum): CppClassData {
        val cppFile = file as CppFileData
        val result = super.buildBlock(file, desc)
        result.apply {
            if (desc.namespace.isNotEmpty()) {
                headerData.classDefinition.append("namespace $namespace {${fileGenerator.newLine()}");
//                headers.append("using $namespace;\n")
            }
//
//            if (desc.classComment.isNotEmpty()) {
//                appendClassDefinition(headerData, "/**")
//                desc.classComment.lines().forEach { line ->
//                    appendClassDefinition(headerData, "* $line")
//                }
//                appendClassDefinition(headerData, "*/")
//            }
//            appendClassDefinition(headerData, "enum ${desc.name} {");
//            headerData.currentTabLevel++;
//            var previous: Any? = null
//            desc.constants.forEach {
//                if (it.value == null && previous != null) {
//                    it.value = previous!! as Int + 1;
//                }
//
//                if (it.value != null) {
//                    previous = it.value
//                }
//                putTabs(headerData.classDefinition, headerData.currentTabLevel)
//                headerData.classDefinition.append(it.name);
//                headerData.classDefinition.append(" = ${Types.toValue(this, it.type, it.value)},")
//                headerData.classDefinition.append('\n')
//            }
//            headerData.currentTabLevel--;
//            appendClassDefinition(headerData, "};");
//
            if (namespace.isNotEmpty()) {
                headerData.classDefinition.append("} // $namespace${fileGenerator.newLine()}");
            }
            return result
        }


    override fun createClassData(): CppClassData = CppClassData()
}