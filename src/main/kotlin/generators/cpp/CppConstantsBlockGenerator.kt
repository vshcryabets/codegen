package generators.cpp

import ce.settings.Project
import generators.obj.FileGenerator
import generators.obj.Generator
import generators.obj.input.ClassField
import generators.obj.input.ConstantsBlock
import generators.obj.input.NotDefined
import generators.obj.out.FileData
import javax.xml.stream.events.Namespace

class CppConstantsBlockGenerator(
    fileGenerator: FileGenerator,
    private val project: Project
) : Generator<ConstantsBlock, CppClassData>(fileGenerator) {

    override fun processBlock(file: FileData, desc: ConstantsBlock): CppClassData {
        val result = super.processBlock(file, desc)
        result.headerData.apply {
            appendNotEmptyWithNewLine(desc.classComment.toString(), classComment)
            classComment
                .append("Constants ${desc.name}")
                .append(fileGenerator.newLine())

            var previous: Any? = null
            desc.leafs.forEach { leaf ->
                val it = leaf as ClassField
                if ((it.value == null || it.value == NotDefined) && previous != null) {
                    it.value = previous!! as Int + 1;
                }

                if (it.value != null) {
                    previous = it.value
                }

                classDefinition.append("const ")
                    .append(Types.typeTo(file, it.type))
                    .append(" ")
                    .append(it.name)
                    .append(" = ${Types.toValue(this, it.type, it.value)};")
                    .append(fileGenerator.newLine())
            }
        }
        return result
    }

    override fun createClassData(namespace: String): CppClassData = CppClassData(namespace)
}