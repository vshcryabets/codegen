package generators.swift

import ce.settings.Project
import generators.obj.FileGenerator
import generators.obj.Generator
import generators.obj.input.ClassField
import generators.obj.input.ConstantsBlock
import generators.obj.out.FileData

class SwiftConstantsBlockGenerator(
    fileGenerator : FileGenerator,
    private val project: Project
) : Generator<ConstantsBlock, SwiftClassData>(fileGenerator) {

    override fun processBlock(file: FileData, desc: ConstantsBlock): SwiftClassData {
        val result = super.processBlock(file, desc)
        result.apply {
            classComment.append(desc.classComment).append(fileGenerator.newLine())

            classDefinition.append("struct ${desc.name} {")
            classDefinition.append(fileGenerator.newLine())
            var previous: Any? = null
            desc.leafs.forEach { leaf ->
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

    override fun createClassData(namespace: String): SwiftClassData = SwiftClassData(namespace)
}