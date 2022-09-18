package generators.cpp

import ce.settings.Project
import generators.obj.FileGenerator
import generators.obj.Generator
import generators.obj.input.ClassField
import generators.obj.input.ConstantsEnum
import generators.obj.out.FileData

class CppEnumGenerator(
    fileGenerator: FileGenerator,
    private val project: Project
) : Generator<ConstantsEnum, CppClassData>(fileGenerator) {

    override fun processBlock(file: FileData, desc: ConstantsEnum): CppClassData {
        val result = super.processBlock(file, desc)
        result.headerData.apply {
            if (desc.classComment.isNotEmpty()) {
                desc.classComment.lines().forEach { line ->
                    classComment.append("* ").append(line).append(fileGenerator.newLine())
                }
            }
            classDefinition.append("enum ${desc.name} {").append(fileGenerator.newLine())
            var previous: Any? = null
            desc.leafs.forEach { leaf ->
                val it = leaf as ClassField
                if (it.value == null && previous != null) {
                    it.value = previous!! as Int + 1;
                }

                if (it.value != null) {
                    previous = it.value
                }
                putTabs(classDefinition, 1)
                classDefinition.append(it.name);
                classDefinition.append(" = ${Types.toValue(this, it.type, it.value)},")
                classDefinition.append('\n')
            }
            appendClassDefinition(this, "};");

        }
        return result
    }

    override fun createClassData(namespace : String): CppClassData = CppClassData(namespace)
}