package ce.gradle

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

abstract class GreetingTask : DefaultTask() {
    abstract val cgenPath : String

    @TaskAction
    fun greet() {
        println("hello from GreetingTask $cgenPath")
    }
}
