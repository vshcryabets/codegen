package ce.repository

import ce.defs.Target
import ce.settings.Project
import generators.cpp.CppClassData
import generators.cpp.CppConstantsBlockGenerator
import generators.cpp.CppDataClassGenerator
import generators.cpp.CppEnumGenerator
import generators.cpp.CppFileGenerator
import generators.cpp.CppWritter
import generators.java.JavaClassData
import generators.java.JavaConstantsGenerator
import generators.java.JavaDataClassGenerator
import generators.java.JavaEnumGenerator
import generators.java.JavaFileGenerator
import generators.java.JavaWritter
import generators.kotlin.KotlinClassData
import generators.kotlin.KotlinEnumGenerator
import generators.kotlin.KotlinFileGenerator
import generators.kotlin.KotlinInterfaceGenerator
import generators.kotlin.KotlinWritter
import generators.kotlin.KtConstantsGenerator
import generators.kotlin.KtDataClassGenerator
import generators.obj.Generator
import generators.obj.MetaGenerator
import generators.obj.input.Block
import generators.obj.input.ConstantsBlock
import generators.obj.input.ConstantsEnum
import generators.obj.input.DataClass
import generators.obj.input.InterfaceDescription
import generators.rust.RsConstantsBlockGenerator
import generators.rust.RsDataClassGenerator
import generators.rust.RustClassData
import generators.rust.RustEnumGenerator
import generators.rust.RustFileGenerator
import generators.rust.RustWritter
import generators.swift.SwiftClassData
import generators.swift.SwiftConstantsBlockGenerator
import generators.swift.SwiftDataClassGenerator
import generators.swift.SwiftEnumGenerator
import generators.swift.SwiftFileGenerator
import generators.swift.SwiftWritter

class GeneratorsRepo(val project: Project) {
    val supportedMeta : Map<Target, MetaGenerator<*>>

    init {
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

        supportedMeta = mapOf(
            Target.Kotlin to kotlinMeta,
            Target.Cxx to cppMeta,
            Target.Swift to swiftMeta,
            Target.Rust to rustMeta,
            Target.Java to javaMeta
        )
    }

    fun get(target: Target): MetaGenerator<*> {
        return supportedMeta[target]!!
    }
}