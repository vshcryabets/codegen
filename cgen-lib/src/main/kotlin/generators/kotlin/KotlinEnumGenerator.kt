package generators.kotlin

import ce.defs.DataType
import ce.domain.usecase.add.AddRegionDefaultsUseCase
import generators.obj.AutoincrementField
import generators.obj.FileGenerator
import generators.obj.TransformBlockUseCase
import generators.obj.input.*
import generators.obj.out.*

class KotlinEnumGenerator(
    fileGenerator: FileGenerator,
    private val addBlockDefaultsUseCase: AddRegionDefaultsUseCase,
) : TransformBlockUseCase<ConstantsEnum> {

    override fun invoke(files: List<FileData>, desc: ConstantsEnum) {
        val file = files.find { it is FileData }
            ?: throw java.lang.IllegalStateException("Can't find Class file for Kotlin")
        val withRawValues = desc.defaultDataType != DataType.VOID
        val autoIncrement = AutoincrementField()
        var needToAddComa = false

        file.addSub(KotlinClassData(desc.name)).also { classData ->
            addBlockDefaultsUseCase(desc, classData)
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
                            addSeparatorNewLine(",")
                        }

                        if (withRawValues) {
                            autoIncrement(it)
                            addEnumLeaf("${it.name}(${Types.toValue(it.type, it.value)})")
                        } else {
                            addEnumLeaf("${it.name}")
                        }
                        needToAddComa = true
                    }
                }
            }
        }
    }
}