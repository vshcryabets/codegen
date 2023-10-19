package ce.parser.domain.usecase

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.withContext
import org.jetbrains.kotlin.javax.inject.Inject
import org.jetbrains.kotlin.javax.inject.Named
import java.io.File

interface LoadFileUseCase {
    suspend operator fun invoke(file: File): StringBuilder
}

class LoadFileUseCaseImpl @Inject constructor(
    @Named("io")
    private val ioScope: CoroutineScope,
): LoadFileUseCase {
    override suspend fun invoke(file: File): StringBuilder =
        withContext(ioScope.coroutineContext) {
        val result = StringBuilder()
        file.forEachLine {
            result.append(it)
            result.append('\n')
        }
        return@withContext result
    }
}