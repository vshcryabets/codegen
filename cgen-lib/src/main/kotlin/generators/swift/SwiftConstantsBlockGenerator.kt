package generators.swift

import ce.settings.Project
import generators.obj.FileGenerator
import generators.obj.TransformBlockUseCase
import generators.obj.input.ConstantsBlock
import generators.obj.out.FileData

class SwiftConstantsBlockGenerator(
    fileGenerator : FileGenerator,
    private val project: Project
) : TransformBlockUseCase<ConstantsBlock> {

    override fun invoke(blockFiles: List<FileData>, desc: ConstantsBlock) {
        val file = blockFiles.find { it is FileData }
            ?: throw java.lang.IllegalStateException("Can't find Main file for Swift")

        file.addSub(SwiftClassData(desc.name)).apply {
//            addMultilineCommentsBlock(desc.classComment.toString(), this)

//            classDefinition.append("struct ${desc.name} {")
//            classDefinition.append(fileGenerator.newLine())
//            val autoIncrement = AutoincrementField()
//            desc.subs.forEach { leaf ->
//                val it = leaf as DataField
//                autoIncrement(it)
//
//                classDefinition.append(fileGenerator.tabSpace);
//                classDefinition.append("static let ");
//                classDefinition.append(it.name);
//                classDefinition.append(" : ${Types.typeTo(file, it.type)}")
//                classDefinition.append(" = ${Types.toValue(this, it.type, it.value)}")
//                classDefinition.append(fileGenerator.newLine())
//            }
//            classDefinition.append("}\n");
        }
    }
}