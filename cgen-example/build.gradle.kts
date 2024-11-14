import ce.domain.usecase.entry.BuildProjectUseCase
import ce.domain.usecase.load.LoadMetaFilesForTargetUseCase
import ce.domain.usecase.load.LoadProjectUseCase
import ce.domain.usecase.load.LoadProjectUseCaseImpl
import ce.domain.usecase.store.StoreAstTreeUseCase
import ce.domain.usecase.store.StoreOutTreeUseCase
import ce.domain.usecase.transform.TransformInTreeToOutTreeUseCase
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
        classpath("com.github.vshcryabets:codegen:feature~gradle-task2-SNAPSHOT")
    }
}

dependencies {
//    api(project(":cgen-lib"))
//    api(project(":cgen-console"))
//    implementation("com.github.vshcryabets:cgen-lib:cafbd0e2b3")
}

kotlin {
    jvmToolchain(Versions.jvmLevel)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(Versions.jvmLevel))
    }
}

//task("buildCgen1", JavaExec::class) {
//    workingDir(File("../"))
//    args("./test/project.json")
//    mainClass.set("ce.entrypoints.BuildProjectKt")
//    classpath = sourceSets["test"].runtimeClasspath
//}

task("testScriptEngines", DefaultTask::class) {
    val kotlinScriptEngine = ScriptEngineManager().getEngineByName("kts")
    //KotlinJsr223DefaultScriptEngineFactory().getScriptEngine()
    val groovyEngine = ScriptEngineManager().getEngineByName("groovy")
    println("KTS = $kotlinScriptEngine")
    println("Groovy engine = $groovyEngine")
}

task("buildCgen2", DefaultTask::class) {
    val engineMaps = mapOf<ce.defs.MetaEngine, javax.script.ScriptEngine>(
        ce.defs.MetaEngine.KTS to ScriptEngineManager().getEngineByName("kts"),
        ce.defs.MetaEngine.GROOVY to ScriptEngineManager().getEngineByName("groovy")
    )
    val buildProjectUseCase = BuildProjectUseCase(
        getProjectUseCase = LoadProjectUseCaseImpl(),
        storeInTreeUseCase = StoreAstTreeUseCase(),
        loadMetaFilesUseCase = LoadMetaFilesForTargetUseCase(engineMaps),
        storeOutTreeUseCase = StoreOutTreeUseCase(),
        transformInTreeToOutTreeUseCase = TransformInTreeToOutTreeUseCase(),
    )
    buildProjectUseCase("./test/project.json")
}