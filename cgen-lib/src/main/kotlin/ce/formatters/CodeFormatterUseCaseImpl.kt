package ce.formatters

import generators.cpp.CompilerDirective
import generators.obj.input.*
import generators.obj.out.*
import javax.inject.Inject

class CodeFormatterUseCaseImpl @Inject constructor(
    private val codeStyleRepo: CodeStyleRepo,
) : CodeFormatterUseCase {
    override fun invoke(syntaxTree: Node): Leaf {
        return processNode(syntaxTree, null, 0)!!
    }

    private fun processLeaf(input: Leaf, outputParent: Node, indent: Int) {
        val nodesToAdd = mutableListOf<Leaf>()
        when (input) {
            is CommentLeaf -> {
                val leaf = CommentLeaf(codeStyleRepo.singleComment() + input.name)
                nodesToAdd.addAll(getIndents(indent))
                nodesToAdd.add(leaf)
                nodesToAdd.add(getNewLine())
            }

            is CompilerDirective -> {
                val leaf = input.copyLeaf(parent = outputParent)
                nodesToAdd.add(leaf)
                nodesToAdd.add(getNewLine())
            }

            else -> {
                val leaf = input.copyLeaf(outputParent)
                nodesToAdd.add(leaf)
            }
        }
        nodesToAdd.forEach {
            outputParent.addSub(it)
        }
    }

    private fun getNewLine(): Leaf = NlSeparator(codeStyleRepo.newLine())

    private fun processNode(input: Node, outputParent: Node?, indent: Int): Leaf? {
        if (input is FileData) {
            if (!input.isDirty) {
                return null
            }
        }
        if ((input is Region) or (input is NamespaceBlock)) {
            if (codeStyleRepo.addSpaceBeforeRegion()) {
                outputParent?.addSeparatorNewLine(codeStyleRepo.spaceBeforeClass())
            }
        }

        return when (input) {

            is ConstantLeaf -> {
                formatConstantLeaf(input, outputParent, indent)
            }

            is OutBlock -> {
                (input.copyLeaf(copySubs = false) as OutBlock).apply {
                    outputParent?.addSub(this)
                    addSub(Space())
                    addKeyword("{")
                    addSeparatorNewLine()
                    processSubs(input, this, indent + 1)
                    addKeyword("}")
                    outputParent?.addSeparatorNewLine()
                }
            }

            is NamespaceBlock -> {
                (input.copyLeaf(copySubs = false) as Node).apply {
                    outputParent?.addSub(this)
                    addSub(Space())
                    addKeyword("{")
                    addSeparatorNewLine()
                    processSubs(input, this, indent + 1)
                    addKeyword("}")
                    outputParent?.addSeparatorNewLine()
                }
            }

            is Region -> {
                (input.copyLeaf(copySubs = false) as Region).apply {
                    outputParent?.addSub(this)
                    processSubs(input, this, indent)
                }
            }

            else -> {
                val node = input.copyLeaf(copySubs = false) as Node
                outputParent?.addSub(node)
                processSubs(input, node, indent)
                node
            }
        }
    }

    private fun formatConstantLeaf(input: ConstantLeaf, parent: Node?, indent: Int): Leaf {
        val res = input.copyLeaf(copySubs = false).apply {
            addIndents(parent, indent)
            parent?.addSub(this)
            input.subs.forEach {
                processLeaf(it, this, indent)
                if ((it !is RValue) and (it !is Separator)) {
                    this.addSub(Space())
                }
            }
            parent?.addSeparatorNewLine()
        }
        return res
    }

    private fun addIndents(parent: Node?, indent: Int) {
        if (parent == null) {
            return
        }
        if (indent > 0) {
            parent.subs.addAll(getIndents(indent))
        }
    }

    private fun getIndents(indent: Int): List<Indent> =
        mutableListOf<Indent>().apply {
            (0..indent - 1).forEach { add(Indent()) }
        }


    private fun processSubs(syntaxTree: Node, res: Node, indent: Int) {
        syntaxTree.subs.forEach {
            if (it is Node) {
                processNode(it, res, indent)
            } else {
                processLeaf(it, res, indent)
            }
        }
    }
}