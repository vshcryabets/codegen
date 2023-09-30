package ce.formatters

import generators.obj.input.*
import generators.obj.out.*
import org.apache.tools.ant.types.DataType
import javax.inject.Inject

class CodeFormatterKotlinUseCaseImpl @Inject constructor(codeStyleRepo: CodeStyleRepo) :
    CodeFormatterUseCaseImpl(codeStyleRepo) {

    override fun processLeaf(input: Leaf, outputParent: Node, indent: Int) {
        super.processLeaf(input, outputParent, indent)
    }

    override fun processNode(input: Node, outputParent: Node?, indent: Int, next: Leaf?, prev: Leaf?): Node? {
        return when (input) {
            is OutBlockArguments -> {
                if (input.subs.size > 1) {
                    // multiple lines
                    input.copyLeaf(copySubs = false).apply {
                        outputParent?.addSub(NlSeparator())
                        outputParent?.addSub(this)
                        outputParent?.addSub(NlSeparator())
                        processSubs(input, this, indent + 1)
                    }
                } else {
                    // single argument
                    super.processNode(input, outputParent, indent, next, prev)
                }
            }
            is OutBlock -> {
                input.copyLeaf(copySubs = false).apply {
                    outputParent?.addSub(this)
                    // find out block args
                    val args = input.subs.findLast {
                        it is OutBlockArguments
                    } as OutBlockArguments?
                    if (args != null) {
                        input.subs.remove(args)
                        addKeyword("(")
                        processNode(args, this, indent, null, null)
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

            is ArgumentNode -> formatArgumentNode(input, outputParent, indent, next, prev)
            else -> super.processNode(input, outputParent, indent, next, prev)
        }
    }

    private fun formatArgumentNode(input: ArgumentNode, outputParent: Node?, indent: Int, next: Leaf?, prev: Leaf?): Node {
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