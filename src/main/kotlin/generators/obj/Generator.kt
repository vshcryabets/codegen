package generators.obj

import ce.settings.CodeStyle
import generators.kotlin.ClassData
import generators.obj.input.ClassDescription

abstract class Generator(style : CodeStyle) {
    val tabSpace : String

    abstract fun build(desc: ClassDescription) : ClassData

    init {
        val builder = StringBuilder()
        for (a in 0..style.tabSize - 1) {
            builder.append(" ");
        }
        tabSpace = builder.toString()
    }
}