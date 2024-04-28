package ce.parser.domain.usecase

import ce.defs.Target
import ce.parser.SampleData
import ce.parser.domain.NamesDictionaryRepo
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
        namesDictionary: NamesDictionaryRepo,
        digitsDictionary: NamesDictionaryRepo,
        stringLiteralsDictionary: NamesDictionaryRepo,
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
        namesDictionary: NamesDictionaryRepo,
        digitsDictionary: NamesDictionaryRepo,
        stringLiteralsDictionary: NamesDictionaryRepo,
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
                namesDictionary = namesDictionary,
                digitsDictionary = digitsDictionary,
                stringLiteralsDictionary = stringLiteralsDictionary,
                debugFindings = true,
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
                namesDictionary = namesDictionary,
                digitsDictionary = digitsDictionary,
                stringLiteralsDictionary = stringLiteralsDictionary,
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