import ce.domain.usecase.entry.BuildProjectUseCase
import ce.domain.usecase.load.LoadMetaFilesForTargetUseCase
import ce.domain.usecase.load.LoadProjectUseCase
import ce.domain.usecase.store.StoreInTreeUseCase
import ce.domain.usecase.store.StoreOutTreeUseCase
import ce.domain.usecase.transform.TransformInTreeToOutTreeUseCase

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
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

kotlin {
    jvmToolchain(Versions.jvmLevel)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(Versions.jvmLevel))
    }
}

abstract class CgenProjectTask : DefaultTask() {

    private var outputFolder = File("")

    @OutputDirectory
    fun getOutputFolder() : File = outputFolder

    fun setOutputFolder(folder: File) {
        this.outputFolder = folder
    }

    @get: InputFile
    abstract val projectFile: RegularFileProperty

    @TaskAction
    fun execute() {
        println("CGEN: Project file = ${projectFile.get()}")
//        println("ASD hello from CgenBuildTask " +
//                "out=${outputFolder.toString()}")
        val buildProjectUseCase = BuildProjectUseCase(
            getProjectUseCase = LoadProjectUseCase(),
            storeInTreeUseCase = StoreInTreeUseCase(),
            loadMetaFilesUseCase = LoadMetaFilesForTargetUseCase(),
            storeOutTreeUseCase = StoreOutTreeUseCase(),
            transformInTreeToOutTreeUseCase = TransformInTreeToOutTreeUseCase(),
        )
        buildProjectUseCase(projectFile.get().toString())
    }
}


// Create a task using the task type
tasks.register<CgenProjectTask>("hello") {
    setOutputFolder(File("./generated2/"))
    projectFile.set(File("../test/project.json"))
}