package generators.swift

import ce.settings.Project
import generators.obj.FileGenerator
import generators.obj.Generator
import generators.obj.input.DataField
import generators.obj.input.DataClass
import generators.obj.out.FileData

class SwiftDataClassGenerator(
    fileGenerator : FileGenerator,
    private val project: Project
) : Generator<DataClass>(fileGenerator) {

    override fun processBlock(blockFiles: List<FileData>, desc: DataClass): SwiftClassData {
        val file = blockFiles.find { it is FileData }
            ?: throw java.lang.IllegalStateException("Can't find Main file for Swift")

        return file.addSub(SwiftClassData(desc.name, file)).apply {
//            addMultilineCommentsBlock(desc.classComment.toString(), this)

            classDefinition.append("struct ${desc.name} {")
            classDefinition.append(fileGenerator.newLine())
            var previous: Any? = null
            desc.subs.forEach { leaf ->
                val it = leaf as DataField
//                if (it.value == null && previous != null) {
//                    it.value = previous!! as Int + 1;
//                }

                if (it.value != null) {
                    previous = it.value
                }

                classDefinition.append(fileGenerator.tabSpace);
                classDefinition.append("static let ");
                classDefinition.append(it.name);
                classDefinition.append(" : ${Types.typeTo(file, it.type)}")
                classDefinition.append(" = ${Types.toValue(this, it.type, it.value)}")
                classDefinition.append(fileGenerator.newLine())
            }
            classDefinition.append("}\n");
        }
    }
}