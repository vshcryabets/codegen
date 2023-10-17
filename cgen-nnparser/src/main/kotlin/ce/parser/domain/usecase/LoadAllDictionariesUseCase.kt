package ce.parser.domain.usecase

import ce.parser.TargetDictionaries
import ce.parser.Word
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.jetbrains.kotlin.javax.inject.Inject
import java.io.File

interface LoadAllDictionariesUseCase {
    operator fun invoke(basePath: String): Map<Target, TargetDictionaries>
}

class LoadAllDictionariesUseCaseImpl @Inject constructor(
    private val ioScope: CoroutineScope,
    private val loadDictionaryUseCase: LoadDictionaryUseCase,
) : LoadAllDictionariesUseCase {
    override fun invoke(basePath: String): Map<Target, TargetDictionaries> {
        ioScope.launch {
            val results = mutableListOf<Deferred<MutableMap<Int, Word>>>()
            ce.defs.Target.values().forEach {
                val file = File("$basePath/${it.rawValue}_core.csv")
                if (file.exists()) {
                    results.add(async { loadDictionaryUseCase(file) })
                }
            }
            results.forEach {
                it.await()
            }
        }
        return emptyMap()
    }
}