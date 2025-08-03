package ce.formatters

import ce.defs.DataValue
import ce.defs.RValue
import generators.obj.input.Leaf
import generators.obj.input.Node
import generators.obj.input.addKeyword
import generators.obj.input.addSeparatorNewLine
import generators.obj.input.addSub
import generators.obj.out.ArgumentNode
import generators.obj.out.Arguments
import generators.obj.out.AstTypeLeaf
import generators.obj.out.EnumNode
import generators.obj.out.FieldNode
import generators.obj.out.Keyword
import generators.obj.out.NamespaceDeclaration
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
    ) {
        formatArgumentNode(
            input, outputParent,
            indent = indent,
            next = inputQueue.firstOrNull(), prev
        )
    }

    override fun processOutBlock(
        input: OutBlock,
        outputParent: Node,
        indent: Int,
        prev: Leaf?,
        inputQueue: MutableList<Leaf>
    ): OutBlock {
        return input.copyLeaf(copySubs = false).apply {
            addIndents(outputParent, indent)
            outputParent.addSub(this)
            // find out block args
            val outBlockArgs = input.subs.findLast {
                it is OutBlockArguments
            } as OutBlockArguments?
            if (outBlockArgs != null) {
                input.subs.remove(outBlockArgs)
                processArguments(
                    input = outBlockArgs,
                    parent = this,
                    indent = indent
                )
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

    override fun processFieldNode(
        input: FieldNode,
        parent: Node,
        indent: Int,
        next: Leaf?,
        prev: Leaf?
    ) {
        addIndents(parent, indent)
        formatArgumentNode(input, parent, indent, next, prev)
        parent.addSeparatorNewLine()
    }

    fun declarationPattern(input: Node): Int {
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
        outputParent: Node,
        indent: Int,
        next: Leaf?,
        prev: Leaf?
    ) {
        input.copyLeaf(copySubs = false).apply {
            if (next is ArgumentNode || prev is ArgumentNode) {
                outputParent.subs.addAll(getIndents(indent))
            }
            outputParent.addSub(this)
            if (next is ArgumentNode) {
                outputParent.addSub(Separator(","))
                outputParent.addSub(NlSeparator())
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
                            // no SP between <NAME> and <:>
                            addSub(Space())
                    }
                    if (current is RValue) {
                        processNode(inputQueue = mutableListOf(current), this, indent, null)
                    } else {
                        addSub(current.copyLeaf(this, true))
                    }
                }
            } else {
                processSubs(input, this, indent)
            }
        }
    }

    override fun processEnumNodeArguments(
        input: EnumNode,
        arguments: Arguments,
        output: EnumNode,
        indent: Int,
    ) {
        input.subs.remove(arguments)
        processArguments(
            input = arguments,
            parent = output,
            indent = indent
        )
    }

    override fun processNamespaceDeclaration(
        input: NamespaceDeclaration,
        outputParent: Node,
        indent: Int
    ) {
        val node = input.copyLeaf(copySubs = false) as Node
        outputParent.addSub(node)
        processSubs(input as Node, node, indent)
        // add spaces
        val newSubs = node.subs.toList()
        node.subs.clear()

        newSubs.forEachIndexed { index, leaf ->
            if (index > 0) {
                // add space before every leaf except first
                node.addSub(Space())
            }
            node.addSub(leaf)
        }
        // after kotlin package declaration we should add two newline
        node.addSub(NlSeparator())
        node.addSub(NlSeparator())
    }

}