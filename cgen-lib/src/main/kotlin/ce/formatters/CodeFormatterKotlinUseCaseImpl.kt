package ce.formatters

import generators.obj.input.*
import generators.obj.out.*
import javax.inject.Inject

class CodeFormatterKotlinUseCaseImpl @Inject constructor(codeStyleRepo: CodeStyleRepo) :
    CodeFormatterUseCaseImpl(codeStyleRepo) {

    override fun processNode(
        inputQueue: MutableList<Leaf>,
        outputParent: Node?,
        indent: Int,
        prev: Leaf?
    ): Node? {
        val input = inputQueue.first()
        return when (input) {
            is OutBlockArguments -> {
                if (input.subs.size > 1) {
                    inputQueue.removeFirst()
                    // multiple lines
                    input.copyLeaf(copySubs = false).apply {
                        outputParent?.addSub(NlSeparator())
                        outputParent?.addSub(this)
                        outputParent?.addSub(NlSeparator())
                        processSubs(input, this, indent + 1)
                    }
                } else {
                    // single argument
                    super.processNode(inputQueue, outputParent, indent, prev)
                }
            }
            is OutBlock -> {
                inputQueue.removeFirst()
                input.copyLeaf(copySubs = false).apply {
                    outputParent?.addSub(this)
                    // find out block args
                    val args = input.subs.findLast {
                        it is OutBlockArguments
                    } as OutBlockArguments?
                    if (args != null) {
                        input.subs.remove(args)
                        addKeyword("(")
                        processNode(mutableListOf(args), this, indent, null)
                        addKeyword(")")
                    }
                    if (!codeStyleRepo.preventEmptyBlocks || input.subs.isNotEmpty()) {
                        addSub(Space())
                        addKeyword("{")
                        addSeparatorNewLine()
                        processSubs(input, this, indent + 1)
                        addKeyword("}")
                    }
                    outputParent?.addSeparatorNewLine()
                }
            }

            is ArgumentNode -> {
                inputQueue.removeFirst()
                val next = inputQueue.firstOrNull()
                formatArgumentNode(input, outputParent, indent, next, prev)
            }

            else -> super.processNode(inputQueue, outputParent, indent, prev)
        }
    }

    override fun formatConstantNode(
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
        for (pos in 0..input.subs.size-1-4) {
            val w1 = input.subs[pos]
            if (w1 !is Keyword)
                continue
            val w2 = input.subs[pos + 1]
            val w3 = input.subs[pos + 2]
            val w4 = input.subs[pos + 3]

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
            // <val><NAME><:><int>
            if ((input.subs.size == 4) &&
                (input.subs[0] is Keyword) &&
                (input.subs[1] is VariableName) &&
                (input.subs[2] is Keyword) &&
                (input.subs[3] is Datatype)
            ) {
                // <val><SPACE><NAME><:><SPACE><int>
                addSub(input.subs[0].copyLeaf(this, true))
                addSub(Space())
                addSub(input.subs[1].copyLeaf(this, true))
                addSub(input.subs[2].copyLeaf(this, true))
                addSub(Space())
                addSub(input.subs[3].copyLeaf(this, true))
            } else if ((input.subs.size == 6) && // <val><NAME><:><int><=><RValue>
                (input.subs[0] is Keyword) &&
                (input.subs[1] is VariableName) &&
                (input.subs[2] is Keyword) &&
                (input.subs[3] is Datatype) &&
                (input.subs[4] is Keyword) &&
                (input.subs[5] is RValue)
            ) {
                // <val><SPACE><NAME><:><SPACE><int><SPACE><=><SPACE><RValue>
                addSub(input.subs[0].copyLeaf(this, true))
                addSub(Space())
                addSub(input.subs[1].copyLeaf(this, true))
                addSub(input.subs[2].copyLeaf(this, true))
                addSub(Space())
                addSub(input.subs[3].copyLeaf(this, true))
                addSub(Space())
                addSub(input.subs[4].copyLeaf(this, true))
                addSub(Space())
                addSub(input.subs[5].copyLeaf(this, true))
            } else {
                processSubs(input, this, indent)
            }
        }
    }
}