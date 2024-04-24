package ce.formatters

import generators.obj.input.Leaf
import generators.obj.input.Node
import generators.obj.input.addSeparator
import generators.obj.input.addSub
import generators.obj.out.EnumNode
import generators.obj.out.Separator
import javax.inject.Inject

class CodeFormatterCxxUseCaseImpl @Inject constructor(codeStyleRepo: CodeStyleRepo) :
    CodeFormatterUseCaseImpl(codeStyleRepo) {

    override fun processLeaf(inputQueue: MutableList<Leaf>, outputParent: Node, indent: Int) {
        val nodesToAdd = mutableListOf<Leaf>()
        val input = inputQueue.first()
        if (input is EnumNode) {
            nodesToAdd.addAll(getIndents(indent))
            nodesToAdd.add(input.copy())
            nodesToAdd.add(Separator(","))
            nodesToAdd.add(getNewLine())
            nodesToAdd.forEach {
                outputParent.addSub(it)
            }
            inputQueue.removeFirst()
            return
        }
        super.processLeaf(inputQueue, outputParent, indent)
    }

    override fun processSeparatorAfterOutBlock(
        inputQueue: MutableList<Leaf>,
        outputParent: Node
    ) {
        val next = inputQueue.first()
        if (next.name.equals(";")) {
            inputQueue.removeFirst()
            outputParent.addSeparator(";")
        }
    }
}