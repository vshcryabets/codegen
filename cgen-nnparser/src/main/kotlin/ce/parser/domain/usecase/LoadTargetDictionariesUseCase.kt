package ce.parser.domain.usecase

import ce.defs.Target
import ce.parser.nnparser.TargetDictionaries
import ce.parser.nnparser.Type
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import org.jetbrains.kotlin.javax.inject.Inject
import java.io.File

interface LoadTargetDictionariesUseCase {
    operator suspend fun invoke(baseDir: String, target: Target): TargetDictionaries
}

class LoadTargetDictionariesUseCaseImpl @Inject constructor(
    private val ioScope: CoroutineScope,
    private val loadCsvDictionaryUseCase: LoadDictionaryUseCase,
    private val loadGroovyDictionaryUseCase: LoadDictionaryUseCase,
) : LoadTargetDictionariesUseCase {
    override suspend fun invoke(baseDir: String, target: Target): TargetDictionaries =
        withContext(ioScope.coroutineContext) {
            val operatorsDef = async { loadCsvDictionaryUseCase(File("$baseDir/${target.rawValue}_operators.csv"), Type.OPERATOR) }
            val commentsDef = async { loadGroovyDictionaryUseCase(File("$baseDir/${target.rawValue}_comments.groovy"), Type.COMMENTS) }
            val digitsDef = async { loadCsvDictionaryUseCase(File("$baseDir/${target.rawValue}_digits.csv"), Type.DIGIT) }
            return@withContext TargetDictionaries(
                map = mapOf(
                    Type.SPACES to async {
                        loadCsvDictionaryUseCase(File("$baseDir/${target.rawValue}_spaces.csv"), Type.SPACES)
                    }.await(),
                    Type.COMMENTS to commentsDef.await(),
                    Type.DIGIT to digitsDef.await(),
                    Type.KEYWORD to async {
                        loadCsvDictionaryUseCase(File("$baseDir/${target.rawValue}_keywords.csv"), Type.KEYWORD)
                    }.await(),
                    Type.OPERATOR to operatorsDef.await()
                )
            )
        }
}