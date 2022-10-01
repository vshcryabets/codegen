package generators.java

import ce.settings.Project
import generators.obj.Generator
import generators.obj.input.ClassField
import generators.obj.input.InterfaceDescription
import generators.obj.out.BlockStart
import generators.obj.out.FileData

class JavaInterfaceGenerator(fileGenerator: JavaFileGenerator,
                             private val project: Project
) : Generator<InterfaceDescription, JavaClassData>(fileGenerator) {

    override fun processBlock(files: List<FileData>, desc: InterfaceDescription): JavaClassData {
        val file = files.find { it is FileData }
            ?: throw java.lang.IllegalStateException("Can't find Class file for Kotlin")
        return file.addSub(JavaClassData(desc.name, file)).apply {
            addBlockDefaults(desc, this)
            subs.add(BlockStart("interface ${desc.name}", this))

            desc.subs.forEach { leaf ->
                val it = leaf as ClassField
            }
            appendClassDefinition(this, "}");
        }
    }

}