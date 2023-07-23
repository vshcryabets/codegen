package generators.cpp

import ce.defs.DataType
import ce.domain.usecase.add.AddBlockDefaultsUseCase
import generators.obj.FileGenerator
import generators.obj.TransformBlockUseCase
import generators.obj.input.ConstantsEnum
import generators.obj.out.FileData

class CppEnumGenerator(
    fileGenerator: FileGenerator,
    private val addBlockDefaultsUseCase: AddBlockDefaultsUseCase,
) : TransformBlockUseCase<ConstantsEnum> {

    override fun invoke(files: List<FileData>, desc: ConstantsEnum) {
        val header = files.find { it is CppHeaderFile }
            ?: throw java.lang.IllegalStateException("Can't find Header file for C++")

        //        val definition = CppClassData(desc.name, header)
        header.addSub(CppClassData(desc.name)).apply {
            addBlockDefaultsUseCase(desc, this)
            val withRawValues = desc.defaultDataType != DataType.VOID

//            classDefinition.append("enum ${desc.name} {").append(fileGenerator.newLine())
//            val autoIncrement = AutoincrementField()
//            desc.subs.forEach { leaf ->
//                val it = leaf as DataField
//                putTabs(classDefinition, 1)
//
//                if (withRawValues) {
//                    autoIncrement.invoke(it)
//                    classDefinition.append(it.name);
//                    classDefinition.append(" = ${Types.toValue(this, it.type, it.value)},")
//                    classDefinition.append(fileGenerator.newLine())
//                } else {
//                    classDefinition.append("${it.name},${fileGenerator.newLine()}");
//                }
//            }
//            appendClassDefinition(this, "};");

        }
    }

}