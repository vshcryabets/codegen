package ce.basetest

import ce.formatters.CLikeCodestyleRepo
import ce.formatters.CodeFormatterKotlinUseCaseImpl
import ce.formatters.PrepareCodeStyleTreeUseCase
import ce.formatters.PrepareCodeStyleTreeUseCaseImpl
import ce.settings.CodeStyle
import ce.treeio.XmlTreeReader
import generators.kotlin.GetArrayDataTypeUseCase
import generators.kotlin.GetTypeNameUseCase
import generators.kotlin.KotlinFileGenerator
import generators.kotlin.PrepareRightValueUseCase

open class KotlinBaseTest {
    val xmlReader = XmlTreeReader()
    val fileGenerator = KotlinFileGenerator()
    val codeStyle = CodeStyle(
        newLinesBeforeClass = 1,
        tabSize = 2,
        preventEmptyBlocks = true,
    )
    val arrayDataType = GetArrayDataTypeUseCase()
    val getTypeNameUseCase = GetTypeNameUseCase(arrayDataType)
    val repo = CLikeCodestyleRepo(codeStyle)
    val formatter = CodeFormatterKotlinUseCaseImpl(repo)
    val prepareRightValueUseCase = PrepareRightValueUseCase(getTypeNameUseCase)
    val prepareCodeStyleTreeUseCase: PrepareCodeStyleTreeUseCase =
        PrepareCodeStyleTreeUseCaseImpl(formatter)
}