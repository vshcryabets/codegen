package generators.swift

import ce.settings.Project
import generators.obj.FileGenerator
import generators.obj.Generator
import generators.obj.input.ClassField
import generators.obj.input.DataClass
import generators.obj.input.Node
import generators.obj.out.FileData

class SwiftDataClassGenerator(
    fileGenerator : FileGenerator,
    private val project: Project
) : Generator<DataClass, SwiftClassData>(fileGenerator) {

    override fun processBlock(file: FileData, desc: DataClass): SwiftClassData {
        val parent = file.getNamespace(desc.getParentPath())
        val result = SwiftClassData(desc.name, parent)
        result.apply {
            addMultilineCommentsBlock(desc.classComment.toString(), result)

            classDefinition.append("struct ${desc.name} {")
            classDefinition.append(fileGenerator.newLine())
            var previous: Any? = null
            desc.subs.forEach { leaf ->
                val it = leaf as ClassField
                if (it.value == null && previous != null) {
                    it.value = previous!! as Int + 1;
                }

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
        return result
    }
}