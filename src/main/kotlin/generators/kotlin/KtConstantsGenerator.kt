package generators.kotlin

import ce.settings.Project
import generators.obj.FileGenerator
import generators.obj.Generator
import generators.obj.input.ClassField
import generators.obj.input.ConstantsBlock
import generators.obj.input.NotDefined
import generators.obj.out.FileData

class KtConstantsGenerator(
    fileGenerator : FileGenerator,
    private val project: Project
) : Generator<ConstantsBlock, KotlinClassData>(fileGenerator) {

    override fun processBlock(file: FileData, desc: ConstantsBlock): KotlinClassData {
        val result = super.processBlock(file, desc)
        result.apply {
            appendNotEmptyWithNewLine(desc.classComment, classComment)
            appendNotEmptyWithNewLine("object ${desc.name} {", classDefinition)
            var previous: Any? = null
            desc.leafs.forEach { leaf ->
                val it = leaf as ClassField
                if ((it.value == null || it.value == NotDefined) && previous != null) {
                    it.value = previous!! as Int + 1;
                }

                if (it.value != null) {
                    previous = it.value
                }

                classDefinition.append(fileGenerator.tabSpace);
                classDefinition.append("const val ");
                classDefinition.append(it.name);
                classDefinition.append(" : ${Types.typeTo(file, it.type)}")
                classDefinition.append(" = ${Types.toValue(this, it.type, it.value)}")
                classDefinition.append(fileGenerator.newLine())
            }
            appendNotEmptyWithNewLine("}", classDefinition)
        }
        return result
    }

    override fun createClassData(namespace: String): KotlinClassData = KotlinClassData(namespace)
}