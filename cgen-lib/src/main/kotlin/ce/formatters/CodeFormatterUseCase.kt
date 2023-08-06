package ce.formatters

import generators.obj.input.Leaf
import generators.obj.input.Node

interface CodeFormatterUseCase {
    operator fun <T: Node> invoke(input: T): T
}