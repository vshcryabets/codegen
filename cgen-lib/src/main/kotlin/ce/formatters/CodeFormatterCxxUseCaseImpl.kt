package ce.formatters

import generators.obj.input.*
import generators.obj.out.*
import javax.inject.Inject

class CodeFormatterCxxUseCaseImpl @Inject constructor(codeStyleRepo: CodeStyleRepo) :
    CodeFormatterUseCaseImpl(codeStyleRepo) {

    override fun processLeaf(input: Leaf, outputParent: Node, indent: Int) {
        val nodesToAdd = mutableListOf<Leaf>()
        if (input is EnumNode) {
            nodesToAdd.addAll(getIndents(indent))
            nodesToAdd.add(input.copy())
            nodesToAdd.add(Separator(","))
            nodesToAdd.add(getNewLine())
            nodesToAdd.forEach {
                outputParent.addSub(it)
            }
            return
        }
        super.processLeaf(input, outputParent, indent)
    }

    override fun processNode(input: Node, outputParent: Node?, indent: Int, next: Leaf?, prev: Leaf?): Node? {
        return when (input) {
//            is OutBlock -> {
//                (input.copyLeaf(copySubs = false) as OutBlock).apply {
//                    outputParent?.addSub(this)
//                    // find out block args
//                    val args = input.subs.findLast {
//                        it is OutBlockArguments
//                    }
//                    if (args != null) {
//                        input.subs.remove(args)
//                        addKeyword("(")
//                        addSub(args)
//                        addKeyword(")")
//                    }
//                    addSub(Space())
//                    addKeyword("{")
//                    addSeparatorNewLine()
//                    processSubs(input, this, indent + 1)
//                    addKeyword("}")
//                    outputParent?.addSeparatorNewLine()
//                }
//            }
            else -> super.processNode(input, outputParent, indent, next, prev)
        }
    }
}