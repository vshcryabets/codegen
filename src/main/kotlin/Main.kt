import ce.settings.Project
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import generators.obj.MetaGenerator
import ce.defs.Target
import ce.defs.customBaseFolderPath
import ce.defs.namescpaceDef
import ce.defs.sourceFile
import generators.cpp.*
import generators.kotlin.*
import generators.obj.input.Block
import generators.obj.input.ConstantsEnum
import generators.obj.out.FileData
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
        enum = ConstantsObjectGenerator(kotlinFileGenerator, project),
        constantsBlock = ConstantsBlockGenerator(kotlinFileGenerator, project),
        writter = KotlinWritter(kotlinFileGenerator, project.outputFolder),
        project = project,
        fileGenerator = KotlinFileGenerator(project.codeStyle)
    ) {
        override fun getBlockFilePath(block: Block): String {
            var fileName = "${block.name}.kt"
            if (block.outputFile.isNotEmpty()) {
                fileName = "${block.outputFile}.kt"
            }
            val namespace = block.namespace.replace('.', File.separatorChar)
            return block.objectBaseFolder + File.separatorChar + namespace + File.separatorChar + fileName
        }
    }

//    val cppMeta = object : MetaGenerator<CppClassData>(
//        target = Target.Cxx,
//        enum = CppEnumGenerator(project.codeStyle, project),
//        constantsBlock = CppConstantsBlockGenerator(project.codeStyle, project),
//        writter = CppWritter(project.codeStyle, project.outputFolder),
//        project = project
//    ) {
//        override fun getBlockFilePath(block: Block): String {
//            var fileName = "${block.name}.cpp"
//            if (block.outputFile.isNotEmpty()) {
//                fileName = "${block.outputFile}.cpp"
//            }
//            val namespace = block.namespace.replace('.', File.separatorChar)
//            return block.objectBaseFolder + File.separatorChar + fileName
//        }
//    }

    val supportedMeta = mapOf(
        Target.Kotlin to kotlinMeta,
        //Target.Cxx to cppMeta
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
                namescpaceDef.setLength(0)
                customBaseFolderPath = project.outputFolder
                sourceFile = fileName
                val obj = engine.eval(reader)
                reader.close()
                if (obj is Array<*>) {
                    obj.forEach {
                        if (it is Block) {
                            fileObjects.add(it)
                        }
                    }
                } else if (obj is List<*>) {
                    obj.forEach {
                        if (it is Block) {
                            fileObjects.add(it)
                        }
                    }
                } else {
                    fileObjects.add(obj as Block)
                }
                objects.addAll(fileObjects)
            }
            meta.write(objects)
        } else {
            println("Not supported $target")
        }
    }
}