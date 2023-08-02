package ce.formatters

import generators.obj.input.Leaf

interface CodeFormatterUseCase {
    operator fun invoke(syntaxTree: Leaf): Leaf
}