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

    override fun processNode(input: Node, outputParent: Node?, indent: Int, next: Leaf?): Node? {
        return when (input) {
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
//                        addSub(args)

                        processNode(args, this, indent, null)

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
                input.copyLeaf(copySubs = false).apply {
                    outputParent?.addSub(this)
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

            else -> super.processNode(input, outputParent, indent, next)
        }
    }
}