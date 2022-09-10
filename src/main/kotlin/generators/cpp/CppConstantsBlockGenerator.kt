package generators.cpp

import ce.settings.CodeStyle
import ce.settings.Project
import generators.obj.FileGenerator
import generators.obj.Generator
import generators.obj.input.ConstantsBlock
import generators.obj.out.FileData

class CppConstantsBlockGenerator(
    fileGenerator: FileGenerator,
    private val project: Project
) : Generator<ConstantsBlock, CppClassData>(fileGenerator) {

    override fun buildBlock(file: FileData, desc: ConstantsBlock): CppClassData {
        val result = super.buildBlock(file, desc)
        result.apply {
//            if (desc.classComment.isNotEmpty()) {
//                classDefinition.append("/**\n")
//                desc.classComment.lines().forEach { line ->
//                    classDefinition.append("* $line\n")
//                }
//                classDefinition.append("*/\n")
//            }
//            classDefinition.append("object ${desc.name} {\n");
//            var previous: Any? = null
//            desc.constants.forEach {
//                if (it.value == null && previous != null) {
//                    it.value = previous!! as Int + 1;
//                }
//
//                if (it.value != null) {
//                    previous = it.value
//                }
//
//                classDefinition.append(tabSpace);
//                classDefinition.append("const val ");
//                classDefinition.append(it.name);
//                classDefinition.append(" : ${Types.typeTo(this, it.type)}")
//                classDefinition.append(" = ${Types.toValue(this, it.type, it.value)}")
//                classDefinition.append('\n')
//            }
//            classDefinition.append("}\n");
        }
        return result
    }

    override fun createClassData(): CppClassData = CppClassData()
}