package generators.obj

import generators.obj.input.Block
import generators.obj.out.ClassData
import generators.obj.out.FileData

abstract class Generator<I: Block, T : ClassData >(val fileGenerator: FileGenerator) {

    abstract fun processBlock(file: FileData, desc: I) : T

    fun putTabs(builder: StringBuilder, count : Int) {
        for (i in 0 .. count - 1) {
            builder.append(fileGenerator.tabSpace)
        }
    }

    fun appendNotEmptyWithNewLine(str: StringBuilder, builder: StringBuilder) {
        appendNotEmptyWithNewLine(str.toString(), builder)
    }
    fun appendNotEmptyWithNewLine(str: String, builder: StringBuilder) {
        if (str.isNotEmpty()) {
            builder.append(str).append(fileGenerator.newLine())
        }
    }

    fun appendClassDefinition(outputClassData: ClassData, s: String) {
        outputClassData.apply {
            classDefinition.append(s)
            classDefinition.append(fileGenerator.newLine())
        }
    }

}