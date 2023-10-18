package ce.parser.domain.usecase

import ce.defs.Target
import ce.parser.TargetDictionaries
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.jetbrains.kotlin.javax.inject.Inject

interface LoadAllDictionariesUseCase {
    operator suspend fun invoke(basePath: String): Map<Target, TargetDictionaries>
}

class LoadAllDictionariesUseCaseImpl @Inject constructor(
    private val ioScope: CoroutineScope,
    private val loadTargetDictionariesUseCase: LoadTargetDictionariesUseCase,
) : LoadAllDictionariesUseCase {
    override suspend fun invoke(basePath: String): Map<Target, TargetDictionaries> {
        val r2 = mutableMapOf<Target, TargetDictionaries>()
        val results = mutableListOf<Deferred<Pair<Target, TargetDictionaries>>>()
        ioScope.launch {
            Target.values().forEach {
                results.add(async { Pair(it, loadTargetDictionariesUseCase(basePath, it)) })
            }
        }
        results.forEach {
            val res = it.await()
            r2[res.first] = res.second
        }
        return r2
    }

}