plugins {
    kotlin("jvm") version Versions.kotlin
    id("org.jetbrains.compose") version Versions.compose
    application
}

group = "com.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    google()
    maven { url = uri("https://jitpack.io") }
}

buildscript {
    repositories {
        maven {
            url = uri("https://jitpack.io")
        }
    }
    dependencies {
        classpath("com.github.vshcryabets:codegen:238769ae06")
//        classpath(project(":cgen-lib"))
    }
}


abstract class CgenBuildTask2 : DefaultTask() {

    private var outputFolder = File("")

    @Optional
    @OutputDirectory
    fun getOutputFolder() : File = outputFolder

    fun setOutputFolder(folder: File) {
        this.outputFolder = folder
    }

//    @get:Optional
//    @get:InputFiles
//    abstract var files : ConfigurableFileCollection


//    @get:Optional
//    @get:InputFile
//    abstract var projectFile : RegularFileProperty

    init {
//        projectFile.set(File(""))
    }

    @TaskAction
    fun greet() {
//        println("Project file = ${projectFile.get().toString()}")

        println("ASD hello from CgenBuildTask " +
                "out=${outputFolder.toString()}")
//        +
//                "inputFolder=${inputFolder.get().toString()}" +
//                "files=${files.files.toString()}, " +
//                )
    }
}


// Create a task using the task type
tasks.register<CgenBuildTask2>("hello") {
    setOutputFolder(File("./generated/"))
//    projectFile.set(File("../test/project.json"))
//    files.files.addAll(
//
//    )
}