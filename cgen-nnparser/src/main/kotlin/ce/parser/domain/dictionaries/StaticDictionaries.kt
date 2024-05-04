package ce.parser.domain.dictionaries

import org.jetbrains.kotlin.javax.inject.Inject

interface StaticDictionaries {
    fun getKeywords(): StaticDictionary
    fun getOperators(): StaticDictionary
    fun getDigits(): StaticDictionary
    fun getComments(): StaticDictionary
    fun getStringLiterals(): StaticDictionary
    fun getSpaces(): StaticDictionary
}

class StaticDictionariesImpl @Inject constructor(
    private val keywords: StaticDictionary,
    private val operators: StaticDictionary,
    private val digits: StaticDictionary,
    private val comments: StaticDictionary,
    private val strings: StaticDictionary,
    private val spaces: StaticDictionary,
): StaticDictionaries {
    override fun getKeywords(): StaticDictionary = keywords
    override fun getOperators(): StaticDictionary = operators
    override fun getDigits(): StaticDictionary = digits
    override fun getComments(): StaticDictionary = comments
    override fun getStringLiterals(): StaticDictionary = strings
    override fun getSpaces(): StaticDictionary = spaces
}