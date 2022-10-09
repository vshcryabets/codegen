package generators.java

import ce.settings.Project
import generators.obj.FileGenerator
import generators.obj.Generator
import generators.obj.input.ClassField
import generators.obj.input.DataClass
import generators.obj.input.NotDefined
import generators.obj.out.FileData

class JavaDataClassGenerator(
    fileGenerator : FileGenerator,
    private val project: Project
) : Generator<DataClass>(fileGenerator) {

    override fun processBlock(blockFiles: List<FileData>, desc: DataClass): JavaClassData {
        val file = blockFiles.find { it is FileData }
            ?: throw java.lang.IllegalStateException("Can't find Main file for Kotlin")

        return file.addSub(JavaClassData(desc.name, file)).apply {
            addBlockDefaults(desc, this)
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
    }
}