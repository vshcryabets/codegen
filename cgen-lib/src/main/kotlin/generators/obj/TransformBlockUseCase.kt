package generators.obj

import generators.obj.abstractSyntaxTree.Block
import generators.obj.syntaxParseTree.FileData

interface TransformBlockUseCase<I: Block> {
    operator fun invoke(blockFiles: List<FileData>, desc: I)
}