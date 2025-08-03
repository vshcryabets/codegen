package ce.formatters.kotlin

import ce.defs.Target
import ce.domain.usecase.add.AddRegionDefaultsUseCaseImpl
import ce.formatters.CLikeCodestyleRepo
import ce.formatters.CodeFormatterKotlinUseCaseImpl
import ce.settings.CodeStyle
import ce.treeio.XmlTreeReader
import generators.kotlin.GetArrayDataTypeUseCase
import generators.kotlin.GetTypeNameUseCase
import generators.kotlin.KotlinFileGenerator
import generators.kotlin.KtConstantsGenerator
import generators.kotlin.PrepareRightValueUseCase
import generators.obj.input.ConstantsBlock
import generators.obj.input.NamespaceImpl
import generators.obj.out.Keyword
import generators.obj.out.NamespaceDeclaration
import generators.obj.out.OutputTree
import generators.obj.out.Space
import generators.obj.out.VariableName
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class KotlinPackageAndImportsTest {
    val xmlReader = XmlTreeReader()
    private val fileGenerator = KotlinFileGenerator()
    val codeStyle1NlBeforeRegion = CodeStyle(
        newLinesBeforeClass = 1,
        tabSize = 4,
        preventEmptyBlocks = true,
    )

    val codeStyle = CodeStyle(
        newLinesBeforeClass = 0,
        tabSize = 2,
        preventEmptyBlocks = true,
    )
    private val arrayDataType = GetArrayDataTypeUseCase()
    private val getTypeNameUseCase = GetTypeNameUseCase(arrayDataType)
    val repoNoSpace = CLikeCodestyleRepo(codeStyle)
    val formatter = CodeFormatterKotlinUseCaseImpl(repoNoSpace)
    private val repo = CLikeCodestyleRepo(codeStyle)
    private val prepareRightValueUseCase = PrepareRightValueUseCase(getTypeNameUseCase)
    private val ktConstantsGenerator = KtConstantsGenerator(
        addBlockDefaultsUseCase = AddRegionDefaultsUseCaseImpl(repo),
        dataTypeToString = getTypeNameUseCase,
        prepareRightValueUseCase = prepareRightValueUseCase
    )

    @Test
    fun testKotlinNamespaceDeclarationPattern() {
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
        val files = fileGenerator.createFile(projectOutput, "a", block)
        val mainFile = files.first()
        ktConstantsGenerator(files, block)
        val formatted = formatter(input = mainFile)
        // expected result
        // <FileData>
        //     <NamespaceDeclaration>
        //         <Keyword name="package"/>
        //         <Space>
        //         <VariableName name="com.goldman.xml"/>
        //         <NewLine/>
        //         <NewLine/>
        //     </NamespaceDeclaration>
        //     <region>
        // ...
        Assertions.assertEquals(2, formatted.subs.size)
        Assertions.assertEquals(NamespaceDeclaration::class.java, formatted.subs[0]::class.java)
        val namespaceDeclaration = formatted.subs[0] as NamespaceDeclaration
        Assertions.assertEquals(5, namespaceDeclaration.subs.size)
        Assertions.assertEquals(Keyword::class.java, namespaceDeclaration.subs[0]::class.java)
        Assertions.assertEquals("package", (namespaceDeclaration.subs[0] as Keyword).name)
        Assertions.assertEquals(Space::class.java, namespaceDeclaration.subs[1]::class.java)
        Assertions.assertEquals(VariableName::class.java, namespaceDeclaration.subs[2]::class.java)
    }
}