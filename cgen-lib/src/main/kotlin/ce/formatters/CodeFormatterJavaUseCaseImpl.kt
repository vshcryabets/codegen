package ce.formatters

import generators.obj.abstractSyntaxTree.Leaf
import generators.obj.abstractSyntaxTree.Node
import generators.obj.abstractSyntaxTree.addSub
import generators.obj.syntaxParseTree.ArgumentNode
import generators.obj.syntaxParseTree.AstTypeLeaf
import generators.obj.syntaxParseTree.NlSeparator
import generators.obj.syntaxParseTree.Separator
import generators.obj.syntaxParseTree.Space
import generators.obj.syntaxParseTree.VariableName
import javax.inject.Inject

class CodeFormatterJavaUseCaseImpl @Inject constructor(codeStyleRepo: CodeStyleRepo) :
    CodeFormatterUseCaseImpl(codeStyleRepo) {

    override fun processArgumentNode(
        input: ArgumentNode,
        outputParent: Node,
        indent: Int,
        prev: Leaf?,
        inputQueue: MutableList<Leaf>
    ) { formatArgumentNode(input, outputParent, indent, inputQueue.firstOrNull(), prev) }

    fun declarationPattern(input: Node): Int {
        if (input.subs.size < 2)
            return -1
        for (pos in 0..input.subs.size-2) {
            val w1 = input.subs[pos] // int
            if (w1 !is AstTypeLeaf)
                continue
            val w2 = input.subs[pos + 1] // NAME
            if (w2 !is VariableName)
                continue
            return pos
        }
        return -1
    }

    private fun formatArgumentNode(
        input: Node,
        outputParent: Node?,
        indent: Int,
        next: Leaf?,
        prev: Leaf?
    ) {
        input.copyLeaf(copySubs = false).apply {
            if (next is ArgumentNode || prev is ArgumentNode) {
                outputParent?.subs?.addAll(getIndents(indent))
            }
            outputParent?.addSub(this)
            if (next is ArgumentNode) {
                outputParent?.addSub(Separator(","))
                outputParent?.addSub(NlSeparator())
            }
            if (declarationPattern(input) >= 0) {
                for (i in 0..input.subs.size - 1) {
                    val current = input.subs[i]
                    if (i > 0) {
                        addSub(Space())
                    }
                    addSub(current.copyLeaf(this, true))
                }
            } else {
                processSubs(input, this, indent)
            }
        }
    }
}