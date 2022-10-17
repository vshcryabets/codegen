package generators.rust

import ce.settings.Project
import generators.obj.FileGenerator
import generators.obj.Generator
import generators.obj.input.DataField
import generators.obj.input.ConstantsBlock
import generators.obj.out.FileData

class RsConstantsBlockGenerator(
    fileGenerator : FileGenerator,
    private val project: Project
) : Generator<ConstantsBlock>(fileGenerator) {

    override fun processBlock(blockFiles: List<FileData>, desc: ConstantsBlock): RustClassData {
        val file = blockFiles.find { it is FileData }
            ?: throw java.lang.IllegalStateException("Can't find Main file for Rust")

        return file.addSub(RustClassData(desc.name, file)).apply {
            desc.classComment.append("Constants ${desc.name}")
            addMultilineCommentsBlock(desc.classComment.toString(), this)
            desc.subs.forEach { leaf ->
                val it = leaf as DataField
                classDefinition.append("const ")
                    .append(it.name)
                    .append(" : ")
                    .append(Types.typeTo(file, it.type))
                    .append(" = ${Types.toValue(this, it.type, it.value)};")
                    .append(fileGenerator.newLine())
            }
        }
    }
}