package generators.cpp

import ce.defs.DataType
import ce.domain.usecase.add.AddRegionDefaultsUseCase
import generators.obj.AutoincrementField
import generators.obj.TransformBlockUseCase
import generators.obj.input.ConstantsEnum
import generators.obj.input.DataField
import generators.obj.input.addCommentLine
import generators.obj.input.addEnumLeaf
import generators.obj.input.addKeyword
import generators.obj.input.addOutBlock
import generators.obj.input.addSeparator
import generators.obj.input.addSub
import generators.obj.input.addVarName
import generators.obj.input.findOrNull
import generators.obj.input.getParentPath
import generators.obj.out.CommentsBlock
import generators.obj.out.FileData
import generators.obj.out.NamespaceBlock
import generators.obj.out.RegionImpl

class CppEnumGenerator(
    private val addBlockDefaultsUseCase: AddRegionDefaultsUseCase,
) : TransformBlockUseCase<ConstantsEnum> {

    override fun invoke(files: List<FileData>, desc: ConstantsEnum) {
        val headerFile = files.find { it is CppHeaderFile }
            ?: throw java.lang.IllegalStateException("Can't find Header file for C++")

        val namespace = headerFile.addSub(NamespaceBlock(desc.getParentPath()))
        val region = namespace.addSub(RegionImpl(desc.name))
        val withRawValues = desc.defaultDataType != DataType.VOID
        val autoIncrement = AutoincrementField()

        addBlockDefaultsUseCase(desc, region)
        if (region.findOrNull(CommentsBlock::class.java) == null) {
            // add default comments block
            region.addSub(CommentsBlock()).apply {
                addCommentLine("Enum ${desc.name}")
            }
        }

        region.addOutBlock("enum ${desc.name}") {
            desc.subs.forEach { leaf ->
                if (leaf is DataField) {
                    val it = leaf

                    if (withRawValues) {
                        addEnumLeaf(it.name).apply {
                            autoIncrement(it)
                            addVarName(it.name)
                            addKeyword("=")
                            addSub(Types.toValue(it.getType(), it.getValue()))
                        }
                    } else {
                        addEnumLeaf(it.name)
                    }
                }
            }
        }
        region.addSeparator(";")


//
//        desc.subs.forEach {
//            if (it is ConstantDesc) {
//                autoIncrement.invoke(it)
//                region.addSub(
//                    ConstantLeaf().apply {
//                        addKeyword("const")
//                        addDatatype(Types.typeTo(headerFile, it.type))
//                        addVarName(it.name)
//                        addKeyword("=")
//                        addRValue(Types.toValue(it.type, it.value))
//                        addKeyword(";")
//                    }
//                )
//            }
//        }


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