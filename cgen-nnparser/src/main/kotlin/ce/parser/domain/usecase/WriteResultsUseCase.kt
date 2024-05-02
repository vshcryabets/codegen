package ce.parser.domain.usecase

import ce.defs.Target
import ce.parser.SampleData
import ce.parser.nnparser.TargetDictionaries
import ce.parser.nnparser.Type
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.withContext
import org.jetbrains.kotlin.incremental.createDirectory
import org.jetbrains.kotlin.javax.inject.Named
import java.io.File
import java.io.PrintWriter
import java.nio.charset.Charset

interface WriteResultsUseCase {
    operator suspend fun invoke(outBasePath: String,
                                sampleName: String,
                                sampleTraget: Target,
                                results: TokenizerUseCase.Result)
}

class WriteResultsUseCaseImpl(
    @Named("io")
    private val ioScope: CoroutineScope,
): WriteResultsUseCase {
    override suspend fun invoke(
        outBasePath: String,
        sampleName: String,
        sampleTraget: Target,
        results: TokenizerUseCase.Result
    ) {
        withContext(ioScope.coroutineContext) {
            val base = File(outBasePath)
            base.createDirectory()
            val linearIds = File(base, "${sampleName}_${sampleTraget}_ids.data")
            println("Write results to ${linearIds.absolutePath}")
            linearIds.printWriter().use { printWriter ->
                var addComa = false
                results.words.forEach {
                    if (addComa) {
                        printWriter.print(",")
                    }
                    printWriter.print(it.id)
                    addComa = true
                }
            }
            val names = File(base, "${sampleName}_${sampleTraget}_names.csv")
            println("Write names to ${names.absolutePath}")
            names.printWriter().use { writter ->
                results.namesDictionary.forEach {
                    writter.println("${it.id},${it.name}")
                }
            }
            val digits = File(base, "${sampleName}_${sampleTraget}_digits.csv")
            println("Write digits to ${digits.absolutePath}")
            digits.printWriter().use { writter ->
                results.digitsDictionary.forEach {
                    writter.println("${it.id},${it.name}")
                }
            }
            val strings = File(base, "${sampleName}_${sampleTraget}_strings.csv")
            println("Write strings to ${strings.absolutePath}")
            strings.printWriter().use { writter ->
                results.stringsDictionary.forEach {
                    writter.println("${it.id},${it.name}")
                }
            }
            val comments = File(base, "${sampleName}_${sampleTraget}_commentss.csv")
            println("Write comments to ${comments.absolutePath}")
            comments.printWriter().use { writter ->
                results.words
                    .filter {
                        it.type == Type.COMMENTS
                    }
                    .forEach {
                        writter.println("${it.id},${it.name}")
                    }
            }
            if (results.debugFindings.isNotEmpty()) {
                val debug = File(base, "${sampleName}_${sampleTraget}_debug.txt")
                println("Write debug to ${debug.absolutePath}")
                debug.printWriter().use { writter ->
                    writter.print(results.debugFindings)
                }
            } else {
                println("Debug information is empty")
            }
        }
    }
}