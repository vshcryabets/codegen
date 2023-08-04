package ce.formatters

import generators.obj.input.Leaf
import generators.obj.input.Node
import generators.obj.input.addSeparatorNewLine
import generators.obj.out.Region
import javax.inject.Inject

class CodeFormatterUseCaseImpl @Inject constructor(
    private val codeStyleRepo: CodeStyleRepo
) : CodeFormatterUseCase {
    override fun invoke(syntaxTree: Leaf): Leaf {
        val result = syntaxTree
        val stack = mutableListOf<Pair<List<Leaf>, Int>>()
        var currentInLeaf = syntaxTree
        var currentOutLeaf: Leaf? = null
        var currentSubs = emptyList<Leaf>()

        when (currentInLeaf) {
            is Region -> {
                currentOutLeaf = Region(currentInLeaf.name).apply {
                    addSeparatorNewLine(codeStyleRepo.spaceBeforeClass())
                }
            }
        }
        if (currentInLeaf is Node) {
            currentSubs = currentInLeaf.subs
        }
        currentSubs.forEach {

        }

        return currentOutLeaf!!
    }
}