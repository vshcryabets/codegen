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
    private val loadDictionaryUseCase: LoadDictionaryUseCase,
) : LoadTargetDictionariesUseCase {
    override suspend fun invoke(baseDir: String, target: Target): TargetDictionaries =
        withContext(ioScope.coroutineContext) {
            val keywordsDef = async { loadDictionaryUseCase(File("$baseDir/${target.rawValue}_keywords.csv")) }
            val operatorsDef = async { loadDictionaryUseCase(File("$baseDir/${target.rawValue}_operators.csv")) }
            val spacesDef = async { loadDictionaryUseCase(File("$baseDir/${target.rawValue}_spaces.csv")) }
            val commentsDef = async { loadDictionaryUseCase(File("$baseDir/${target.rawValue}_comments.csv")) }
            val stdlibsDef = async { loadDictionaryUseCase(File("$baseDir/${target.rawValue}_stdlibs.csv")) }
            val thirdDef = async { loadDictionaryUseCase(File("$baseDir/${target.rawValue}_third.csv")) }
            val projectDef = async { loadDictionaryUseCase(File("$baseDir/${target.rawValue}_project.csv")) }
            return@withContext TargetDictionaries(
                keywords = keywordsDef.await(),
                operators = operatorsDef.await(),
                stdlibs = stdlibsDef.await(),
                thirdlibs = thirdDef.await(),
                projectlibs = projectDef.await(),
                map = mapOf(
                    Type.SPACES to spacesDef.await(),
                    Type.COMMENTS to commentsDef.await()
                )
            )
        }
}