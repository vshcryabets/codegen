package generators.kotlin

import ce.defs.DataType
import ce.settings.Project
import generators.obj.AutoincrementField
import generators.obj.Generator
import generators.obj.input.*
import generators.obj.out.*

class KotlinEnumGenerator(
    fileGenerator: KotlinFileGenerator,
    private val project: Project
) : Generator<ConstantsEnum>(fileGenerator) {

    override fun processBlock(files: List<FileData>, desc: ConstantsEnum): KotlinClassData {
        val file = files.find { it is FileData }
            ?: throw java.lang.IllegalStateException("Can't find Class file for Kotlin")
        val withRawValues = desc.defaultDataType != DataType.VOID
        val autoIncrement = AutoincrementField()
        var needToAddComa = false

        return file.addSub(KotlinClassData(desc.name)).also { classData ->
            addBlockDefaults(desc, classData)
            classData.addOutBlock("enum class ${desc.name}") {
                if (withRawValues) {
                    addOutBlockArguments {
                        addDataField("val rawValue : ${Types.typeTo(file, desc.defaultDataType)}", desc.defaultDataType)
                    }
                }
                desc.subs.forEach { leaf ->
                    if (leaf is DataField) {
                        val it = leaf as DataField
                        if (needToAddComa) {
                            addSeparator(",${fileGenerator.newLine()}")
                        }

                        if (withRawValues) {
                            autoIncrement(it)
                            addEnumLeaf("${it.name}(${Types.toValue(classData, it.type, it.value)})")
                        } else {
                            addEnumLeaf("${it.name}")
                        }
                        needToAddComa = true
                    }
                }
                addSub(NlSeparator())
            }
        }
    }
}