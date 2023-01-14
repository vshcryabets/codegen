package ce.domain.usecase.transform

import ce.defs.Target
import ce.defs.globRootNamespace
import ce.defs.namespaceMap
import ce.settings.Project
import generators.cpp.*
import generators.java.*
import generators.kotlin.*
import generators.obj.Generator
import generators.obj.MetaGenerator
import generators.obj.input.*
import generators.obj.out.ProjectOutput
import generators.rust.*
import generators.swift.*

class TransformInTreeToOutTreeUseCase {
    operator fun invoke(inTree: Node, target: Target, project: Project) : ProjectOutput {
        val kotlinFileGenerator = KotlinFileGenerator(project.codeStyle)
        val cppFileGenerator = CppFileGenerator(project.codeStyle)
        val swiftFileGenerator = SwiftFileGenerator(project.codeStyle)
        val rustFileGenerator = RustFileGenerator(project.codeStyle)
        val javaFileGenerator = JavaFileGenerator(project.codeStyle)

        val kotlinGenerators : Map<Class<out Block>, Generator<out Block>> = mapOf(
            ConstantsEnum::class.java to KotlinEnumGenerator(kotlinFileGenerator, project),
            ConstantsBlock::class.java to KtConstantsGenerator(kotlinFileGenerator, project),
            DataClass::class.java to KtDataClassGenerator(kotlinFileGenerator, project),
            InterfaceDescription::class.java to KotlinInterfaceGenerator(kotlinFileGenerator, project)
        )
        val kotlinMeta = MetaGenerator<KotlinClassData>(
            target = Target.Kotlin,
            writter = KotlinWritter(kotlinFileGenerator, project.outputFolder),
            project = project,
            fileGenerator = kotlinFileGenerator,
            generatorsMap = kotlinGenerators
        )

        val cppGenerators : Map<Class<out Block>, Generator<out Block>> = mapOf(
            ConstantsEnum::class.java to CppEnumGenerator(cppFileGenerator, project),
            ConstantsBlock::class.java to CppConstantsBlockGenerator(cppFileGenerator, project),
            DataClass::class.java to CppDataClassGenerator(cppFileGenerator, project)
        )
        val cppMeta = MetaGenerator<CppClassData>(
            target = Target.Cxx,
            writter = CppWritter(cppFileGenerator, project.outputFolder),
            project = project,
            fileGenerator = cppFileGenerator,
            generatorsMap = cppGenerators
        )

        val swiftGenerators : Map<Class<out Block>, Generator<out Block>> = mapOf(
            ConstantsEnum::class.java to SwiftEnumGenerator(swiftFileGenerator, project),
            ConstantsBlock::class.java to SwiftConstantsBlockGenerator(swiftFileGenerator, project),
            DataClass::class.java to SwiftDataClassGenerator(swiftFileGenerator, project)
        )
        val swiftMeta = MetaGenerator<SwiftClassData>(
            target = Target.Swift,
            writter = SwiftWritter(swiftFileGenerator, project.outputFolder),
            project = project,
            fileGenerator = swiftFileGenerator,
            generatorsMap = swiftGenerators
        )

        val rustGenerators : Map<Class<out Block>, Generator<out Block>> = mapOf(
            ConstantsEnum::class.java to RustEnumGenerator(rustFileGenerator, project),
            ConstantsBlock::class.java to RsConstantsBlockGenerator(rustFileGenerator, project),
            DataClass::class.java to RsDataClassGenerator(rustFileGenerator, project)
        )
        val rustMeta = MetaGenerator<RustClassData>(
            target = Target.Rust,
            writter = RustWritter(rustFileGenerator, project.outputFolder),
            project = project,
            fileGenerator = rustFileGenerator,
            generatorsMap = rustGenerators
        )

        val javaGenerators : Map<Class<out Block>, Generator<out Block>> = mapOf(
            ConstantsEnum::class.java to JavaEnumGenerator(javaFileGenerator, project),
            ConstantsBlock::class.java to JavaConstantsGenerator(javaFileGenerator, project),
            DataClass::class.java to JavaDataClassGenerator(javaFileGenerator, project)
        )
        val javaMeta = MetaGenerator<JavaClassData>(
            target = Target.Java,
            writter = JavaWritter(javaFileGenerator, project.outputFolder),
            project = project,
            fileGenerator = javaFileGenerator,
            generatorsMap = javaGenerators
        )

        val supportedMeta = mapOf(
            Target.Kotlin to kotlinMeta,
            Target.Cxx to cppMeta,
            Target.Swift to swiftMeta,
            Target.Rust to rustMeta,
            Target.Java to javaMeta
        )

        val meta = supportedMeta[target]

        // build output tree and generate code
        return meta!!.processProject(inTree, namespaceMap)
    }
}