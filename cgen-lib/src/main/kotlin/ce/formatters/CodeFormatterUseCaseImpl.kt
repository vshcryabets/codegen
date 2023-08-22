package ce.formatters

import generators.cpp.CompilerDirective
import generators.obj.input.*
import generators.obj.out.*
import javax.inject.Inject

open class CodeFormatterUseCaseImpl @Inject constructor(
    private val codeStyleRepo: CodeStyleRepo,
) : CodeFormatterUseCase {
    override fun <T : Node> invoke(input: T): T {
        return processNode(input, null, 0, null) as T
    }

    protected open fun processLeaf(input: Leaf, outputParent: Node, indent: Int) {
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

    protected fun getNewLine(): Leaf = NlSeparator(codeStyleRepo.newLine())

    protected open fun processNode(input: Node, outputParent: Node?, indent: Int, next: Leaf?): Node? {
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
            is EnumLeaf -> {
                input.copyLeaf(copySubs = false).also { output ->
                    addIndents(outputParent, indent)
                    outputParent?.also {parent ->
                        parent.addSub(output)
                        if (next != null && next is EnumLeaf) {
                            parent.addSub(Separator(","))
                        }
                        parent.addSub(getNewLine())
                    }
                    processSubs(input, output, indent + 1)
                }
            }

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

    private fun formatConstantLeaf(input: ConstantLeaf, parent: Node?, indent: Int): ConstantLeaf {
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

    protected open fun addIndents(parent: Node?, indent: Int) {
        if (parent == null) {
            return
        }
        if (indent > 0) {
            parent.subs.addAll(getIndents(indent))
        }
    }

    protected fun getIndents(indent: Int): List<Indent> =
        mutableListOf<Indent>().apply {
            (0..indent - 1).forEach { add(Indent()) }
        }


    protected open fun processSubs(input: Node, output: Node, indent: Int) {
        for (i in 0..input.subs.size - 1) {
            val current = input.subs[i]
            val next = if (i < input.subs.size - 1) input.subs[i + 1] else null
            if (current is Node) {
                processNode(current, output, indent, next)
            } else {
                processLeaf(current, output, indent)
            }
        }
    }

}