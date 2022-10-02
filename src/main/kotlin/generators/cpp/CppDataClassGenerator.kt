package generators.cpp

import ce.settings.Project
import generators.obj.FileGenerator
import generators.obj.Generator
import generators.obj.input.ClassField
import generators.obj.input.DataClass
import generators.obj.input.NotDefined
import generators.obj.out.CommentLeaf
import generators.obj.out.CommentsBlock
import generators.obj.out.FileData
import generators.obj.out.ImportsBlock

class CppDataClassGenerator(
    fileGenerator: FileGenerator,
    private val project: Project
) : Generator<DataClass, CppClassData>(fileGenerator) {

    override fun processBlock(files: List<FileData>, desc: DataClass): CppClassData {
        val header = files.find { it is CppHeaderFile }
            ?: throw java.lang.IllegalStateException("Can't find Header file for C++")
        val definition = files.find { it is CppFileData }
            ?: throw java.lang.IllegalStateException("Can't find Definition file for C++")

        definition.findSub(ImportsBlock::class.java).addInclude(header.name)

        return header.addSub(CppClassData(desc.name, header)).apply {
            desc.classComment.append("Data class ${desc.name}${fileGenerator.newLine()}")
            addBlockDefaults(desc, this)

            classDefinition.append("class ${desc.name} {${fileGenerator.newLine()}")
            classDefinition.append("private:${fileGenerator.newLine()}")
            desc.subs.forEach { leaf ->
                val it = leaf as ClassField
                putTabs(classDefinition, 1)
                classDefinition
                    .append(Types.typeTo(header, it.type))
                    .append(" ")
                    .append(it.name)
                if (it.value != NotDefined) {
                    classDefinition.append(" = ${Types.toValue(this, it.type, it.value)};${fileGenerator.newLine()}")
                }
                classDefinition
                    .append(";")
                    .append(fileGenerator.newLine())
            }
            classDefinition.append("}${fileGenerator.newLine()}")
        }
    }
}