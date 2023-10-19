package ce.parser.domain.usecase

import ce.parser.SampleData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.withContext
import org.jetbrains.kotlin.javax.inject.Inject
import org.jetbrains.kotlin.javax.inject.Named
import java.io.File

interface ProcessSampleUseCase {
    suspend operator fun invoke(sampleData: SampleData, outputDir: String)
}

class ProcessSampleUseCaseImpl @Inject constructor(
    @Named("default")
    private val calcScope: CoroutineScope,
    private val loadFileUseCase: LoadFileUseCase
) : ProcessSampleUseCase {
    override suspend fun invoke(sampleData: SampleData, outputDir: String) =
        withContext(calcScope.coroutineContext) {
            val fileSrc = File(sampleData.sourceFile)
            val fileMeta = File(sampleData.metaFile)
            val bufferSrc = loadFileUseCase(fileSrc)
            val bufferMeta = loadFileUseCase(fileMeta)
        }
}