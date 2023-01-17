package generators.kotlin

import ce.settings.Project
import generators.obj.FileGenerator
import generators.obj.Generator
import generators.obj.input.DataClass
import generators.obj.input.DataField
import generators.obj.out.FileData

class KtDataClassGenerator(
    fileGenerator: FileGenerator,
    private val project: Project
) : Generator<DataClass>(fileGenerator) {

    override fun processBlock(blockFiles: List<FileData>, desc: DataClass): KotlinClassData {
        val file = blockFiles.find { it is FileData }
            ?: throw IllegalStateException("Can't find Main file for Kotlin")

        return file.addSub(KotlinClassData(desc.name)).apply {
            val kotlinClass = this
            addBlockDefaults(desc, this)
            addOutBlock("data class ${desc.name}") {
                addOutBlockArguments {
                    desc.subs.forEach { leaf ->
                        if (leaf is DataField) {
                            addDataField(
                                if (leaf.value.isDefined())
                                    "val ${leaf.name} : ${Types.typeTo(file, leaf.type)} = ${Types.toValue(kotlinClass, leaf.type, leaf.value)}"
                                else
                                    "val ${leaf.name} : ${Types.typeTo(file, leaf.type)}",
                                leaf.type)
                        }
//                val it = leaf as DataField
//
//                classDefinition.append(fileGenerator.tabSpace)
//                classDefinition.append("val ")
//                classDefinition.append(it.name)
//                classDefinition.append(" : ${Types.typeTo(file, it.type)}")
//                if (!it.value.notDefined()) {
//                    classDefinition.append(" = ${Types.toValue(this, it.type, it.value)}")
//                }
//
//                appendNotEmptyWithNewLine(",", classDefinition)
                    }
                }
            }
        }
    }
}