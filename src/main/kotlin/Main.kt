import ce.defs.*
import ce.defs.Target
import ce.settings.Project
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import generators.obj.MetaGenerator
import generators.cpp.*
import generators.kotlin.KotlinClassData
import generators.kotlin.KotlinEnumGenerator
import generators.kotlin.KotlinFileGenerator
import generators.kotlin.KotlinWritter
import generators.obj.input.Block
import generators.rust.*
import generators.swift.*
import generators.swift.SwiftConstantsBlockGenerator
import okio.buffer
import okio.source
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader
import javax.script.ScriptEngineManager

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

    val kotlinMeta = MetaGenerator<KotlinClassData>(
        target = Target.Kotlin,
        enum = KotlinEnumGenerator(kotlinFileGenerator, project),
        constantsBlock = generators.kotlin.KtConstantsGenerator(kotlinFileGenerator, project),
        dataClass = generators.kotlin.KtDataClassGenerator(kotlinFileGenerator, project),
        writter = KotlinWritter(kotlinFileGenerator, project.outputFolder),
        project = project,
        fileGenerator = kotlinFileGenerator
    )

    val cppMeta = MetaGenerator<CppClassData>(
        target = Target.Cxx,
        enum = CppEnumGenerator(cppFileGenerator, project),
        constantsBlock = CppConstantsBlockGenerator(cppFileGenerator, project),
        writter = CppWritter(cppFileGenerator, project.outputFolder),
        dataClass = CppDataClassGenerator(cppFileGenerator, project),
        project = project,
        fileGenerator = cppFileGenerator
    )

    val swiftMeta = MetaGenerator<SwiftClassData>(
        target = Target.Swift,
        enum = SwiftEnumGenerator(swiftFileGenerator, project),
        constantsBlock = SwiftConstantsBlockGenerator(swiftFileGenerator, project),
        dataClass = SwiftDataClassGenerator(cppFileGenerator, project),
        writter = SwiftWritter(swiftFileGenerator, project.outputFolder),
        project = project,
        fileGenerator = swiftFileGenerator
    )

    val rustMeta = MetaGenerator<RustClassData>(
        target = Target.Rust,
        enum = RustEnumGenerator(rustFileGenerator, project),
        constantsBlock = generators.rust.RsConstantsBlockGenerator(rustFileGenerator, project),
        dataClass = RsDataClassGenerator(cppFileGenerator, project),
        writter = RustWritter(rustFileGenerator, project.outputFolder),
        project = project,
        fileGenerator = rustFileGenerator
    )

    val supportedMeta = mapOf(
        Target.Kotlin to kotlinMeta,
        Target.Cxx to cppMeta,
        Target.Swift to swiftMeta,
        Target.Rust to rustMeta
    )

    project.targets.forEach { target ->
        if (target in supportedMeta) {
            val meta = supportedMeta[target]!!
            println("Target $target")
            ce.defs.currentTarget = target
            val objects = mutableListOf<Block>()

            project.files.forEach { fileName ->
                println("Processing $fileName")
                val fileObject = File(fileName)
                val reader = InputStreamReader(FileInputStream(fileObject))
                // clean global defines for each file
                definedBloks.clear()
                namescpaceDef.setLength(0)
                customBaseFolderPath = project.outputFolder
                sourceFile = fileObject.absolutePath
                outputFile = ""
                engine.eval(reader)
                reader.close()
                objects.addAll(definedBloks)
            }
            meta.write(objects)
        } else {
            println("Not supported $target")
        }
    }
}
