package ce.domain.usecase.add

import ce.formatters.CodeStyleRepo
import generators.obj.abstractSyntaxTree.Block
import generators.obj.abstractSyntaxTree.addSub
import generators.obj.syntaxParseTree.*
import javax.inject.Inject

interface AddRegionDefaultsUseCase {
    operator fun invoke(desc: Block, result: Region)
}

class AddRegionDefaultsUseCaseImpl @Inject constructor(
    private val codeStyleRepo: CodeStyleRepo,
) : AddRegionDefaultsUseCase {
    override fun invoke(desc: Block, result: Region) {
        if (desc.subs.size > 0) {
            val first = desc.subs[0]

            if (first is CommentsBlock) {
                result.addSub(first.copyLeaf(result))
            }
        }
    }
}