package generators.kotlin

import ce.domain.usecase.add.AddRegionDefaultsUseCase
import generators.obj.FileGenerator
import generators.obj.TransformBlockUseCase
import generators.obj.input.*
import generators.obj.out.FileData
import generators.obj.out.NlSeparator

class KtDataClassGenerator(
    fileGenerator: FileGenerator,
    private val addBlockDefaultsUseCase: AddRegionDefaultsUseCase,
) : TransformBlockUseCase<DataClass> {

    override fun invoke(blockFiles: List<FileData>, desc: DataClass) {
        val file = blockFiles.find { it is FileData }
            ?: throw IllegalStateException("Can't find Main file for Kotlin")

        file.addSub(KotlinClassData(desc.name)).apply {
            val kotlinClass = this
            addBlockDefaultsUseCase(desc, this)
            addOutBlock("data class ${desc.name}") {
                addOutBlockArguments {
                    addSub(NlSeparator())
                    var addNewLine = false
                    desc.subs.forEach { leaf ->
                        if (leaf is DataField) {
                            if (addNewLine) {
                                addSeparatorNewLine(",")
                            }
                            addDataField(
                                if (leaf.value.isDefined())
                                    "val ${leaf.name}: ${Types.typeTo(file, leaf.type)} = ${Types.toValue(kotlinClass, leaf.type, leaf.value)}"
                                else
                                    "val ${leaf.name}: ${Types.typeTo(file, leaf.type)}",
                                leaf.type)
                            addNewLine = true
                        }
                    }
                }
            }
        }
    }
}