package ce.parser.domain.dictionaries

import org.jetbrains.kotlin.javax.inject.Inject

interface DynamicDictionaries {
    fun getNamesDictionary(): NamesDictionary
    fun getDigitsDictionary(): NamesDictionary
    fun getStringDictionary(): NamesDictionary
}

class DynamicDictionariesImpl @Inject constructor(
    private val namesDictionary: NamesDictionary,
    private val digitsDictionary: NamesDictionary,
    private val stringLiteralsDictionary: NamesDictionary
) : DynamicDictionaries {
    override fun getNamesDictionary(): NamesDictionary = namesDictionary

    override fun getDigitsDictionary(): NamesDictionary = digitsDictionary

    override fun getStringDictionary(): NamesDictionary = stringLiteralsDictionary
}