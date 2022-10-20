package generators.obj.out

import generators.obj.input.Leaf
import generators.obj.input.Node

// package $name
class NamespaceDeclaration(name : String, parent: Node) : Leaf(name, parent)

//import $name
class ImportLeaf(name : String, parent: Node) : Leaf(name, parent)

// //$name
class CommentLeaf(name : String) : Leaf(name, null)


class BlockStart(name : String, parent : Node) : Leaf(name, parent)
class BlockEnd(name : String, parent : Node) : Leaf(name, parent)

class ArgumentLeaf(name : String) : Leaf(name, null)
class ResultLeaf(name : String) : Leaf(name, null)
class FieldLeaf(name : String, parent : Node) : Leaf(name, parent)
class ConstantLeaf(name : String) : Leaf(name, null)
class EnumLeaf(name : String, parent : Node) : Leaf(name, parent)

// for example ",\n"
class Separator(name : String) : Leaf(name, null)
class NlSeparator : Leaf("", null)
class BlockPreNewLines : Leaf("", null)