import ce.domain.usecase.entry.BuildProjectUseCase
import ce.domain.usecase.load.LoadMetaFilesForTargetUseCase
import ce.domain.usecase.load.LoadProjectUseCase
import ce.domain.usecase.load.LoadProjectUseCaseImpl
import ce.domain.usecase.store.StoreAstTreeUseCase
import ce.domain.usecase.store.StoreOutTreeUseCase
import ce.domain.usecase.transform.TransformInTreeToOutTreeUseCase
import ce.defs.domain.DirsConfiguration
import java.io.File
import javax.script.ScriptEngineManager
import kotlin.script.experimental.jsr223.KotlinJsr223DefaultScriptEngineFactory

plugins {
    kotlin("jvm")
//    id("org.jetbrains.compose")
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
        classpath("org.jetbrains.kotlin:kotlin-scripting-jsr223:${Versions.kotlin}")
        classpath("org.codehaus.groovy:groovy-jsr223:3.0.17")
//        classpath("org.jetbrains.kotlin:kotlin-scripting-dependencies-maven:${Versions.kotlin}")
//        classpath("org.jetbrains.kotlin:kotlin-maven-plugin:${Versions.kotlin}")
        classpath("com.github.vshcryabets:codegen:4895044cf9")
//        classpath(files("./libs/shadow.jar")) // Name of the JAR file without the `.jar` extension
    }
}

dependencies {
}

kotlin {
    jvmToolchain(Versions.jvmLevel)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(Versions.jvmLevel))
    }
}

task("testScriptEngines", DefaultTask::class) {
    val kotlinScriptEngine = ScriptEngineManager().getEngineByName("kts")
    //KotlinJsr223DefaultScriptEngineFactory().getScriptEngine()
    val groovyEngine = ScriptEngineManager().getEngineByName("groovy")
    println("KTS = $kotlinScriptEngine")
    println("Groovy engine = $groovyEngine")
}

tasks.register("runCgen") {
    group = "Custom"
    description = "Run code generation"

    doLast {
        val engineMaps = mapOf<ce.defs.MetaEngine, javax.script.ScriptEngine>(
            ce.defs.MetaEngine.KTS to ScriptEngineManager().getEngineByName("kts"),
            ce.defs.MetaEngine.GROOVY to ScriptEngineManager().getEngineByName("groovy")
        )
        val dirsConfiguration = DirsConfiguration(
            workingDir = rootDir.absolutePath //project.projectDir.absolutePath
        )
        println("Project dir = ${dirsConfiguration.workingDir}")
        val buildProjectUseCase = BuildProjectUseCase(
            getProjectUseCase = LoadProjectUseCaseImpl(),
            storeInTreeUseCase = StoreAstTreeUseCase(),
            loadMetaFilesUseCase = LoadMetaFilesForTargetUseCase(engineMaps),
            storeOutTreeUseCase = StoreOutTreeUseCase(),
            transformInTreeToOutTreeUseCase = TransformInTreeToOutTreeUseCase(),
        )
        buildProjectUseCase(
            projectFile = "./test/project.json",
            dirsConfiguration = dirsConfiguration)
    }
}