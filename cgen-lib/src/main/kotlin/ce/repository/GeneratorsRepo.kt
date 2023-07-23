package ce.repository

import ce.defs.Target
import ce.domain.usecase.add.AddBlockDefaultsUseCaseImpl
import ce.formatters.CLikeCodestyleRepo
import ce.settings.Project
import generators.cpp.CppConstantsBlockGenerator
import generators.cpp.CppDataClassGenerator
import generators.cpp.CppEnumGenerator
import generators.cpp.CppFileGenerator
import generators.cpp.CppWritter
import generators.java.JavaConstantsGenerator
import generators.java.JavaDataClassGenerator
import generators.java.JavaEnumGenerator
import generators.java.JavaFileGenerator
import generators.java.JavaWritter
import generators.kotlin.KotlinEnumGenerator
import generators.kotlin.KotlinFileGenerator
import generators.kotlin.KotlinInterfaceGenerator
import generators.kotlin.KotlinWritter
import generators.kotlin.KtConstantsGenerator
import generators.kotlin.KtDataClassGenerator
import generators.obj.TransformBlockUseCase
import generators.obj.MetaGenerator
import generators.obj.input.Block
import generators.obj.input.ConstantsBlock
import generators.obj.input.ConstantsEnum
import generators.obj.input.DataClass
import generators.obj.input.InterfaceDescription
import generators.rust.RsConstantsBlockGenerator
import generators.rust.RsDataClassGenerator
import generators.rust.RustEnumGenerator
import generators.rust.RustFileGenerator
import generators.rust.RustWritter
import generators.swift.SwiftConstantsBlockGenerator
import generators.swift.SwiftDataClassGenerator
import generators.swift.SwiftEnumGenerator
import generators.swift.SwiftFileGenerator
import generators.swift.SwiftWritter

class GeneratorsRepo(val project: Project) {
    val supportedMeta : Map<Target, MetaGenerator>

    init {
        val clikeCodeStyleRepo = CLikeCodestyleRepo(project.codeStyle)

        val kotlinFileGenerator = KotlinFileGenerator(project.codeStyle)
        val cppFileGenerator = CppFileGenerator(project.codeStyle)
        val swiftFileGenerator = SwiftFileGenerator(project.codeStyle)
        val rustFileGenerator = RustFileGenerator(project.codeStyle)
        val javaFileGenerator = JavaFileGenerator(project.codeStyle)

        val kotlinAddBlockDefaultsUseCase = AddBlockDefaultsUseCaseImpl(clikeCodeStyleRepo)
        val cppAddBlockDefaultsUseCase = AddBlockDefaultsUseCaseImpl(clikeCodeStyleRepo)
        val javaAddBlockDefaultsUseCase = AddBlockDefaultsUseCaseImpl(clikeCodeStyleRepo)

        val kotlinGenerators : Map<Class<out Block>, TransformBlockUseCase<out Block>> = mapOf(
            ConstantsEnum::class.java to KotlinEnumGenerator(kotlinFileGenerator, kotlinAddBlockDefaultsUseCase),
            ConstantsBlock::class.java to KtConstantsGenerator(kotlinFileGenerator, kotlinAddBlockDefaultsUseCase),
            DataClass::class.java to KtDataClassGenerator(kotlinFileGenerator, kotlinAddBlockDefaultsUseCase),
            InterfaceDescription::class.java to KotlinInterfaceGenerator(kotlinFileGenerator, kotlinAddBlockDefaultsUseCase)
        )
        val kotlinMeta = MetaGenerator(
            target = Target.Kotlin,
            writter = KotlinWritter(clikeCodeStyleRepo, project.outputFolder),
            project = project,
            fileGenerator = kotlinFileGenerator,
            generatorsMap = kotlinGenerators
        )

        val cppGenerators : Map<Class<out Block>, TransformBlockUseCase<out Block>> = mapOf(
            ConstantsEnum::class.java to CppEnumGenerator(cppFileGenerator, cppAddBlockDefaultsUseCase),
            ConstantsBlock::class.java to CppConstantsBlockGenerator(cppAddBlockDefaultsUseCase),
            DataClass::class.java to CppDataClassGenerator(clikeCodeStyleRepo, cppAddBlockDefaultsUseCase)
        )
        val cppMeta = MetaGenerator(
            target = Target.Cxx,
            writter = CppWritter(clikeCodeStyleRepo, project.outputFolder),
            project = project,
            fileGenerator = cppFileGenerator,
            generatorsMap = cppGenerators
        )

        val swiftGenerators : Map<Class<out Block>, TransformBlockUseCase<out Block>> = mapOf(
            ConstantsEnum::class.java to SwiftEnumGenerator(swiftFileGenerator, project),
            ConstantsBlock::class.java to SwiftConstantsBlockGenerator(swiftFileGenerator, project),
            DataClass::class.java to SwiftDataClassGenerator(swiftFileGenerator, project)
        )
        val swiftMeta = MetaGenerator(
            target = Target.Swift,
            writter = SwiftWritter(swiftFileGenerator, project.codeStyle, project.outputFolder),
            project = project,
            fileGenerator = swiftFileGenerator,
            generatorsMap = swiftGenerators
        )

        val rustGenerators : Map<Class<out Block>, TransformBlockUseCase<out Block>> = mapOf(
            ConstantsEnum::class.java to RustEnumGenerator(rustFileGenerator, project),
            ConstantsBlock::class.java to RsConstantsBlockGenerator(rustFileGenerator, project),
            DataClass::class.java to RsDataClassGenerator(rustFileGenerator, project)
        )
        val rustMeta = MetaGenerator(
            target = Target.Rust,
            writter = RustWritter(rustFileGenerator, project.codeStyle, project.outputFolder),
            project = project,
            fileGenerator = rustFileGenerator,
            generatorsMap = rustGenerators
        )

        val javaGenerators : Map<Class<out Block>, TransformBlockUseCase<out Block>> = mapOf(
            ConstantsEnum::class.java to JavaEnumGenerator(javaFileGenerator, javaAddBlockDefaultsUseCase),
            ConstantsBlock::class.java to JavaConstantsGenerator(javaFileGenerator, javaAddBlockDefaultsUseCase),
            DataClass::class.java to JavaDataClassGenerator(javaFileGenerator, javaAddBlockDefaultsUseCase)
        )
        val javaMeta = MetaGenerator(
            target = Target.Java,
            writter = JavaWritter(clikeCodeStyleRepo, project.outputFolder),
            project = project,
            fileGenerator = javaFileGenerator,
            generatorsMap = javaGenerators
        )

        supportedMeta = mapOf(
            Target.Kotlin to kotlinMeta,
            Target.Cxx to cppMeta,
            Target.Swift to swiftMeta,
            Target.Rust to rustMeta,
            Target.Java to javaMeta
        )
    }

    fun get(target: Target): MetaGenerator {
        return supportedMeta[target]!!
    }
}