package ce.parser.domain.usecase

import ce.parser.WordDictionary
import generators.obj.input.Leaf
import org.jetbrains.kotlin.javax.inject.Inject

interface TokenizerUseCase {
    operator fun invoke(buffer: String, dictionary: WordDictionary): List<Leaf>
}

class TokenizerUseCaseImpl @Inject constructor(): TokenizerUseCase {
    override operator fun invoke(buffer: String, dictionary: WordDictionary): List<Leaf> {
        return emptyList()
    }
}