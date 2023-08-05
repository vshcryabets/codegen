package ce.formatters

import generators.cpp.CompilerDirective
import generators.obj.input.*
import generators.obj.out.*
import javax.inject.Inject

class CodeFormatterUseCaseImpl @Inject constructor(
    private val codeStyleRepo: CodeStyleRepo
) : CodeFormatterUseCase {
    override fun invoke(syntaxTree: Node): Leaf {
        return processNode(syntaxTree, null, 0)
    }

    private fun processLeaf(input: Leaf, parent: Node?, indent: Int) {
        val nodesToAdd = mutableListOf<Leaf>()
        when (input) {
            is CommentLeaf -> {
                val leaf = CommentLeaf(codeStyleRepo.singleComment() + input.name)
                nodesToAdd.addAll(getIndents(indent))
                nodesToAdd.add(leaf)
                nodesToAdd.add(getNewLine())
                leaf
            }

            is CompilerDirective -> {
                val leaf = input.copyLeaf(parent = parent)
                nodesToAdd.add(leaf)
                nodesToAdd.add(getNewLine())
                leaf
            }

            else -> {
                val leaf = input.copyLeaf(parent)
                nodesToAdd.add(leaf)
                leaf
            }
        }
        parent?.apply {
            nodesToAdd.forEach {
                addSub(it)
            }
        }
    }

    private fun getNewLine(): Leaf = NlSeparator(codeStyleRepo.newLine())

    private fun processNode(input: Node, parent: Node?, indent: Int): Leaf {
        return when (input) {
            is Region -> {
                if (codeStyleRepo.addSpaceBeforeRegion()) {
                    parent?.addSeparatorNewLine(codeStyleRepo.spaceBeforeClass())
                }
                (input.copyLeaf(copySubs = false) as Region).apply {
                    parent?.addSub(this)
                    processSubs(input, this, indent)
                }
            }

            is ConstantLeaf -> {
                input.copyLeaf(copySubs = false).apply {
                    addIndents(parent, indent)
                    parent?.addSub(this)
                    processSubs(input, this, indent)
                    parent?.addSeparatorNewLine()
                }
            }

            is NamespaceBlock -> {
                (input.copyLeaf(copySubs = false) as Node).apply {
                    parent?.addSub(this)
                    addKeyword("{")
                    addSeparatorNewLine()
                    processSubs(input, this, indent + 1)
                    addKeyword("}")
                    parent?.addSeparatorNewLine()
                }
            }

            else -> {
                val node = input.copyLeaf(copySubs = false) as Node
                parent?.addSub(node)
                processSubs(input, node, indent)
                node
            }
        }
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