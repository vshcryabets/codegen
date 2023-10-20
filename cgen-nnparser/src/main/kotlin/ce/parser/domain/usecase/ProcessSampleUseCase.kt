package ce.parser.domain.usecase

import ce.defs.Target
import ce.parser.SampleData
import ce.parser.TargetDictionaries
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.withContext
import org.jetbrains.kotlin.javax.inject.Inject
import org.jetbrains.kotlin.javax.inject.Named
import java.io.File

interface ProcessSampleUseCase {
    suspend operator fun invoke(sampleData: SampleData, outputDir: String, dictionaries: Map<Target, TargetDictionaries>)
}

class ProcessSampleUseCaseImpl @Inject constructor(
    @Named("default")
    private val calcScope: CoroutineScope,
    private val loadFileUseCase: LoadFileUseCase,
    private val buildLinearUseCase: BuildLinearUseCase,
) : ProcessSampleUseCase {
    override suspend fun invoke(sampleData: SampleData, outputDir: String, dictionaries: Map<Target, TargetDictionaries>) =
        withContext(calcScope.coroutineContext) {
            val fileSrc = File(sampleData.sourceFile)
            val fileMeta = File(sampleData.metaFile)
            val bufferSrc = loadFileUseCase(fileSrc)
            val bufferMeta = loadFileUseCase(fileMeta)
            val dictSrc = dictionaries[sampleData.sourceTarget]!!
            val dictMeta = dictionaries[Target.Meta]!!
            val srcLinearResult = buildLinearUseCase(bufferSrc, 0, dictSrc.core)
            val metaLinearResult = buildLinearUseCase(bufferMeta, 0, dictMeta.core)
            println(srcLinearResult)
            println(metaLinearResult)
        }
}