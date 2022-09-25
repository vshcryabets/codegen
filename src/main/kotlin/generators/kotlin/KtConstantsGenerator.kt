package generators.kotlin

import ce.settings.Project
import generators.obj.FileGenerator
import generators.obj.Generator
import generators.obj.input.ClassField
import generators.obj.input.ConstantsBlock
import generators.obj.input.Node
import generators.obj.input.NotDefined
import generators.obj.out.BlockStart
import generators.obj.out.FileData

class KtConstantsGenerator(
    fileGenerator : FileGenerator,
    private val project: Project
) : Generator<ConstantsBlock, KotlinClassData>(fileGenerator) {

    override fun processBlock(file: FileData, parent: Node, desc: ConstantsBlock): KotlinClassData {
        val result = KotlinClassData(desc.name, parent)
        result.apply {
            addBlockDefaults(desc, this)
            subs.add(BlockStart("object ${desc.name} {", this))
            var previous: Any? = null
            desc.subs.forEach { leaf ->
                val it = leaf as ClassField
                if ((it.value == null || it.value == NotDefined) && previous != null) {
                    it.value = previous!! as Int + 1;
                }

                if (it.value != null) {
                    previous = it.value
                }

                classDefinition.append(fileGenerator.tabSpace);
                classDefinition.append("const val ");
                classDefinition.append(it.name);
                classDefinition.append(" : ${Types.typeTo(file, it.type)}")
                classDefinition.append(" = ${Types.toValue(this, it.type, it.value)}")
                classDefinition.append(fileGenerator.newLine())
            }
            appendNotEmptyWithNewLine("}", classDefinition)
        }
        return result
    }
}