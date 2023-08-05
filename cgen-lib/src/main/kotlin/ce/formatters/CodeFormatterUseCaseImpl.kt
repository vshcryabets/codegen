package ce.formatters

import generators.obj.input.Leaf
import generators.obj.input.Node
import generators.obj.input.addSeparatorNewLine
import generators.obj.input.addSub
import generators.obj.out.CommentLeaf
import generators.obj.out.NlSeparator
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
        val nodesToAdd = mutableListOf<Leaf>()
        val res = when (syntaxTree) {
            is CommentLeaf -> {
                val leaf = CommentLeaf(codeStyleRepo.singleComment() + syntaxTree.name)
                nodesToAdd.add(leaf)
                nodesToAdd.add(NlSeparator())
                leaf
            }

            else -> {
                val leaf = syntaxTree.copyLeaf(parent)
                nodesToAdd.add(leaf)
                leaf
            }
        }
        parent?.apply {
            nodesToAdd.forEach {
                addSub(it)
            }
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
            if (it is Node) {
                res.addSub(processNode(it, res))
            } else {
                processLeaf(it, res)
            }
        }
        return res
    }
}