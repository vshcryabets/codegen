package generators.kotlin

import ce.settings.Project
import generators.obj.Generator
import generators.obj.input.ClassField
import generators.obj.input.InterfaceDescription
import generators.obj.input.Method
import generators.obj.out.BlockStart
import generators.obj.out.FileData
import generators.obj.out.OutBlock

class KotlinInterfaceGenerator(fileGenerator: KotlinFileGenerator,
private val project: Project
) : Generator<InterfaceDescription, KotlinClassData>(fileGenerator) {

    override fun processBlock(files: List<FileData>, desc: InterfaceDescription): KotlinClassData {
        val file = files.find { it is FileData }
            ?: throw java.lang.IllegalStateException("Can't find Class file for Kotlin")
        return file.addSub(KotlinClassData(desc.name, file)).apply {
            addBlockDefaults(desc, this)
            addSub(OutBlock("interface ${desc.name}", this)).apply {
//                desc.subs.forEach { leaf ->
//                    when (leaf) {
//                        is Method ->
//                    }
//                }
//                appendClassDefinition(this, "}");
            }
        }
    }

}