package ce.parser.domain.usecase

import ce.defs.Target
import ce.parser.domain.dictionaries.natural.StaticDictionaries
import ce.parser.domain.dictionaries.natural.StaticDictionariesImpl
import ce.parser.nnparser.Type
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import org.jetbrains.kotlin.javax.inject.Inject
import java.io.File

interface LoadTargetDictionariesUseCase {
    operator suspend fun invoke(baseDir: String, target: Target): StaticDictionaries
}

class LoadTargetDictionariesUseCaseImpl @Inject constructor(
    private val ioScope: CoroutineScope,
    private val loadCsvDictionaryUseCase: LoadDictionaryUseCase,
    private val loadGroovyDictionaryUseCase: LoadDictionaryUseCase,
) : LoadTargetDictionariesUseCase {
    override suspend fun invoke(baseDir: String, target: Target): StaticDictionaries =
        withContext(ioScope.coroutineContext) {
            val operatorsDef = async { loadCsvDictionaryUseCase(
                File("$baseDir/${target.rawValue}_operators.csv"), Type.OPERATOR) }
            val commentsDef = async { loadGroovyDictionaryUseCase(
                File("$baseDir/${target.rawValue}_comments.groovy"), Type.COMMENTS) }
            val digitsDef = async { loadGroovyDictionaryUseCase(
                File("$baseDir/${target.rawValue}_digits.groovy"), Type.DIGIT) }
            val stringLiteralsDef = async { loadGroovyDictionaryUseCase(
                File("$baseDir/${target.rawValue}_strings.groovy"), Type.COMMENTS) }
            return@withContext StaticDictionariesImpl(
                spaces = async {
                        loadCsvDictionaryUseCase(File("$baseDir/${target.rawValue}_spaces.csv"), Type.SPACES)
                    }.await(),
                comments =  commentsDef.await(),
                digits =  digitsDef.await(),
                keywords = async {
                        loadCsvDictionaryUseCase(File("$baseDir/${target.rawValue}_keywords.csv"), Type.KEYWORD)
                    }.await(),
                operators = operatorsDef.await(),
                strings = stringLiteralsDef.await()
            )
        }
}