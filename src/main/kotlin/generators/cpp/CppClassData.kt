package generators.cpp

import generators.obj.input.Leaf
import generators.obj.input.Node
import generators.obj.out.ClassData
import generators.obj.out.FileData


class CompilerDirective(name: String, parent: Node) : Leaf(name, parent)
class CppClassData(name: String, parent: Node) : ClassData(name, parent)

open class CppHeaderFile(name: String, parent: Node) : FileData(name, parent) {
    init {
        addSub(CompilerDirective("pragma once", this))
    }
}

class CppFileData(
    name: String,
    parent: Node
) : FileData(name, parent)