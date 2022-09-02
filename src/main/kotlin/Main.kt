import ce.settings.Project
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import generators.kotlin.ConstantsObjectGenerator
import generators.kotlin.KotlinWritter
import generators.obj.MetaGenerator
import ce.defs.Target
import ce.defs.customBaseFolderPath
import ce.defs.namescpaceDef
import generators.cpp.CppClassData
import generators.cpp.CppConstantsBlockGenerator
import generators.cpp.CppEnumGenerator
import generators.cpp.CppWritter
import generators.kotlin.ConstantsBlockGenerator
import generators.kotlin.KotlinClassData
import generators.obj.input.ClassDescription
import okio.buffer
import okio.source
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

    val kotlinMeta = MetaGenerator<KotlinClassData>(
        target = Target.Kotlin,
        enum = ConstantsObjectGenerator(project.codeStyle, project),
        constantsBlock = ConstantsBlockGenerator(project.codeStyle, project),
        writter = KotlinWritter(project.codeStyle, project.outputFolder)
    )

    val cppMeta = MetaGenerator<CppClassData>(
        target = Target.Cxx,
        enum = CppEnumGenerator(project.codeStyle, project),
        constantsBlock = CppConstantsBlockGenerator(project.codeStyle, project),
        writter = CppWritter(project.codeStyle, project.outputFolder)
    )

    val supportedMeta = mapOf(
        Target.Kotlin to kotlinMeta,
        Target.Cxx to cppMeta
    )

    project.enumFiles.forEach { fileName ->
        println("Processing $fileName")
        project.targets.forEach { target ->
            if (target in supportedMeta) {
                val meta = supportedMeta[target]!!
                println("Target $target")
                val reader = InputStreamReader(FileInputStream(fileName))
                // clean global defines for each file
                namescpaceDef.setLength(0)
                customBaseFolderPath = project.outputFolder
                val obj = engine.eval(reader)
                reader.close()
                ce.defs.currentTarget = target
                if (obj is Array<*>) {
                    obj.forEach {
                        meta.writeItem(it as ClassDescription, customBaseFolderPath)
                    }
                } else if (obj is List<*>) {
                    obj.forEach {
                        meta.writeItem(it as ClassDescription, customBaseFolderPath)
                    }
                } else {
                    meta.writeItem(obj as ClassDescription, customBaseFolderPath)
                }
            } else {
                println("Not supported $target")
            }
        }
    }
}