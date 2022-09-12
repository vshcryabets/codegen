import ce.defs.*
import ce.defs.Target
import ce.settings.Project
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import generators.obj.MetaGenerator
import generators.cpp.*
import generators.kotlin.*
import generators.obj.input.Block
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
    val kotlinMeta = object : MetaGenerator<KotlinClassData>(
        target = Target.Kotlin,
        enum = KotlinEnumGenerator(kotlinFileGenerator, project),
        constantsBlock = ConstantsBlockGenerator(kotlinFileGenerator, project),
        writter = KotlinWritter(kotlinFileGenerator, project.outputFolder),
        project = project,
        fileGenerator = KotlinFileGenerator(project.codeStyle)
    ) {
        override fun getBlockFilePath(block: Block): String {
            var fileName = "${block.name}"
            if (block.outputFile.isNotEmpty()) {
                fileName = "${block.outputFile}"
            }
            val namespace = block.namespace.replace('.', File.separatorChar)
            return block.objectBaseFolder + File.separatorChar + namespace + File.separatorChar + fileName
        }
    }

    val cppFileGenerator = CppFileGenerator(project.codeStyle)
    val cppMeta = object : MetaGenerator<CppClassData>(
        target = Target.Cxx,
        enum = CppEnumGenerator(cppFileGenerator, project),
        constantsBlock = CppConstantsBlockGenerator(cppFileGenerator, project),
        writter = CppWritter(cppFileGenerator, project.outputFolder),
        project = project,
        fileGenerator = cppFileGenerator
    ) {
        override fun getBlockFilePath(block: Block): String {
            var fileName = "${block.name}"
            if (block.outputFile.isNotEmpty()) {
                fileName = "${block.outputFile}"
            }
            val namespace = block.namespace.replace('.', File.separatorChar)
            return block.objectBaseFolder + File.separatorChar + fileName
        }
    }

    val supportedMeta = mapOf(
        Target.Kotlin to kotlinMeta,
        Target.Cxx to cppMeta
    )

    project.targets.forEach { target ->
        if (target in supportedMeta) {
            val meta = supportedMeta[target]!!
            println("Target $target")
            ce.defs.currentTarget = target
            val objects = mutableListOf<Block>()

            project.files.forEach { fileName ->
                println("Processing $fileName")
                val fileObjects = mutableListOf<Block>()
                val reader = InputStreamReader(FileInputStream(fileName))
                // clean global defines for each file
                definedBloks.clear()
                namescpaceDef.setLength(0)
                customBaseFolderPath = project.outputFolder
                sourceFile = fileName
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