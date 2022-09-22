package generators.kotlin

import ce.settings.Project
import generators.obj.FileGenerator
import generators.obj.Generator
import generators.obj.input.ClassField
import generators.obj.input.DataClass
import generators.obj.input.NotDefined
import generators.obj.out.FileData

class KtDataClassGenerator(
    fileGenerator : FileGenerator,
    private val project: Project
) : Generator<DataClass, KotlinClassData>(fileGenerator) {

    override fun processBlock(file: FileData, desc: DataClass): KotlinClassData {
        val result = KotlinClassData(desc.getParentPath(), desc.name, file)
        result.apply {
            appendNotEmptyWithNewLine(desc.classComment, classComment)
            appendNotEmptyWithNewLine("data class ${desc.name} (", classDefinition)

            desc.subs.forEach { leaf ->
                val it = leaf as ClassField

                classDefinition.append(fileGenerator.tabSpace)
                classDefinition.append("val ")
                classDefinition.append(it.name)
                classDefinition.append(" : ${Types.typeTo(file, it.type)}")
                if (it.value != NotDefined) {
                    classDefinition.append(" = ${Types.toValue(this, it.type, it.value)}")
                }

                appendNotEmptyWithNewLine(",", classDefinition)
            }
            appendNotEmptyWithNewLine(")", classDefinition)
        }
        return result
    }
}