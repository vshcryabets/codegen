package ce.gradle

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

abstract class GreetingTask : DefaultTask() {
    @TaskAction
    fun greet() {
        println("hello from GreetingTask")
    }
}
