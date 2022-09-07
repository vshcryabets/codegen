package generators.cpp

import ce.settings.CodeStyle
import ce.settings.Project
import generators.kotlin.KotlinClassData
import generators.obj.Generator
import generators.obj.input.Block
import generators.obj.input.ConstantsEnum
import generators.obj.out.FileData

class CppEnumGenerator(
    style: CodeStyle,
    private val project: Project
) : Generator<ConstantsEnum, CppClassData>(style) {

    override fun buildBlock(file: FileData, desc: ConstantsEnum): CppClassData {
        val result = super.buildBlock(file, desc)
        result.apply {

//            headerData.fileName = "${desc.name}.h"
//            fileName = "${desc.name}.cpp"
//
//            if (namespace.isNotEmpty()) {
//                headerData.headers.append("namespace $namespace {\n");
//                headerData.currentTabLevel++
//
////                headers.append("using $namespace;\n")
//            }
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
//            if (namespace.isNotEmpty()) {
//                headerData.end.append("} // $namespace\n");
//                headerData.currentTabLevel--
            }
            return result
        }


    override fun createClassData(): CppClassData = CppClassData()
}