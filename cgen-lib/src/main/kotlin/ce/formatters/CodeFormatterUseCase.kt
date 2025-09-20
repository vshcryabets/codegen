package ce.formatters

import generators.obj.abstractSyntaxTree.Node

interface CodeFormatterUseCase {
    operator fun <T: Node> invoke(input: T): T
}