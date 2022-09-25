package generators.obj.out

import generators.obj.input.Leaf
import generators.obj.input.Node

class NamespaceDeclaration(name : String, parent: OutNode) : Leaf(name, parent)
class ImportLeaf(name : String, parent: OutNode) : Leaf(name, parent)
class CommentLeaf(name : String, parent: Node) : Leaf(name, parent)
class BlockPreNewLines(parent : Node) : Leaf("", parent)
class BlockStart(name : String, parent : Node) : Leaf(name, parent)
class BlockEnd(name : String, parent : Node) : Leaf(name, parent)
class FieldLeaf(name : String, parent : Node) : Leaf(name, parent)