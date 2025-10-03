package ce.formatters

import ce.defs.RValue
import generators.obj.abstractSyntaxTree.Leaf
import generators.obj.abstractSyntaxTree.Node
import generators.obj.abstractSyntaxTree.addSeparator
import generators.obj.abstractSyntaxTree.addSub
import generators.obj.abstractSyntaxTree.insertSub
import generators.obj.syntaxParseTree.EnumNode
import generators.obj.syntaxParseTree.Keyword
import generators.obj.syntaxParseTree.Separator
import generators.obj.syntaxParseTree.Space
import generators.obj.syntaxParseTree.VariableName
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

    override fun processEnumNode(input: EnumNode, parent: Node?, indent: Int, next: Leaf?, prev: Leaf?): EnumNode {
        val result = super.processEnumNode(input, parent, indent, next, prev)
        // make sure that we have spaces between name, = and rvalue
        if (result.subs.size >= 3) {
            val varName = result.subs[0]
            val eq = result.subs[1]
            val rValue = result.subs[2]
            if (varName is VariableName && eq is Keyword &&  rValue is RValue) {
                // add spaces
                result.insertSub(2, Space())
                result.insertSub(1, Space())
            }
        }
        return result
    }
}