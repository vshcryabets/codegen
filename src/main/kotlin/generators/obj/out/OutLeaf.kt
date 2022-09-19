package generators.obj.out

open class OutLeaf {
}

class NamespaceDeclaration(var name : String) : OutLeaf()
class ImportLeaf(val line : String) : OutLeaf()
class CommentLeaf(val line : String) : OutLeaf()