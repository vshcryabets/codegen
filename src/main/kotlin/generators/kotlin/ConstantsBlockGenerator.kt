package generators.kotlin

import ce.settings.Project
import generators.obj.FileGenerator
import generators.obj.Generator
import generators.obj.input.ConstantsBlock
import generators.obj.out.FileData
import javax.xml.stream.events.Namespace

class ConstantsBlockGenerator(
    fileGenerator : FileGenerator,
    private val project: Project
) : Generator<ConstantsBlock, KotlinClassData>(fileGenerator) {

    override fun processBlock(file: FileData, desc: ConstantsBlock): KotlinClassData {
        val result = super.processBlock(file, desc)
        result.apply {
            if (desc.classComment.isNotEmpty()) {
                classDefinition.append(fileGenerator.multilineCommentStart())
                desc.classComment.lines().forEach { line ->
                    classDefinition.append("* $line")
                    classDefinition.append(fileGenerator.newLine())
                }
                classDefinition.append(fileGenerator.multilineCommentEnd())
            }
            classDefinition.append("object ${desc.name} {")
            classDefinition.append(fileGenerator.newLine())
            var previous: Any? = null
            desc.constants.forEach {
                if (it.value == null && previous != null) {
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
            classDefinition.append("}\n");
        }
        return result
    }

    override fun createClassData(namespace: String): KotlinClassData = KotlinClassData(namespace)
}