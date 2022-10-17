package generators.kotlin

import ce.defs.DataType
import ce.settings.Project
import generators.obj.AutoincrementInt
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
        val autoIncrement = AutoincrementInt()
        var needToAddComa = false

        return KotlinClassData(desc.name, file).also { classData ->
            file.addSub(classData)
            addBlockDefaults(desc, classData)
            classData.addSub(OutBlock("enum class ${desc.name}", classData)).apply {
                if (withRawValues) {
                    this.addSub(OutBlockArguments("", this)).apply {
                        addSub(DataField("val rawValue : ${Types.typeTo(file, desc.defaultDataType)}", this, desc.defaultDataType))
                    }
                }
                desc.subs.forEach { leaf ->
                    val it = leaf as DataField
                    if (needToAddComa) {
                        addSub(Separator(",${fileGenerator.newLine()}"))
                    }

                    if (withRawValues) {
                        autoIncrement(it)
                        addSub(EnumLeaf("${it.name}(${Types.toValue(classData, it.type, it.value)})", this))
                    } else {
                        addSub(EnumLeaf("${it.name}", this))
                    }
                    needToAddComa = true
                }
                addSub(NlSeparator())
            }
        }
    }
}