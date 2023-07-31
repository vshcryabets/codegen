package ce.formatters

import generators.obj.out.ProjectOutput

interface CodeFormatterUseCase {
    operator fun invoke(syntaxTree: ProjectOutput): ProjectOutput
}