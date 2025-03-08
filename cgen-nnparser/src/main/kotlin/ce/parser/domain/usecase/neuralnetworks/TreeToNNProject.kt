package ce.parser.domain.usecase.neuralnetworks

import ce.parser.domain.FormatProject
import generators.obj.input.Leaf

interface TreeToFormatProject {
    fun addTree(tree: Leaf,
              project: FormatProject) : FormatProject

}