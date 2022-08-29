import ce.settings.Project
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import genrators.kotlin.ConstantsObjectGenrator
import genrators.obj.ConstantsEnum
import okio.buffer
import okio.source
import java.io.FileInputStream
import java.io.InputStreamReader
import javax.script.ScriptEngineManager

fun main(args: Array<String>) {

    if (args.size < 1) {
        error("Specify project file!")
        System.exit(255)
    }

    val engine = ScriptEngineManager().getEngineByExtension("kts")
    val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    val adapter: JsonAdapter<Project> = moshi.adapter(Project::class.java)
//    val enumAdapter: JsonAdapter<ConstantsEnum> = moshi.adapter(ConstantsEnum::class.java)
//
    // load project file
    val projectJson = FileInputStream(args[0])
    val project = adapter.fromJson(projectJson.source().buffer())!!
    projectJson.close()
    println(project)

    val generator = ConstantsObjectGenrator()
    project.enumFiles.forEach {
        println("Processing $it")
        val reader = InputStreamReader(FileInputStream(it))
        val obj = engine.eval(reader)
        reader.close()
//        println(obj)
        if (obj is ConstantsEnum) {
            generator.build(obj)
        }
    }
}