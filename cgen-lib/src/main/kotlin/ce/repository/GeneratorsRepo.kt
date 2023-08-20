package ce.repository

import ce.defs.Target
import ce.domain.usecase.add.AddRegionDefaultsUseCaseImpl
import ce.formatters.CLikeCodestyleRepo
import ce.formatters.CodeFormatterCxxUseCaseImpl
import ce.formatters.CodeFormatterKotlinUseCaseImpl
import ce.formatters.CodeFormatterUseCaseImpl
import ce.settings.Project
import generators.cpp.CppConstantsBlockGenerator
import generators.cpp.CppDataClassGenerator
import generators.cpp.CppEnumGenerator
import generators.cpp.CppFileGenerator
import generators.cpp.CppWritter
import generators.java.JavaFileGenerator
import generators.kotlin.KotlinEnumGenerator
import generators.kotlin.KotlinFileGenerator
import generators.kotlin.KotlinInterfaceGenerator
import generators.kotlin.KotlinWritter
import generators.kotlin.KtConstantsGenerator
import generators.kotlin.KtDataClassGenerator
import generators.obj.MetaGenerator
import generators.obj.PrepareFilesListUseCaseImpl
import generators.obj.input.ConstantsBlock
import generators.obj.input.ConstantsEnum
import generators.obj.input.DataClass
import generators.obj.input.InterfaceDescription
import generators.rust.RustFileGenerator
import generators.swift.SwiftConstantsBlockGenerator
import generators.swift.SwiftDataClassGenerator
import generators.swift.SwiftEnumGenerator
import generators.swift.SwiftFileGenerator

class GeneratorsRepo(val project: Project) {
    val supportedMeta: Map<Target, MetaGenerator>

    init {
        val clikeCodeStyleRepo = CLikeCodestyleRepo(project.codeStyle)

        val targets = listOf(
            Target.Kotlin,
            Target.Cxx,
        )

        val fileGeneratorsMap = mapOf(
            Target.Kotlin to KotlinFileGenerator(project.codeStyle),
            Target.Cxx to CppFileGenerator(project.codeStyle),
            Target.Swift to SwiftFileGenerator(project.codeStyle),
            Target.Rust to RustFileGenerator(project.codeStyle),
            Target.Java to JavaFileGenerator(project.codeStyle),
        )

        val codestylesMap = targets.map {
            it to clikeCodeStyleRepo
        }.toMap()

        val codeFormatters = mapOf(
            Target.Kotlin to CodeFormatterKotlinUseCaseImpl(clikeCodeStyleRepo),
            Target.Cxx to CodeFormatterCxxUseCaseImpl(clikeCodeStyleRepo),
//            Target.Swift to SwiftWritter(codestylesMap[Target.Swift]!!, project.outputFolder),
//            Target.Rust to RustWritter(codestylesMap[Target.Rust]!!, project.outputFolder),
//            Target.Java to JavaWritter(codestylesMap[Target.Java]!!, project.outputFolder),
        )

        val writtersMap = mapOf(
            Target.Kotlin to KotlinWritter(codestylesMap[Target.Kotlin]!!, project.outputFolder),
            Target.Cxx to CppWritter(codestylesMap[Target.Cxx]!!, project.outputFolder),
//            Target.Swift to SwiftWritter(codestylesMap[Target.Swift]!!, project.outputFolder),
//            Target.Rust to RustWritter(codestylesMap[Target.Rust]!!, project.outputFolder),
//            Target.Java to JavaWritter(codestylesMap[Target.Java]!!, project.outputFolder),
        )

        val addBlockDefaultsUseCases = targets.map {
            it to AddRegionDefaultsUseCaseImpl(codestylesMap[it]!!)
        }.toMap()

        val prepareFilesListUseCases = targets.map {
            it to PrepareFilesListUseCaseImpl(project, fileGeneratorsMap[it]!!)
        }.toMap()


        supportedMeta = targets.map {
            val fileGenerator = fileGeneratorsMap[it]!!
            val addBlockUseCase = addBlockDefaultsUseCases[it]!!
            val generators = when (it) {
                Target.Kotlin -> mapOf(
                    ConstantsEnum::class.java to KotlinEnumGenerator(addBlockUseCase),
                    ConstantsBlock::class.java to KtConstantsGenerator(addBlockUseCase),
                    DataClass::class.java to KtDataClassGenerator(fileGenerator, addBlockUseCase),
                    InterfaceDescription::class.java to KotlinInterfaceGenerator(fileGenerator, addBlockUseCase)
                )

                Target.Cxx -> mapOf(
                    ConstantsEnum::class.java to CppEnumGenerator(addBlockUseCase),
                    ConstantsBlock::class.java to CppConstantsBlockGenerator(addBlockUseCase),
                    DataClass::class.java to CppDataClassGenerator(clikeCodeStyleRepo, addBlockUseCase)
                )

                Target.Swift -> mapOf(
                    ConstantsEnum::class.java to SwiftEnumGenerator(fileGenerator, project),
                    ConstantsBlock::class.java to SwiftConstantsBlockGenerator(fileGenerator, project),
                    DataClass::class.java to SwiftDataClassGenerator(fileGenerator, project)
                )

                else -> throw IllegalStateException("Not supported target $it")
            }
            it to MetaGenerator(
                target = it,
                writter = writtersMap[it]!!,
                fileGenerator = fileGenerator,
                generatorsMap = generators,
                prepareFilesListUseCase = prepareFilesListUseCases[it]!!,
                codeFormatter = codeFormatters[it]!!,
            )
        }.toMap()



//        val rustGenerators : Map<Class<out Block>, TransformBlockUseCase<out Block>> = mapOf(
//            ConstantsEnum::class.java to RustEnumGenerator(rustFileGenerator, project),
//            ConstantsBlock::class.java to RsConstantsBlockGenerator(rustFileGenerator, project),
//            DataClass::class.java to RsDataClassGenerator(rustFileGenerator, project)
//        )
//        val rustMeta = MetaGenerator(
//            target = Target.Rust,
//            writter = RustWritter(rustFileGenerator, clikeCodeStyleRepo, project.outputFolder),
//            project = project,
//            fileGenerator = rustFileGenerator,
//            generatorsMap = rustGenerators,
//            codeStyleRepo = clikeCodeStyleRepo
//        )
//
//        val javaGenerators : Map<Class<out Block>, TransformBlockUseCase<out Block>> = mapOf(
//            ConstantsEnum::class.java to JavaEnumGenerator(javaFileGenerator, javaAddBlockDefaultsUseCase),
//            ConstantsBlock::class.java to JavaConstantsGenerator(javaFileGenerator, javaAddBlockDefaultsUseCase),
//            DataClass::class.java to JavaDataClassGenerator(javaFileGenerator, javaAddBlockDefaultsUseCase)
//        )
//        val javaMeta = MetaGenerator(
//            target = Target.Java,
//            writter = JavaWritter(clikeCodeStyleRepo, project.outputFolder),
//            project = project,
//            fileGenerator = javaFileGenerator,
//            generatorsMap = javaGenerators,
//            codeStyleRepo = clikeCodeStyleRepo
//        )
    }

    fun get(target: Target): MetaGenerator {
        return supportedMeta[target]!!
    }
}