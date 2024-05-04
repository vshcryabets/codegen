package ce.parser.domain.usecase

import ce.defs.Target
import ce.parser.domain.dictionaries.StaticDictionaries
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import org.jetbrains.kotlin.javax.inject.Inject

interface LoadAllDictionariesUseCase {
    operator suspend fun invoke(basePath: String): Map<Target, StaticDictionaries>
}

class LoadAllDictionariesUseCaseImpl @Inject constructor(
    private val ioScope: CoroutineScope,
    private val loadTargetDictionariesUseCase: LoadTargetDictionariesUseCase,
) : LoadAllDictionariesUseCase {
    override suspend fun invoke(basePath: String): Map<Target, StaticDictionaries> =
        withContext(ioScope.coroutineContext) {
        val asyncs = mutableListOf<Deferred<Pair<Target, StaticDictionaries>>>()
        Target.values().forEach {
            asyncs.add(async { Pair(it, loadTargetDictionariesUseCase(basePath, it)) })
        }
        val result = mutableMapOf<Target, StaticDictionaries>()
        asyncs.forEach {
            val res = it.await()
            result[res.first] = res.second
        }
        return@withContext result
    }

}