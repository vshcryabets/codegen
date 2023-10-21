package ce.parser.domain.usecase

import ce.parser.*
import org.jetbrains.kotlin.javax.inject.Inject

interface BuildLinearUseCase {
    suspend operator fun invoke(buffer: StringBuilder, inPos: Int, dictionary: WordDictionary): LinearResult
}

class BuildLinearUseCaseImpl @Inject constructor(): BuildLinearUseCase {
    override suspend fun invoke(buffer: StringBuilder, inPos: Int, dictionary: WordDictionary): LinearResult {
        println("buildLinear")
        val srcBuffer = SourceBuffer(buffer, inPos)
        var literalCounter = 1000000
        var digitCounter = 2000000
        val literalsMap = mutableMapOf<Int, Literal>()
        val digitisMap = mutableMapOf<Int, Digit>()
        val namesMap = mutableMapOf<Int, Name>()
        val numbers = mutableListOf<Int>()
        var prevWord = Word("")

        val wordsMapRevers = dictionary.reverse

        do {
            if (srcBuffer.nextIs("//")) {
                val literalPair = srcBuffer.readUntil("\n", false, true)
                literalsMap[literalCounter] = Literal(literalPair.first)
                numbers.add(literalCounter)
                println("Comment \"${literalPair.first}\" = $literalCounter")
                literalCounter++
            } else if (srcBuffer.nextIs("/*")) {
                val strPair = srcBuffer.readUntil("*/", false, true)
                literalsMap[literalCounter] = Literal(strPair.first)
                numbers.add(literalCounter)
                println("Comment multiline \"${strPair.first}\" = $literalCounter")
                literalCounter++
            } else if (srcBuffer.nextIs("\"")) {
                val literalPair = srcBuffer.readLiteral()
                literalsMap[literalCounter] = literalPair.first
                numbers.add(literalCounter)
                println("Literal \"${literalPair.first.name}\" = $literalCounter")
                literalCounter++
            } else if (srcBuffer.nextIn(SourceBuffer.spaces)) {
                srcBuffer.skipChar()
            } else if (srcBuffer.nextIn(SourceBuffer.digits)) {
                val digit = srcBuffer.readDigit()
                digitisMap[digitCounter] = digit.first
                numbers.add(digitCounter)
                digitCounter++
            } else {
                val wordPair = srcBuffer.readWord()

                if (!wordsMapRevers.containsKey(wordPair.first.name)) {

                    if (prevWord.nextIsLiteral) {
                        literalsMap[literalCounter] = Literal(wordPair.first.name)
                        numbers.add(literalCounter)
                        println("Literal \"${wordPair.first.name}\" = $literalCounter")
                        literalCounter++
                        prevWord = Word("")
                    } else {
                        // add to dictionary
                        val newWord = dictionary.addWord(wordPair.first)
                        numbers.add(newWord.id)
                        prevWord = wordPair.first
                    }
                } else {
                    var id = wordsMapRevers[wordPair.first.name]!!
                    numbers.add(id)
                    prevWord = dictionary.dictionary[id]!!
                }
            }
        } while (!srcBuffer.end())
        println(numbers.toString())
        return LinearResult(
            wordsMap = dictionary.dictionary,
            digits = digitisMap,
            namesMap = namesMap,
            literalsMap = literalsMap,
            tokens = numbers,
        )
    }

}