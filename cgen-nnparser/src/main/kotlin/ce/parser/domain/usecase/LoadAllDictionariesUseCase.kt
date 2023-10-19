package ce.parser.domain.usecase

import ce.defs.Target
import ce.parser.TargetDictionaries
import kotlinx.coroutines.*
import org.jetbrains.kotlin.javax.inject.Inject

interface LoadAllDictionariesUseCase {
    operator suspend fun invoke(basePath: String): Map<Target, TargetDictionaries>
}

class LoadAllDictionariesUseCaseImpl @Inject constructor(
    private val ioScope: CoroutineScope,
    private val loadTargetDictionariesUseCase: LoadTargetDictionariesUseCase,
) : LoadAllDictionariesUseCase {
    override suspend fun invoke(basePath: String): Map<Target, TargetDictionaries> =
        withContext(ioScope.coroutineContext) {
        val asyncs = mutableListOf<Deferred<Pair<Target, TargetDictionaries>>>()
        Target.values().forEach {
            asyncs.add(async { Pair(it, loadTargetDictionariesUseCase(basePath, it)) })
        }
        val result = mutableMapOf<Target, TargetDictionaries>()
        asyncs.forEach {
            val res = it.await()
            result[res.first] = res.second
        }
        return@withContext result
    }

}