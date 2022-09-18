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
        val result = super.processBlock(file, desc)
        result.apply {
            appendNotEmptyWithNewLine(desc.classComment, classComment)

            classDefinition.append("data class ${desc.name} (")
            classDefinition.append(fileGenerator.newLine())

            desc.leafs.forEach { leaf ->
                val it = leaf as ClassField

                classDefinition.append(fileGenerator.tabSpace)
                classDefinition.append("val ")
                classDefinition.append(it.name)
                classDefinition.append(" : ${Types.typeTo(file, it.type)}")
                if (it.value != NotDefined) {
                    classDefinition.append(" = ${Types.toValue(this, it.type, it.value)}")
                }

                classDefinition
                    .append(',')
                    .append(fileGenerator.newLine())
            }
            classDefinition.append(")")
                .append(fileGenerator.newLine())
        }
        return result
    }

    override fun createClassData(namespace: String): KotlinClassData = KotlinClassData(namespace)
}