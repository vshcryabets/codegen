package ce.formatters

import generators.obj.input.*
import generators.obj.out.*
import org.gradle.internal.serialization.Transient.Var
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
                    if (w4 !is Datatype)
                        continue
                    return pos
                } else if (w3.name == "=") {
                    if (w4 !is RValue)
                        continue
                    return pos
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