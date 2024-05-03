package ce.parser.nnparser

import ce.parser.domain.NamesDictionaryRepo
import org.jetbrains.kotlin.javax.inject.Inject

interface DynamicDictionaries {
    fun getNamesDictionary(): NamesDictionaryRepo
    fun getDigitsDictionary(): NamesDictionaryRepo
    fun getStringDictionary(): NamesDictionaryRepo
}

class DynamicDictionariesImpl @Inject constructor(
    private val namesDictionary: NamesDictionaryRepo,
    private val digitsDictionary: NamesDictionaryRepo,
    private val stringLiteralsDictionary: NamesDictionaryRepo
) : DynamicDictionaries {
    override fun getNamesDictionary(): NamesDictionaryRepo = namesDictionary

    override fun getDigitsDictionary(): NamesDictionaryRepo = digitsDictionary

    override fun getStringDictionary(): NamesDictionaryRepo = stringLiteralsDictionary
}