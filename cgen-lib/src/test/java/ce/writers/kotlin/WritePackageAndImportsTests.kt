package ce.writers.kotlin

import ce.basetest.KotlinBaseTest
import ce.defs.Target
import ce.domain.usecase.add.AddRegionDefaultsUseCaseImpl
import ce.io.CodeWriter
import generators.kotlin.KotlinWriter
import generators.kotlin.KtConstantsGenerator
import generators.obj.abstractSyntaxTree.ConstantsBlock
import generators.obj.abstractSyntaxTree.NamespaceImpl
import generators.obj.syntaxParseTree.OutputTree
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class WritePackageAndImportsTests: KotlinBaseTest() {
    private val ktConstantsGenerator = KtConstantsGenerator(
        addBlockDefaultsUseCase = AddRegionDefaultsUseCaseImpl(repo),
        dataTypeToString = getTypeNameUseCase,
        prepareRightValueUseCase = prepareRightValueUseCase
    )
    private val writer = KotlinWriter(repo, "")

    @Test
    fun testPackageFileOutput() {
        val tree = xmlReader.loadFromString("""
            <Namespace name="com.goldman.xml">
                <ConstantsBlock defaultType="string" name="Constants">
                    <ConstantDesc name="Const1" value="ABC"/>
                    <ConstantDesc name="Const2" value="DEF"/>
                </ConstantsBlock>
            </Namespace>
        """.trimIndent())
        val lastNs = (tree as NamespaceImpl).getNamespace("goldman.xml")
        val block = lastNs.subs.first() as ConstantsBlock

        val projectOutput = OutputTree(Target.Kotlin)
        val files = fileGenerator.createFile(projectOutput,
            workingDirectory = "./",
            packageDirectory = "",
            "a", block)
        val mainFile = files.first()
        ktConstantsGenerator(files, block)
        val formatted = formatter(input = mainFile)

        val buffer = StringBuffer()
        writer.writeNode(formatted, object : CodeWriter {
            override fun write(str: String): CodeWriter {
                buffer.append(str)
                return this
            }

            override fun writeNl(): CodeWriter {
                buffer.append("\n")
                return this
            }

            override fun writeNlIfNotEmpty(): CodeWriter = this
            override fun setIndent(str: String): CodeWriter = this
            override fun setNewLine(str: String) {}
        }, "")
        Assertions.assertTrue(buffer.toString().startsWith("package com.goldman.xml\n\n"))
    }
}