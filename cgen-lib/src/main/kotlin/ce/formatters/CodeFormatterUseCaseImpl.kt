package ce.formatters

import generators.obj.out.ProjectOutput
import javax.inject.Inject

class CodeFormatterUseCaseImpl @Inject constructor(
    private val codeStyleRepo: CodeStyleRepo
) : CodeFormatterUseCase {
    override fun invoke(syntaxTree: ProjectOutput): ProjectOutput {
        val result = ProjectOutput(syntaxTree.target)
        return result
    }
}