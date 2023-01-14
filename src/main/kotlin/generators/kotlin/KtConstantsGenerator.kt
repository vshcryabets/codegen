package generators.kotlin

import ce.settings.Project
import generators.obj.AutoincrementField
import generators.obj.FileGenerator
import generators.obj.Generator
import generators.obj.input.ConstantDesc
import generators.obj.input.ConstantsBlock
import generators.obj.out.*

class KtConstantsGenerator(
    fileGenerator : FileGenerator,
    private val project: Project
) : Generator<ConstantsBlock>(fileGenerator) {

    override fun processBlock(blockFiles: List<FileData>, desc: ConstantsBlock): KotlinClassData {
        val file = blockFiles.find { it is FileData }
            ?: throw java.lang.IllegalStateException("Can't find Main file for Kotlin")
        val autoIncrement = AutoincrementField()

        return file.addSub(KotlinClassData(desc.name, file)).also { classData ->
            addBlockDefaults(desc, classData)
            classData.addSub(OutBlock("object ${desc.name}", classData)).apply {
                desc.subs.forEach {
                    if (it is ConstantDesc) {
                        autoIncrement.invoke(it)
                        addSub(ConstantLeaf(
                            "const val ${it.name} : " +
                                    "${Types.typeTo(file, it.type)} = " +
                                    "${Types.toValue(classData, it.type, it.value)}"))
                    }
                }
            }
        }
    }
}