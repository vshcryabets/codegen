package ce.parser.domain.usecase

import ce.defs.Target
import ce.parser.SampleData
import ce.parser.domain.dictionaries.natural.DynamicDictionaries
import ce.parser.domain.dictionaries.natural.StaticDictionaries
import ce.parser.domain.usecase.save.WriteResultsUseCase
import ce.parser.nnparser.SourceBuffer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.withContext
import org.jetbrains.kotlin.javax.inject.Inject
import org.jetbrains.kotlin.javax.inject.Named
import java.io.File

interface ProcessSampleUseCase {
    suspend operator fun invoke(
        sampleData: SampleData,
        outputDir: String,
        dictionaries: Map<Target, StaticDictionaries>,
        dynamicDictionaries: DynamicDictionaries,
    )
}

class ProcessSampleUseCaseImpl @Inject constructor(
    @Named("default")
    private val calcScope: CoroutineScope,
    private val loadFileUseCase: LoadFileUseCase,
    private val tokenizerUseCase: TokenizerUseCase,
    private val metaTokenizerUseCase: TokenizerUseCase,
    private val writeResultsUseCase: WriteResultsUseCase,
) : ProcessSampleUseCase {
    override suspend fun invoke(
        sampleData: SampleData,
        outputDir: String,
        dictionaries: Map<Target, StaticDictionaries>,
        dynamicDictionaries: DynamicDictionaries,
    ) {
        withContext(calcScope.coroutineContext) {
            val fileSrc = File(sampleData.sourceFile)
            val fileMeta = File(sampleData.metaFile)
            val dictSrc = dictionaries[sampleData.sourceTarget]
                ?: throw IllegalStateException("Can't find ${sampleData.sourceTarget} dictionaries")
            val dictMeta = dictionaries[Target.Meta] ?: throw IllegalStateException("Can't find META dictionaries")
            val srcLinearResult = tokenizerUseCase(
                buffer = SourceBuffer(loadFileUseCase(fileSrc)),
                dictSrc,
                dynamicDictionaries = dynamicDictionaries,
                debugFindings = true,
            )
            // write src results
            writeResultsUseCase.invoke(
                outBasePath = outputDir,
                results = srcLinearResult,
                sampleName = sampleData.sampleName,
                sampleTraget = sampleData.sourceTarget
            )
            val metaLinearResult = metaTokenizerUseCase(
                buffer = SourceBuffer(loadFileUseCase(fileMeta)),
                dictMeta,
                dynamicDictionaries = dynamicDictionaries,
                debugFindings = true,
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