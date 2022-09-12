package generators.kotlin

import ce.settings.Project
import generators.obj.Generator
import generators.obj.input.ConstantsEnum
import generators.obj.out.FileData

class KotlinEnumGenerator(
    fileGenerator: KotlinFileGenerator,
    private val project: Project
) : Generator<ConstantsEnum, KotlinClassData>(fileGenerator) {

    override fun processBlock(file: FileData, desc: ConstantsEnum): KotlinClassData {
        val result = super.processBlock(file, desc)
        result.apply {
//            headers.append("package $namespace\n");

            if (desc.classComment.isNotEmpty()) {
                appendClassDefinition(result, "/**")
                desc.classComment.lines().forEach { line ->
                    appendClassDefinition(result, "* $line")
                }
                appendClassDefinition(result, "*/")
            }
            appendClassDefinition(result, "object ${desc.name} {");
            file.currentTabLevel++
            var previous: Any? = null
            desc.constants.forEach {
                if (it.value == null && previous != null) {
                    it.value = previous!! as Int + 1;
                }

                if (it.value != null) {
                    previous = it.value
                }

                putTabs(classDefinition, file.currentTabLevel)
                classDefinition.append("const val ");
                classDefinition.append(it.name);
                classDefinition.append(" : ${Types.typeTo(file, it.type)}")
                classDefinition.append(" = ${Types.toValue(this, it.type, it.value)}")
                classDefinition.append('\n')
            }
            file.currentTabLevel--
            appendClassDefinition(result, "}");
        }
        return result
    }

    override fun createClassData(namespace: String): KotlinClassData = KotlinClassData(namespace)
}