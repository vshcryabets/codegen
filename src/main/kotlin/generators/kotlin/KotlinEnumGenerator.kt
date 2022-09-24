package generators.kotlin

import ce.defs.DataType
import ce.settings.Project
import generators.obj.Generator
import generators.obj.input.*
import generators.obj.out.BlockStart
import generators.obj.out.ClassData
import generators.obj.out.FileData

class KotlinEnumGenerator(
    fileGenerator: KotlinFileGenerator,
    private val project: Project
) : Generator<ConstantsEnum, KotlinClassData>(fileGenerator) {

    override fun processBlock(file: FileData, parent: Node, desc: ConstantsEnum): KotlinClassData {
        val result = KotlinClassData(desc.name, parent)
        result.apply {
            addBlockDefaults(desc, this)
            val withRawValues = desc.defaultDataType != DataType.VOID
            subs.add(BlockStart("enum class ${desc.name}", this))
            if (!withRawValues) {
                classDefinition.append(" {")
                    .append(fileGenerator.newLine())
            } else {
                classDefinition.append("(val rawValue : ${Types.typeTo(file, desc.defaultDataType)}) {")
                    .append(fileGenerator.newLine())
            }

            var previous: Any? = null
            var needToAddComa = false
            desc.subs.forEach { leaf ->
                val it = leaf as ClassField
                if (NotDefined.equals(it.value) && previous != null) {
                    it.value = previous!! as Int + 1;
                }

                if (!NotDefined.equals(it.value)) {
                    previous = it.value
                }

                if (withRawValues) {
                    putTabs(classDefinition, 1)
                    classDefinition
                        .append(it.name)
                        .append("(${Types.toValue(this, it.type, it.value)}),")
                        .append(fileGenerator.newLine())
                } else {
                    if (needToAddComa) {
                        classDefinition.append(", ")
                    }
                    classDefinition.append(it.name)
                    needToAddComa = true
                }
            }
            if (!withRawValues) {
                classDefinition.append(fileGenerator.newLine())
                    .append(fileGenerator.newLine())
            }
            appendClassDefinition(result, "}");
        }
        return result
    }
}