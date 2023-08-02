package generators.obj.out

import generators.obj.input.Leaf
import generators.obj.input.Node

// package $name
class NamespaceDeclaration(override val name: String, override var parent: Node? = null) : Leaf
class NamespaceBlock(name : String) : Node(name, null)
//import $name
class ImportLeaf(override val name: String, override var parent: Node? = null) : Leaf

// //$name
class CommentLeaf(override val name: String, override var parent: Node? = null) : Leaf


//class BlockStart(name : String) : Leaf(name, null)
//class BlockEnd(name : String, parent : Node) : Leaf(name, parent)

class ArgumentLeaf(override val name: String, override var parent: Node? = null) : Leaf
class ResultLeaf(override val name: String, override var parent: Node? = null) : Leaf
class FieldLeaf(override val name: String, override var parent: Node? = null) : Leaf
class ConstantLeaf(fnc: ConstantLeaf.()->Unit) : Node("", null) {
    init {
        fnc(this)
    }
}
class EnumLeaf(override val name: String, override var parent: Node? = null) : Leaf

class RValue(override val name: String, override var parent: Node? = null) : Leaf // something after "="
class Keyword(override val name: String, override var parent: Node? = null) : Leaf
class Datatype(override val name: String, override var parent: Node? = null) : Leaf
class VariableName(override val name: String, override var parent: Node? = null) : Leaf

// for example ",\n"
class Separator(override val name: String, override var parent: Node? = null) : Leaf
class NlSeparator(override val name: String = "", override var parent: Node? = null) : Leaf
class BlockPreNewLines(override val name: String = "", override var parent: Node? = null) : Leaf