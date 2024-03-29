package generators.rust

import ce.settings.Project
import generators.obj.FileGenerator
import generators.obj.TransformBlockUseCase
import generators.obj.input.DataField
import generators.obj.input.DataClass
import generators.obj.input.addSub
import generators.obj.out.FileData

class RsDataClassGenerator(
    fileGenerator : FileGenerator,
    private val project: Project
) : TransformBlockUseCase<DataClass> {

    override fun invoke(blockFiles: List<FileData>, desc: DataClass) {
        val file = blockFiles.find { it is FileData }
            ?: throw java.lang.IllegalStateException("Can't find Main file for Rust")

        file.addSub(RustClassData(desc.name)).apply {
//            addMultilineCommentsBlock(desc.classComment.toString(), this)

            desc.subs.forEach { leaf ->
                val it = leaf as DataField
//                classDefinition.append("const ")
//                    .append(it.name)
//                    .append(" : ")
//                    .append(Types.typeTo(file, it.type))
//                    .append(" = ${Types.toValue(this, it.type, it.value)};")
//                    .append(fileGenerator.newLine())
            }
        }
    }
}