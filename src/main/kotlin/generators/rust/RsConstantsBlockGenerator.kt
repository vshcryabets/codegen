package generators.rust

import ce.settings.Project
import generators.obj.FileGenerator
import generators.obj.Generator
import generators.obj.input.ClassField
import generators.obj.input.ConstantsBlock
import generators.obj.input.Node
import generators.obj.out.FileData

class RsConstantsBlockGenerator(
    fileGenerator : FileGenerator,
    private val project: Project
) : Generator<ConstantsBlock, RustClassData>(fileGenerator) {

    override fun processBlock(file: FileData, parent: Node, desc: ConstantsBlock): RustClassData {
        val result = RustClassData(desc.name, parent)
        result.apply {
            desc.classComment.append("Constants ${desc.name}")
            addMultilineCommentsBlock(desc.classComment.toString(), result)
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
}