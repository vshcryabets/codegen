package generators.obj.out

import generators.obj.input.Leaf
import generators.obj.input.Node

// package $name
class NamespaceDeclaration(name : String) : Leaf(name)
class NamespaceBlock(name : String) : Node(name, null)
//import $name
class ImportLeaf(name : String) : Leaf(name)

// //$name
class CommentLeaf(name : String) : Leaf(name)


//class BlockStart(name : String) : Leaf(name, null)
//class BlockEnd(name : String, parent : Node) : Leaf(name, parent)

class ArgumentLeaf(name : String) : Leaf(name)
class ResultLeaf(name : String) : Leaf(name)
class FieldLeaf(name : String, parent : Node) : Leaf(name, parent)
class ConstantLeaf() : Node("", null)
class EnumLeaf(name : String) : Leaf(name)

class RValue(name : String) : Leaf(name) // something after "="
class Keyword(name : String) : Leaf(name)
class VariableName(name : String) : Leaf(name)

// for example ",\n"
class Separator(name : String) : Leaf(name)
class NlSeparator(name : String = "") : Leaf(name)
class BlockPreNewLines : Leaf("")