package generators.rust

import ce.settings.Project
import generators.obj.FileGenerator
import generators.obj.Generator
import generators.obj.input.ClassField
import generators.obj.input.ConstantsBlock
import generators.obj.input.DataClass
import generators.obj.out.FileData

class RsDataClassGenerator(
    fileGenerator : FileGenerator,
    private val project: Project
) : Generator<DataClass, RustClassData>(fileGenerator) {

    override fun processBlock(file: FileData, desc: DataClass): RustClassData {
        val result = super.processBlock(file, desc)
        result.apply {
            appendNotEmptyWithNewLine(desc.classComment.toString(), classComment)
            classComment
                .append("Constants ${desc.name}")

            desc.subs.forEach { leaf ->
                val it = leaf as ClassField
                classDefinition.append("const ")
                    .append(it.name)
                    .append(" : ")
                    .append(Types.typeTo(file, it.type))
                    .append(" = ${Types.toValue(this, it.type, it.value)};")
                    .append(fileGenerator.newLine())
            }
        }
        return result
    }

    override fun createClassData(namespace: String): RustClassData = RustClassData(namespace)
}