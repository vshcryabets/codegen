package ce.parser.nnparser

import ce.parser.domain.usecase.CheckStringInDictionaryUseCase

class MetaNamespaceWord(
    override val name: String = "NAMESPACE",
    override val type: Type = Type.UNKNOWN,
    override val id: Int = 0): ProgrammableWord {
    override fun check(buffer: SourceBuffer): CheckStringInDictionaryUseCase.Result {
        val resultWords = mutableListOf<WordItem>()
        if (buffer.nextIs("namespace(\"")) {
            resultWords.add(
                Word(
                    name = "namespace"
                )
            )
        }
        return CheckStringInDictionaryUseCase.EMPTY_RESULT
    }
}