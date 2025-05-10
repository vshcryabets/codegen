package ce.formatters

import ce.defs.DataValue
import generators.obj.input.Leaf
import generators.obj.input.Node
import generators.obj.input.addKeyword
import generators.obj.input.addSeparatorNewLine
import generators.obj.input.addSub
import generators.obj.out.ArgumentNode
import generators.obj.out.ConstantNode
import generators.obj.out.AstTypeLeaf
import generators.obj.out.Keyword
import generators.obj.out.NlSeparator
import generators.obj.out.OutBlock
import generators.obj.out.OutBlockArguments
import generators.obj.out.Separator
import generators.obj.out.Space
import generators.obj.out.VariableName
import javax.inject.Inject

class CodeFormatterKotlinUseCaseImpl @Inject constructor(codeStyleRepo: CodeStyleRepo) :
    CodeFormatterUseCaseImpl(codeStyleRepo) {

    override fun processArgumentNode(
        input: ArgumentNode,
        outputParent: Node,
        indent: Int,
        prev: Leaf?,
        inputQueue: MutableList<Leaf>
    ): ArgumentNode = formatArgumentNode(input, outputParent, indent, inputQueue.firstOrNull(), prev) as ArgumentNode

    override fun processOutBlock(
        input: OutBlock,
        outputParent: Node,
        indent: Int,
        prev: Leaf?,
        inputQueue: MutableList<Leaf>
    ): OutBlock {
        return input.copyLeaf(copySubs = false).apply {
            outputParent.addSub(this)
            // find out block args
            val outBlockArgs = input.subs.findLast {
                it is OutBlockArguments
            } as OutBlockArguments?
            if (outBlockArgs != null) {
                input.subs.remove(outBlockArgs)
                addKeyword("(")
                processNode(mutableListOf(outBlockArgs), this, indent, null)
                addKeyword(")")
            }
            if (!codeStyleRepo.preventEmptyBlocks || input.subs.isNotEmpty()) {
                addSub(Space())
                addKeyword("{")
                addSeparatorNewLine()
                processSubs(input, this, indent + 1)
                addKeyword("}")
            }
            outputParent.addSeparatorNewLine()
        }
    }

    override fun processConstantNode(
        input: ConstantNode,
        parent: Node?,
        indent: Int,
        next: Leaf?,
        prev: Leaf?
    ): ConstantNode {
        addIndents(parent, indent)
        val result = formatArgumentNode(input, parent, indent, next, prev) as ConstantNode
        parent?.addSeparatorNewLine()
        return result
    }

    public fun declarationPattern(input: Node): Int {
        if (input.subs.size < 4)
            return -1
        for (pos in 0..input.subs.size-4) {
            val w1 = input.subs[pos] // val/var
            if (w1 !is Keyword)
                continue
            val w2 = input.subs[pos + 1] // NAME
            if (w2 !is VariableName)
                continue
            val w3 = input.subs[pos + 2] // : or =
            val w4 = input.subs[pos + 3] // Int or 10
            if (w3 is Keyword) {
                if (w3.name == ":") {
                    if (w4 !is AstTypeLeaf)
                        continue
                    return pos
                } else if (w3.name == "=") {
                    if (w4 is DataValue)
                        return pos
                    continue
                }
            }
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
                // <val><NAME><:><int> to
                // <val><SPACE><NAME><:><SPACE><int>
                // <val><NAME><:><int><=><RValue> to
                // <val><SPACE><NAME><:><SPACE><int><SPACE><=><SPACE><RValue>
                for (i in 0..input.subs.size - 1) {
                    val current = input.subs[i]
                    if (i > 0) {
                        if (input.subs[i-1] !is VariableName || !current.name.equals(":"))
                            // no SP between <ANME> and <:>
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