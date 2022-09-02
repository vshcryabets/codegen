import ce.settings.Project
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import generators.kotlin.ConstantsObjectGenerator
import generators.kotlin.KotlinWritter
import generators.obj.MetaGenerator
import ce.defs.Target
import ce.defs.constantsBlock
import generators.kotlin.ConstantsBlockGenerator
import generators.obj.input.ClassDescription
import generators.obj.input.ConstantsBlock
import generators.obj.input.ConstantsEnum
import okio.buffer
import okio.source
import java.io.FileInputStream
import java.io.InputStreamReader
import javax.script.ScriptEngineManager

fun writeItem(metagen: MetaGenerator, item: ClassDescription) {
    if (item is ConstantsEnum) {
        val classData = metagen.enum.build(item)
        metagen.writter.write(classData)
    }
    if (item is ConstantsBlock) {
        val classData = metagen.constantsBlock.build(item)
        metagen.writter.write(classData)
    }
}

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

    val kotlinMeta = MetaGenerator(
        target = Target.Kotlin,
        enum = ConstantsObjectGenerator(project.codeStyle, project),
        constantsBlock = ConstantsBlockGenerator(project.codeStyle, project),
        writter = KotlinWritter(project.codeStyle, project.outputFolder)
    )

    val supportedMeta = mapOf(
        Target.Kotlin to kotlinMeta
    )

    project.enumFiles.forEach { fileName ->
        println("Processing $fileName")
        project.targets.forEach { target ->
            if (target in supportedMeta) {
                val meta = supportedMeta[target]!!
                println("Target $target")
                val reader = InputStreamReader(FileInputStream(fileName))
                val obj = engine.eval(reader)
                reader.close()
                ce.defs.currentTarget = target
                if (obj is Array<*>) {
                    obj.forEach {
                        writeItem(meta, it as ClassDescription)
                    }
                } else if (obj is List<*>) {
                    obj.forEach {
                        writeItem(meta, it as ClassDescription)
                    }
                } else {
                    writeItem(meta, obj as ClassDescription)
                }
            } else {
                println("Not supported $target")
            }
        }
    }
}