package generators.obj

import ce.settings.CodeStyle
import generators.obj.input.Block
import generators.obj.out.ClassData
import generators.obj.out.FileData
import generators.obj.out.ProjectOutput

abstract class Generator<I: Block, T : ClassData >(style : CodeStyle) {
    val tabSpace : String

    abstract fun createClassData() : T

    open fun buildBlock(file: FileData, desc: I) : T =
        createClassData().apply {
//            namespace = desc.namespace
//            fileFolder = desc.objectBaseFolder
        }

    init {
        val builder = StringBuilder()
        for (a in 0..style.tabSize - 1) {
            builder.append(" ");
        }
        tabSpace = builder.toString()
    }

    fun putTabs(builder: StringBuilder, count : Int) {
        for (i in 0 .. count - 1) {
            builder.append(tabSpace)
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