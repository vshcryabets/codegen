package generators.obj.out

import generators.obj.input.Leaf

class NamespaceDeclaration(name : String, parent: OutNode) : Leaf(name, parent)
class ImportLeaf(name : String, parent: OutNode) : Leaf(name, parent)
class CommentLeaf(name : String, parent: OutNode) : Leaf(name, parent)