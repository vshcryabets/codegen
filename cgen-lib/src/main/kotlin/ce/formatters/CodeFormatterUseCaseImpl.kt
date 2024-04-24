package ce.formatters

import generators.cpp.CompilerDirective
import generators.obj.input.*
import generators.obj.out.*
import javax.inject.Inject

open class CodeFormatterUseCaseImpl @Inject constructor(
    protected val codeStyleRepo: CodeStyleRepo,
) : CodeFormatterUseCase {
    override fun <T : Node> invoke(input: T): T {
        return processNode(mutableListOf(input), null, 0, null) as T
    }

    protected open fun processLeaf(inputQueue: MutableList<Leaf>, outputParent: Node, indent: Int) {
        val nodesToAdd = mutableListOf<Leaf>()
        val input = inputQueue.first()
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
        inputQueue.removeFirst()
    }

    protected fun getNewLine(): Leaf = NlSeparator(codeStyleRepo.newLine())

    protected open fun processOutBlock(
        input: OutBlock,
        outputParent: Node,
        indent: Int,
        prev: Leaf?,
        inputQueue: MutableList<Leaf>
    ): OutBlock =
        input.copyLeaf(copySubs = false).apply {
            addIndents(outputParent, indent)
            outputParent.addSub(this)
            // find out block args
            val args = input.subs.findLast {
                it is OutBlockArguments
            }
            if (args != null) {
                input.subs.remove(args)
                addKeyword("(")
                addSub(args)
                addKeyword(")")
            }
            addSub(Space())
            addKeyword("{")
            addSeparatorNewLine()
            processSubs(input, this, indent + 1)
            addIndents(this, indent)
            addKeyword("}")
            val next = inputQueue.firstOrNull()
            if (next != null && next is Separator)
                processSeparatorAfterOutBlock(inputQueue, outputParent)
            outputParent.addSeparatorNewLine()
        }

    protected open fun processSeparatorAfterOutBlock(
        inputQueue: MutableList<Leaf>,
        outputParent: Node
    ) {
    }

    protected open fun processNode(
        inputQueue: MutableList<Leaf>,
        outputParent: Node?,
        indent: Int,
        prev: Leaf?
    ): Node? {
        val input = inputQueue.first()
        inputQueue.removeFirst()
        val next = inputQueue.firstOrNull()
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
            is EnumNode -> {
                input.copyLeaf(copySubs = false).also { output ->
                    addIndents(outputParent, indent)
                    outputParent?.also {parent ->
                        parent.addSub(output)
                        if (next != null && next is EnumNode) {
                            parent.addSub(Separator(","))
                        }
                        parent.addSub(getNewLine())
                    }
                    processSubs(input, output, indent + 1)
                }
            }

            is ConstantNode -> formatConstantNode(input, outputParent, indent, next, prev)

            is OutBlock -> processOutBlock(input, outputParent!!, indent, prev, inputQueue)

            is NamespaceBlock -> {
                input.copyLeaf(copySubs = false).apply {
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
                processSubs(input as Node, node, indent)
                node
            }
        }
    }

    open fun formatConstantNode(
        input: ConstantNode,
        parent: Node?,
        indent: Int,
        next: Leaf?,
        prev: Leaf?
    ): ConstantNode {
        val res = input.copyLeaf(copySubs = false).apply {
            addIndents(parent, indent)
            parent?.addSub(this)
            val queue = input.subs.toMutableList()
            while (queue.isNotEmpty()) {
                val first = queue.first()
                processLeaf(queue, this, indent)
                if ((first !is RValue) and (first !is Separator)) {
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
        val subsQueue = input.subs.toMutableList()
        var prev: Leaf? = null
        while (subsQueue.isNotEmpty()) {
            val current = subsQueue.first()
            if (current is Node) {
                processNode(subsQueue, output, indent, prev)
            } else {
                processLeaf(subsQueue, output, indent)
            }
            prev = current
        }
    }

}