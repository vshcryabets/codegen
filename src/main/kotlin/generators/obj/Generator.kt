package generators.obj

import ce.settings.CodeStyle
import generators.obj.input.ClassDescription
import generators.obj.out.ClassData

abstract class Generator<T : ClassData >(style : CodeStyle) {
    val tabSpace : String

    abstract fun createClassData() : T

    open fun build(desc: ClassDescription) : T =
        createClassData().apply {
            namespace = desc.namespace
            customBaseFolder = desc.objectBaseFolder
        }

    init {
        val builder = StringBuilder()
        for (a in 0..style.tabSize - 1) {
            builder.append(" ");
        }
        tabSpace = builder.toString()
    }
}