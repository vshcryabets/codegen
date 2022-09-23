package generators.cpp

import ce.settings.Project
import generators.obj.FileGenerator
import generators.obj.Generator
import generators.obj.input.ClassField
import generators.obj.input.ConstantsEnum
import generators.obj.input.Node
import generators.obj.out.CommentLeaf
import generators.obj.out.CommentsBlock
import generators.obj.out.FileData

class CppEnumGenerator(
    fileGenerator: FileGenerator,
    private val project: Project
) : Generator<ConstantsEnum, CppClassData>(fileGenerator) {

    override fun processBlock(file: FileData, parent: Node, desc: ConstantsEnum): CppClassData {
        val result = CppClassData(desc.name, parent)
        val headerData = CppHeaderData(desc.name, result)
        result.subs.add(headerData)
        headerData.apply {
            if (desc.classComment.isNotEmpty()) {
                subs.add(CommentsBlock(this).apply {
                    desc.classComment.lines().forEach { line ->
                        subs.add(CommentLeaf("${fileGenerator.multilineCommentMid()} $line", this))
                    }
                })
            }
            if (desc.classComment.isNotEmpty()) {
                subs.add(CommentsBlock(this).apply {
                    subs.add(CommentLeaf(desc.classComment.toString(), this))
                })
            }

            classDefinition.append("enum ${desc.name} {").append(fileGenerator.newLine())
            var previous: Any? = null
            desc.subs.forEach { leaf ->
                val it = leaf as ClassField
                if (it.value == null && previous != null) {
                    it.value = previous!! as Int + 1;
                }

                if (it.value != null) {
                    previous = it.value
                }
                putTabs(classDefinition, 1)
                classDefinition.append(it.name);
                classDefinition.append(" = ${Types.toValue(this, it.type, it.value)},")
                classDefinition.append('\n')
            }
            appendClassDefinition(this, "};");

        }
        return result
    }

}