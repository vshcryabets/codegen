package generators.kotlin

import ce.settings.Project
import generators.obj.Generator
import generators.obj.input.ClassField
import generators.obj.input.InterfaceDescription
import generators.obj.out.BlockEnd
import generators.obj.out.BlockStart
import generators.obj.out.FileData

class KotlinInterfaceGenerator(fileGenerator: KotlinFileGenerator,
private val project: Project
) : Generator<InterfaceDescription>(fileGenerator) {

    override fun processBlock(files: List<FileData>, desc: InterfaceDescription): KotlinClassData {
        val file = files.find { it is FileData }
            ?: throw java.lang.IllegalStateException("Can't find Class file for Kotlin")
        return file.addSub(KotlinClassData(desc.name, file)).apply {
            addBlockDefaults(desc, this)
            addSub(BlockStart("interface ${desc.name}", this))

//            desc.subs.forEach { leaf ->
//                val it = leaf as ClassField
//            }
            addSub(BlockEnd("}", this))
        }
    }

}