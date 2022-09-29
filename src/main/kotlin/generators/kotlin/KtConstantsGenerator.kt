package generators.kotlin

import ce.settings.Project
import generators.obj.AutoincrementInt
import generators.obj.FileGenerator
import generators.obj.Generator
import generators.obj.input.ClassField
import generators.obj.input.ConstantsBlock
import generators.obj.out.BlockStart
import generators.obj.out.FileData

class KtConstantsGenerator(
    fileGenerator : FileGenerator,
    private val project: Project
) : Generator<ConstantsBlock, KotlinClassData>(fileGenerator) {

    override fun processBlock(blockFiles: List<FileData>, desc: ConstantsBlock): KotlinClassData {
        val file = blockFiles.find { it is FileData }
            ?: throw java.lang.IllegalStateException("Can't find Main file for Kotlin")

        return file.addSub(KotlinClassData(desc.name, file)).apply {
            addBlockDefaults(desc, this)
            subs.add(BlockStart("object ${desc.name} {", this))
            val autoIncrement = AutoincrementInt()
            desc.subs.forEach { leaf ->
                val it = leaf as ClassField
                autoIncrement.invoke(it)

                classDefinition.append(fileGenerator.tabSpace);
                classDefinition.append("const val ");
                classDefinition.append(it.name);
                classDefinition.append(" : ${Types.typeTo(file, it.type)}")
                classDefinition.append(" = ${Types.toValue(this, it.type, it.value)}")
                classDefinition.append(fileGenerator.newLine())
            }
            appendNotEmptyWithNewLine("}", classDefinition)
        }
    }
}