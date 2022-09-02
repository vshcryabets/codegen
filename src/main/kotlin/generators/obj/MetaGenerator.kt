package generators.obj

import ce.defs.Target
import generators.obj.input.ClassDescription
import generators.obj.input.ConstantsBlock
import generators.obj.input.ConstantsEnum
import generators.obj.out.ClassData

class MetaGenerator<T : ClassData>(
    val target: Target = Target.Other,
    val enum : Generator<T>,
    val constantsBlock : Generator<T>,
    val writter: Writter<T>
) {

    fun writeItem(item: ClassDescription, baseFolderPath : String) {
        if (item is ConstantsEnum) {
            val classData = enum.build(item)
            if (classData.customBaseFolder.isEmpty()) {
                classData.customBaseFolder = baseFolderPath
            }
            writter.write(classData)
        }
        if (item is ConstantsBlock) {
            val classData = constantsBlock.build(item)
            if (classData.customBaseFolder.isEmpty()) {
                classData.customBaseFolder = baseFolderPath
            }
            writter.write(classData)
        }
    }
}