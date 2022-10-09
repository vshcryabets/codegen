package generators.kotlin

import ce.settings.Project
import generators.obj.AutoincrementInt
import generators.obj.FileGenerator
import generators.obj.Generator
import generators.obj.input.ConstantDesc
import generators.obj.input.ConstantsBlock
import generators.obj.out.BlockEnd
import generators.obj.out.BlockStart
import generators.obj.out.ConstantLeaf
import generators.obj.out.FileData

class KtConstantsGenerator(
    fileGenerator : FileGenerator,
    private val project: Project
) : Generator<ConstantsBlock>(fileGenerator) {

    override fun processBlock(blockFiles: List<FileData>, desc: ConstantsBlock): KotlinClassData {
        val file = blockFiles.find { it is FileData }
            ?: throw java.lang.IllegalStateException("Can't find Main file for Kotlin")
        val autoIncrement = AutoincrementInt()

        return file.addSub(KotlinClassData(desc.name, file)).apply {
            addBlockDefaults(desc, this)
            addSub(BlockStart("object ${desc.name} {", this))
            desc.subs.forEach {
                if (it is ConstantDesc) {
                    autoIncrement.invoke(it)
                    addSub(ConstantLeaf(
                            "const val ${it.name} : " +
                                    "${Types.typeTo(file, it.type)} = " +
                                    "${Types.toValue(this, it.type, it.value)}", this
                        )
                    )
                }
            }
            addSub(BlockEnd("}", this))
        }
    }
}