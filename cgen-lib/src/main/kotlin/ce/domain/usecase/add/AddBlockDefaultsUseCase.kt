package ce.domain.usecase.add

import generators.obj.FileGenerator
import generators.obj.input.Block
import generators.obj.out.*
import javax.inject.Inject

interface AddBlockDefaultsUseCase {
    operator fun invoke(desc: Block, result: ClassData)
}

class AddBlockDefaultsUseCaseImpl @Inject constructor(
    private val fileGenerator: FileGenerator
): AddBlockDefaultsUseCase {
    override fun invoke(desc: Block, result: ClassData) {
        result.addSub(BlockPreNewLines())
        if (desc.subs.size > 0) {
            val first = desc.subs[0]
            if (first is CommentsBlock) {
                if (first.subs.size > 1) {
                    // multiline
                    result.addSub(MultilineCommentsBlock()).apply {
                        first.subs.forEach {
                            addSub(it)
                        }
                    }
                } else {
                    // singleline
                    result.addSub(CommentsBlock()).apply {
                        first.subs.forEach {
                            if (it is CommentLeaf) {
                                this.addSub(CommentLeaf(fileGenerator.singleComment() + " " + it.name))
                            } else {
                                this.addSub(it)
                            }
                        }
                    }
                }
                desc.removeSub(first) // let's remove comments block because we already handle it
            }
        }
    }
}