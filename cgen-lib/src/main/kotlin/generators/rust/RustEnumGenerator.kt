package generators.rust

import ce.defs.DataType
import ce.defs.DataValueImpl
import ce.settings.Project
import generators.obj.TransformBlockUseCase
import generators.obj.abstractSyntaxTree.ConstantsEnum
import generators.obj.abstractSyntaxTree.DataField
import generators.obj.abstractSyntaxTree.addSub
import generators.obj.syntaxParseTree.FileData

class RustEnumGenerator(
    fileGenerator: RustFileGenerator,
    private val project: Project
) : TransformBlockUseCase<ConstantsEnum> {

    override fun invoke(blockFiles: List<FileData>, desc: ConstantsEnum) {
        val file = blockFiles.find { it is FileData }
            ?: throw java.lang.IllegalStateException("Can't find Main file for Rust")

        file.addSub(RustClassData(desc.name)).apply {
//            addMultilineCommentsBlock(desc.classComment.toString(), this)
            val withRawValues = !(desc.defaultDataType is DataType.VOID)
//            if (withRawValues) {
//                appendClassDefinition(this, "enum ${desc.name}  : ${Types.typeTo(file, desc.defaultDataType)} {")
//            } else {
//                appendClassDefinition(this, "enum ${desc.name} {");
//                putTabs(classDefinition, 1)
//                classDefinition.append("case ")
//            }
            var previous: Any? = null
            var needToAddComa = false
            desc.subs.forEach { leaf ->
                val it = leaf as DataField
//                if (it.value == null && previous != null) {
//                    it.value = previous!! as Int + 1;
//                }

                if (it.getValue() != DataValueImpl.NotDefinedValue) {
                    previous = it.getValue()
                }

//                if (withRawValues) {
//                    putTabs(classDefinition, 1)
//                    classDefinition.append("case ")
//                        .append(it.name)
//                        .append(" = ${Types.toValue(this, it.type, it.value)}")
//                        .append(fileGenerator.newLine())
//                } else {
//                    if (needToAddComa) {
//                        classDefinition.append(", ")
//                    }
//                    classDefinition.append(it.name)
//                    needToAddComa = true
//                }
            }
//            if (!withRawValues) {
//                classDefinition.append(fileGenerator.newLine())
//            }
//            appendClassDefinition(this, "}");
        }
    }
}