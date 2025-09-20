package ce.parser.domain.usecase.neuralnetworks

import ce.parser.domain.FormatProject
import generators.obj.abstractSyntaxTree.Leaf

interface TreeToFormatProject {
    fun addTree(tree: Leaf,
                project: FormatProject) : FormatProject

}