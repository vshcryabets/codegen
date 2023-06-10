package ce.gradle

import org.gradle.api.DefaultTask
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

abstract class CgenBuildTask : DefaultTask() {

    @get:Optional
    @get:Input
    abstract var cgenPath: Property<String?>

    @get:Optional
    @get:InputFiles
    abstract var files : ConfigurableFileCollection

    @get:Optional
    @get:OutputDirectory
    abstract var outputFolder : DirectoryProperty

    @get:Optional
    @get:OutputDirectory
    abstract var inputFolder : DirectoryProperty

    init {
        cgenPath.convention("hello from CgenBuildTask")
    }

    @TaskAction
    fun greet() {
        println("hello from CgenBuildTask ${cgenPath.get()} " +
                "inputFolder=${inputFolder.get().toString()}" +
                "files=${files.files.toString()}, " +
                "out=${outputFolder.get().toString()}")
    }
}
