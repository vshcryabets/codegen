package ce.formatters

import ce.defs.RValue
import generators.cpp.CompilerDirective
import generators.obj.input.Leaf
import generators.obj.input.Node
import generators.obj.input.addKeyword
import generators.obj.input.addSeparator
import generators.obj.input.addSeparatorNewLine
import generators.obj.input.addSub
import generators.obj.out.ArgumentNode
import generators.obj.out.CommentLeaf
import generators.obj.out.ConstantNode
import generators.obj.out.EnumNode
import generators.obj.out.FileData
import generators.obj.out.Indent
import generators.obj.out.NamespaceBlock
import generators.obj.out.NlSeparator
import generators.obj.out.OutBlock
import generators.obj.out.OutBlockArguments
import generators.obj.out.Region
import generators.obj.out.Separator
import generators.obj.out.Space
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

    open fun processArgumentNode(
        input: ArgumentNode,
        outputParent: Node,
        indent: Int,
        prev: Leaf?,
        inputQueue: MutableList<Leaf>
    ): ArgumentNode = defaultProcessNode(input, outputParent, indent) as ArgumentNode

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
            val outBlockArgs = input.subs.findLast {
                it is OutBlockArguments
            } as OutBlockArguments?
            if (outBlockArgs != null) {
                // wrap then into ()
                input.subs.remove(outBlockArgs)
                processArguments(
                    input = outBlockArgs,
                    parent = this,
                    indent = indent
                )
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

    protected open fun defaultProcessNode(input: Leaf, outputParent: Node?, indent: Int): Node {
        val node = input.copyLeaf(copySubs = false) as Node
        outputParent?.addSub(node)
        processSubs(input as Node, node, indent)
        return node
    }

    protected open fun processSeparatorAfterOutBlock(
        inputQueue: MutableList<Leaf>,
        outputParent: Node
    ) {
    }

    open fun processEnumNode(
        input: EnumNode,
        parent: Node?,
        indent: Int,
        next: Leaf?,
        prev: Leaf?
    ): EnumNode {
        return input.copyLeaf(copySubs = false).also { output ->
            addIndents(parent, indent)
            parent?.addSub(output)
            if (next != null && next is EnumNode) {
                parent?.addSub(Separator(","))
            }
            parent?.addSub(getNewLine())
            processSubs(input, output, indent + 1)
        }
    }

    open fun processArguments(
        input: Node,
        parent: Node?,
        indent: Int
    ): Node {
        parent?.addKeyword("(")
        val multiline = input.subs.size > 1

        val result = input.copyLeaf(copySubs = false).apply {
            if (multiline) parent?.addSub(NlSeparator())
            parent?.addSub(this)
            if (multiline) parent?.addSub(NlSeparator())
            processSubs(input, this, indent + 1)
        }
        parent?.addKeyword(")")
        return result
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
            is EnumNode -> processEnumNode(
                input = input,
                parent = outputParent,
                indent = indent,
                next = next,
                prev = prev
            )

            is ConstantNode -> processConstantNode(input, outputParent, indent, next, prev)

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
            is ArgumentNode -> processArgumentNode(input, outputParent!!, indent, prev, inputQueue)
            is OutBlockArguments -> processArguments(
                input = input,
                parent = outputParent,
                indent = indent
            )

            else -> {
                defaultProcessNode(input, outputParent, indent)
            }
        }
    }

    open fun processConstantNode(
        input: ConstantNode,
        parent: Node?,
        indent: Int,
        next: Leaf?,
        prev: Leaf?
    ): ConstantNode {
        addIndents(parent, indent)
        val res = input.copyLeaf(copySubs = false).apply {
            parent?.addSub(this)
            val queue = input.subs.toMutableList()
            while (queue.isNotEmpty()) {
                val first = queue.first()
                processLeaf(queue, this, indent)
                if ((first !is Separator) and (first !is RValue)) {
                    this.addSub(Space())
                }
            }
        }
        parent?.addSeparator(";")
        parent?.addSeparatorNewLine()
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