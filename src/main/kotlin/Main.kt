import ce.defs.*
import ce.defs.Target
import ce.settings.Project
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import generators.obj.MetaGenerator
import generators.cpp.*
import generators.java.*
import generators.kotlin.*
import generators.obj.Generator
import generators.obj.input.*
import generators.rust.*
import generators.swift.*
import generators.swift.SwiftConstantsBlockGenerator
import okio.buffer
import okio.source
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader
import javax.script.ScriptEngineManager
import kotlin.reflect.KClass

fun main(args: Array<String>) {
    if (args.size < 1) {
        error("Specify project file!")
    }

    val engine = ScriptEngineManager().getEngineByExtension("kts")
    val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    val adapter: JsonAdapter<Project> = moshi.adapter(Project::class.java)

    // load project file
    val projectJson = FileInputStream(args[0])
    val project = adapter.fromJson(projectJson.source().buffer())!!
    projectJson.close()
    println(project)

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

    project.targets.forEach { target ->
        if (target in supportedMeta) {
            val meta = supportedMeta[target]!!
            println("Target $target")
            namespaceMap.clear()
            currentTarget = target
            globRootNamespace.subs.clear()

            project.files.forEach { fileName ->
                println("Processing $fileName")
                val fileObject = File(fileName)
                val reader = InputStreamReader(FileInputStream(fileObject))
                // clean global defines for each file
                globCurrentNamespace = globRootNamespace
                customBaseFolderPath = project.outputFolder
                sourceFile = fileObject.absolutePath
                outputFile = ""
                engine.eval(reader)
                reader.close()
            }
            meta.write(globRootNamespace, namespaceMap)
        } else {
            println("Not supported $target")
        }
    }
}
