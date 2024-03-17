package ce.parser.domain.usecase

import ce.defs.Target
import ce.parser.SampleData
import ce.parser.nnparser.SourceBuffer
import ce.parser.nnparser.TargetDictionaries
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.withContext
import org.jetbrains.kotlin.javax.inject.Inject
import org.jetbrains.kotlin.javax.inject.Named
import java.io.File

interface ProcessSampleUseCase {
    suspend operator fun invoke(
        sampleData: SampleData,
        outputDir: String,
        dictionaries: Map<Target, TargetDictionaries>,
        nameBase: Int,
        digitBase: Int,
    )
}

class ProcessSampleUseCaseImpl @Inject constructor(
    @Named("default")
    private val calcScope: CoroutineScope,
    private val loadFileUseCase: LoadFileUseCase,
    private val tokenizerUseCase: TokenizerUseCase,
    private val writeResultsUseCase: WriteResultsUseCase,
) : ProcessSampleUseCase {
    override suspend fun invoke(
        sampleData: SampleData,
        outputDir: String,
        dictionaries: Map<Target, TargetDictionaries>,
        nameBase: Int,
        digitBase: Int,
    ) {
        withContext(calcScope.coroutineContext) {
            val fileSrc = File(sampleData.sourceFile)
            val fileMeta = File(sampleData.metaFile)
            val bufferSrc = loadFileUseCase(fileSrc)
            val bufferMeta = loadFileUseCase(fileMeta)
            val dictSrc = dictionaries[sampleData.sourceTarget]!!
            val dictMeta = dictionaries[Target.Meta]!!
            val srcLinearResult = tokenizerUseCase(
                SourceBuffer(bufferSrc), dictSrc,
                nameBase = nameBase,
                digitBase = digitBase,
                debugFindings = true
            )
            // write stc results
            writeResultsUseCase.invoke(
                outBasePath = outputDir,
                results = srcLinearResult,
                sampleName = sampleData.sampleName,
                sampleTraget = sampleData.sourceTarget
            )
            val metaLinearResult = tokenizerUseCase(
                SourceBuffer(bufferMeta), dictMeta,
                nameBase = nameBase,
                digitBase = digitBase,
                debugFindings = true
            )
            // write meta results
            writeResultsUseCase.invoke(
                outBasePath = outputDir,
                results = metaLinearResult,
                sampleName = sampleData.sampleName,
                sampleTraget = Target.Meta
            )
        }
    }
}