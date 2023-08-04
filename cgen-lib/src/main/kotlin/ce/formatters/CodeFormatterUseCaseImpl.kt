package ce.formatters

import generators.obj.input.Leaf
import generators.obj.input.Node
import generators.obj.input.addSeparatorNewLine
import generators.obj.input.addSub
import generators.obj.out.CommentLeaf
import generators.obj.out.Region
import javax.inject.Inject

class CodeFormatterUseCaseImpl @Inject constructor(
    private val codeStyleRepo: CodeStyleRepo
) : CodeFormatterUseCase {
    override fun invoke(syntaxTree: Leaf): Leaf {
        var res = if (syntaxTree is Node) {
            processNode(syntaxTree, null)
        } else {
            processLeaf(syntaxTree, null)
        }
        return res
    }

    private fun processLeaf(syntaxTree: Leaf, parent: Node?): Leaf {
        val res = when (syntaxTree) {
            is CommentLeaf -> CommentLeaf(codeStyleRepo.singleComment() + syntaxTree.name)
            else -> syntaxTree.copyLeaf(parent)
        }
        return res
    }

    private fun processNode(syntaxTree: Node, parent: Node?): Leaf {
        val res = when (syntaxTree) {
            is Region -> {
                parent?.addSeparatorNewLine(codeStyleRepo.spaceBeforeClass())
                syntaxTree.copyLeaf(copySubs = false)
            }

            else -> syntaxTree.copyLeaf(copySubs = false)
        } as Node
        syntaxTree.subs.forEach {
            res.addSub(
                if (it is Node) {
                    processNode(it, res)
                } else {
                    processLeaf(it, res)
                }
            )
        }
        return res
    }
}