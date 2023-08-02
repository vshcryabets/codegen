package generators.cpp

import generators.obj.input.Leaf
import generators.obj.input.Node
import generators.obj.out.ClassData
import generators.obj.out.FileData


class CompilerDirective(override val name: String, override var parent: Node?) : Leaf
class CppClassData(name: String) : ClassData(name)
class CppScopeGroup(name: String) : ClassData(name)

open class CppHeaderFile(name: String, parent: Node) : FileData(name, parent) {
    init {
        addSub(CompilerDirective("pragma once", this))
        resetDirtyFlag()
    }
}

class CppFileData(
    name: String,
    parent: Node
) : FileData(name, parent)