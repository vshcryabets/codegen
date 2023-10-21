package ce.parser.domain.usecase

import ce.parser.nnparser.Word
import ce.parser.nnparser.WordDictionary
import generators.obj.input.Leaf
import org.jetbrains.kotlin.javax.inject.Inject

interface TokenizerUseCase {
    operator fun invoke(
        buffer: String,
        keywords: WordDictionary,
        dictionary: WordDictionary
    ): List<Word>
}

class TokenizerUseCaseImpl @Inject constructor() : TokenizerUseCase {
    override operator fun invoke(
        buffer: String,
        keywords: WordDictionary,
        operators: WordDictionary
    ): List<Word> {
        return emptyList()
    }
}