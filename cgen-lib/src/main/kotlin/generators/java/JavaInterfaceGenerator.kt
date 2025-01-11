package generators.java

import ce.domain.usecase.add.AddRegionDefaultsUseCase
import ce.settings.Project
import generators.obj.TransformBlockUseCase
import generators.obj.input.DataField
import generators.obj.input.InterfaceDescription
import generators.obj.input.addSub
import generators.obj.out.FileData

class JavaInterfaceGenerator(
    private val addBlockDefaultsUseCase: AddRegionDefaultsUseCase,
) : TransformBlockUseCase<InterfaceDescription> {

    override fun invoke(files: List<FileData>, desc: InterfaceDescription) {
        val file = files.find { it is FileData }
            ?: throw java.lang.IllegalStateException("Can't find Class file for Kotlin")
        file.addSub(JavaClassData(desc.name)).apply {
            addBlockDefaultsUseCase(desc, this)
//            subs.add(BlockStart("interface ${desc.name}", this))

            desc.subs.forEach { leaf ->
//                val it = leaf as DataField
            }
//            appendClassDefinition(this, "}");
        }
    }

}