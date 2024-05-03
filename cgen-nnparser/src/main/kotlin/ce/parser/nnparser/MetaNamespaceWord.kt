package ce.parser.nnparser

import ce.parser.domain.usecase.CheckStringInDictionaryUseCase

class MetaNamespaceWord(
    override val name: String = "NAMESPACE",
    override val type: Type = Type.UNKNOWN,
    override val id: Int = 0): ProgrammableWord {
    override fun check(buffer: SourceBuffer): CheckStringInDictionaryUseCase.Result {
        return CheckStringInDictionaryUseCase.EMPTY_RESULT
    }
}