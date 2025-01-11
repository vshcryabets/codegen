package ce.formatters

import generators.obj.input.Leaf
import generators.obj.input.Node
import generators.obj.input.addSub
import generators.obj.out.ArgumentNode
import generators.obj.out.Datatype
import generators.obj.out.Keyword
import generators.obj.out.NlSeparator
import generators.obj.out.RValue
import generators.obj.out.Separator
import generators.obj.out.Space
import generators.obj.out.VariableName
import javax.inject.Inject

class CodeFormatterJavaUseCaseImpl @Inject constructor(codeStyleRepo: CodeStyleRepo) :
    CodeFormatterUseCaseImpl(codeStyleRepo) {

    override fun processArgumentNode(
        input: ArgumentNode,
        outputParent: Node,
        indent: Int,
        prev: Leaf?,
        inputQueue: MutableList<Leaf>
    ): ArgumentNode = formatArgumentNode(input, outputParent, indent, inputQueue.firstOrNull(), prev) as ArgumentNode

    public fun declarationPattern(input: Node): Int {
        if (input.subs.size < 2)
            return -1
        for (pos in 0..input.subs.size-2) {
            val w1 = input.subs[pos] // int
            if (w1 !is Datatype)
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
    ): Node {
        return input.copyLeaf(copySubs = false).apply {
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