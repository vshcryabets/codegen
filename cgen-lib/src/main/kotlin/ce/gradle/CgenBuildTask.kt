package ce.gradle

import org.gradle.api.DefaultTask
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

abstract class CgenBuildTask : DefaultTask() {

    @get:Input
    abstract val cgenPath: Property<String>

    @get:InputFiles
    abstract val files : ConfigurableFileCollection

    @get:OutputDirectory
    abstract val outputFolder : DirectoryProperty

    init {
        cgenPath.convention("hello from CgenBuildTask")
    }

    @TaskAction
    fun greet() {
        println("hello from CgenBuildTask ${cgenPath.get()} " +
                "files=${files.toList()}, " +
                "out=${outputFolder.get().toString()}")
    }
}
