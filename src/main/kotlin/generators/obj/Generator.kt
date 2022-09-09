package generators.obj

import generators.obj.input.Block
import generators.obj.out.ClassData
import generators.obj.out.FileData

abstract class Generator<I: Block, T : ClassData >(val fileGenerator: FileGenerator) {

    abstract fun createClassData() : T

    open fun buildBlock(file: FileData, desc: I) : T =  createClassData()

    fun putTabs(builder: StringBuilder, count : Int) {
        for (i in 0 .. count - 1) {
            builder.append(fileGenerator.tabSpace)
        }
    }

    fun appendClassDefinition(file: FileData, outputClassData: ClassData, s: String) {
        outputClassData.apply {
            putTabs(classDefinition, file.currentTabLevel)
            classDefinition.append(s)
            classDefinition.append('\n')
        }
    }

}