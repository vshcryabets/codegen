package generators.java

import ce.domain.usecase.add.AddRegionDefaultsUseCase
import generators.obj.AutoincrementField
import generators.obj.FileGenerator
import generators.obj.TransformBlockUseCase
import generators.obj.input.ConstantsBlock
import generators.obj.input.addOutBlock
import generators.obj.input.addSub
import generators.obj.out.FileData

class JavaConstantsGenerator(
    private val addBlockDefaultsUseCase: AddRegionDefaultsUseCase,
) : TransformBlockUseCase<ConstantsBlock> {

    override fun invoke(blockFiles: List<FileData>, desc: ConstantsBlock) {
        val file = blockFiles.find { it is FileData }
            ?: throw java.lang.IllegalStateException("Can't find Main file for Kotlin")

        file.addSub(JavaClassData(desc.name)).apply {
            addBlockDefaultsUseCase(desc, this)
            addOutBlock("public class ${desc.name}") {
                val autoIncrement = AutoincrementField()
//            desc.subs.forEach { leaf ->
//                val it = leaf as DataField
//                autoIncrement.invoke(it)
//
//                classDefinition.append(fileGenerator.tabSpace);
//                classDefinition.append("public static final ${Types.typeTo(file, it.type)} ");
//                classDefinition.append(it.name);
//                classDefinition.append(" = ${Types.toValue(this, it.type, it.value)};")
//                classDefinition.append(fileGenerator.newLine())
//            }
//            appendNotEmptyWithNewLine("}", classDefinition)
            }
        }
    }
}