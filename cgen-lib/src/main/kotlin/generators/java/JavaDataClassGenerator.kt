package generators.java

import ce.domain.usecase.add.AddRegionDefaultsUseCase
import generators.obj.FileGenerator
import generators.obj.TransformBlockUseCase
import generators.obj.input.DataClass
import generators.obj.input.addSub
import generators.obj.out.FileData

class JavaDataClassGenerator(
    private val addBlockDefaultsUseCase: AddRegionDefaultsUseCase,
) : TransformBlockUseCase<DataClass> {

    override fun invoke(blockFiles: List<FileData>, desc: DataClass) {
        val file = blockFiles.find { it is FileData }
            ?: throw java.lang.IllegalStateException("Can't find Main file for Kotlin")

        file.addSub(JavaClassData(desc.name)).apply {
            addBlockDefaultsUseCase(desc, this)
//            appendNotEmptyWithNewLine("data class ${desc.name} (", classDefinition)
//
//            desc.subs.forEach { leaf ->
//                val it = leaf as DataField
//
//                classDefinition.append(fileGenerator.tabSpace)
//                classDefinition.append("val ")
//                classDefinition.append(it.name)
//                classDefinition.append(" : ${Types.typeTo(file, it.type)}")
//                if (it.value.notDefined()) {
//                    classDefinition.append(" = ${Types.toValue(this, it.type, it.value)}")
//                }
//
//                appendNotEmptyWithNewLine(",", classDefinition)
//            }
//            appendNotEmptyWithNewLine(")", classDefinition)
        }
    }
}