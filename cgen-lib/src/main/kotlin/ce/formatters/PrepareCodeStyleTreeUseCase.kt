package ce.formatters

import generators.obj.abstractSyntaxTree.addSub
import generators.obj.syntaxParseTree.CodeStyleOutputTree
import generators.obj.syntaxParseTree.OutputTree

interface PrepareCodeStyleTreeUseCase {
    fun prepareCodeStyleTree(projectOutput: OutputTree): CodeStyleOutputTree
}

class PrepareCodeStyleTreeUseCaseImpl(
    private val codeFormatter: CodeFormatterUseCase,
): PrepareCodeStyleTreeUseCase {
    override fun prepareCodeStyleTree(projectOutput: OutputTree): CodeStyleOutputTree {
        val tree = codeFormatter(projectOutput)
        val result = CodeStyleOutputTree(
            target = projectOutput.target
        )
        tree.subs.forEach {
            result.addSub(it)
        }
        return result
    }
}