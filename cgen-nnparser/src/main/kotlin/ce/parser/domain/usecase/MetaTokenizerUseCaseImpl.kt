package ce.parser.domain.usecase

import ce.parser.domain.dictionaries.StaticDictionaries
import ce.parser.nnparser.SourceBuffer
import ce.parser.nnparser.WordItem
import org.jetbrains.kotlin.javax.inject.Inject

class MetaTokenizerUseCaseImpl @Inject constructor(
    checkStringInDictionaryUseCase: CheckStringInDictionaryUseCase
) :
    TokenizerUseCaseImpl(
        checkString = checkStringInDictionaryUseCase
    ) {

    private var namespaceParseMode: Int = 0

    override fun lookInStaticDictionaries(
        buffer: SourceBuffer,
        result: MutableList<WordItem>,
        debugLine1: StringBuilder,
        debugLine2: StringBuilder,
        staticDictionaries: StaticDictionaries
    ): Boolean {
        if (buffer.nextIs("namespace(")) {
            namespaceParseMode = 2
        }
        if (namespaceParseMode > 0 && buffer.nextIs("\"")) {
            // What about escaped \" symbols ?
            val quoteOperator = staticDictionaries.getOperators().search("\"")!!
            result.add(quoteOperator)
            debugLine1.append('"')
            debugLine2.append("${quoteOperator.id}, ")
            buffer.movePosBy(1)
            namespaceParseMode--
            return true
        }
        return super.lookInStaticDictionaries(buffer, result, debugLine1, debugLine2, staticDictionaries)
    }
}