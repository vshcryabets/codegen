package ce.formatters

import generators.obj.input.Leaf
import generators.obj.input.Node
import generators.obj.input.addSeparatorNewLine
import generators.obj.input.addSub
import generators.obj.out.Region
import generators.obj.out.RegionImpl
import javax.inject.Inject

class CodeFormatterUseCaseImpl @Inject constructor(
    private val codeStyleRepo: CodeStyleRepo
) : CodeFormatterUseCase {
    override fun invoke(syntaxTree: Leaf): Leaf {
        if (syntaxTree is Node) {
            return processNode(syntaxTree, null)
        } else {
            return processLeaf(syntaxTree, null)
        }
    }

    private fun processLeaf(syntaxTree: Leaf, parent: Node?): Leaf {
        return syntaxTree.copyLeaf(null)
    }

    private fun processNode(syntaxTree: Node, parent: Node?): Leaf {
        val res = when (syntaxTree) {
            is Region -> {
                parent?.addSeparatorNewLine(codeStyleRepo.spaceBeforeClass())
                syntaxTree.copyLeaf(null)
            }

            else -> syntaxTree.copyLeaf(null)
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