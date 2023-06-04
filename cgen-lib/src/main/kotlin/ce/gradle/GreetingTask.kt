package ce.gradle

import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

abstract class GreetingTask : DefaultTask() {
    @get:Input
    abstract val cgenPath: Property<String>

    init {
        cgenPath.convention("hello from GreetingTask")
    }

    @TaskAction
    fun greet() {
        println("hello from GreetingTask ${cgenPath.get()}")
    }
}
