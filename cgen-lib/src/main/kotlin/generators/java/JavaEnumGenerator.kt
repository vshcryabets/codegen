package generators.java

import ce.defs.DataType
import ce.domain.usecase.add.AddRegionDefaultsUseCase
import generators.obj.TransformBlockUseCase
import generators.obj.input.ConstantsEnum
import generators.obj.input.addSub
import generators.obj.out.FileData

class JavaEnumGenerator(
    fileGenerator: JavaFileGenerator,
    private val addBlockDefaultsUseCase: AddRegionDefaultsUseCase,
) : TransformBlockUseCase<ConstantsEnum> {

    override fun invoke(files: List<FileData>, desc: ConstantsEnum) {
        val file = files.find { it is FileData }
            ?: throw java.lang.IllegalStateException("Can't find Class file for Kotlin")
        file.addSub(JavaClassData(desc.name)).apply {
            addBlockDefaultsUseCase(desc, this)
            val withRawValues = desc.defaultDataType != DataType.VOID
//            subs.add(BlockStart("enum class ${desc.name}", this))
//            if (!withRawValues) {
//                classDefinition.append(" {")
//                    .append(fileGenerator.newLine())
//            } else {
//                classDefinition.append("(val rawValue : ${Types.typeTo(file, desc.defaultDataType)}) {")
//                    .append(fileGenerator.newLine())
//            }
//
//            val autoIncrement = AutoincrementField()
//            var needToAddComa = false
//            desc.subs.forEach { leaf ->
//                val it = leaf as DataField
//
//                if (withRawValues) {
//                    autoIncrement.invoke(it)
//                    putTabs(classDefinition, 1)
//                    classDefinition
//                        .append(it.name)
//                        .append("(${Types.toValue(this, it.type, it.value)}),")
//                        .append(fileGenerator.newLine())
//                } else {
//                    if (needToAddComa) {
//                        classDefinition.append(", ")
//                    }
//                    classDefinition.append(it.name)
//                    needToAddComa = true
//                }
//            }
//            if (!withRawValues) {
//                classDefinition.append(fileGenerator.newLine())
//                    .append(fileGenerator.newLine())
//            }
//            appendClassDefinition(this, "}");
        }
    }
}