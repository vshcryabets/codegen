package generators.obj.out

import generators.obj.input.Leaf
import generators.obj.input.Node

// package $name
class NamespaceDeclaration(name : String, parent: Node) : Leaf(name, parent)

//import $name
class ImportLeaf(name : String, parent: Node) : Leaf(name, parent)

// //$name
class CommentLeaf(name : String, parent: Node) : Leaf(name, parent)


class BlockStart(name : String, parent : Node) : Leaf(name, parent)
class BlockEnd(name : String, parent : Node) : Leaf(name, parent)

class FieldLeaf(name : String, parent : Node) : Leaf(name, parent)
class ConstantLeaf(name : String, parent : Node) : Leaf(name, parent)
class EnumLeaf(name : String, parent : Node) : Leaf(name, parent)

// for example ",\n"
class Separator(name : String, parent : Node) : Leaf(name, parent)
class NlSeparator(parent : Node) : Leaf("", parent)
class BlockPreNewLines(parent : Node) : Leaf("", parent)