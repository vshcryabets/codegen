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
//        classpath("org.jetbrains.kotlin:kotlin-scripting-dependencies-maven:${Versions.kotlin}")
//        classpath("org.jetbrains.kotlin:kotlin-maven-plugin:${Versions.kotlin}")
        classpath("com.github.vshcryabets:codegen:cd0b4ed5c5")
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

//task("buildCgen2", DefaultTask::class) {
//    val kotlinScriptEngine = KotlinJsr223DefaultScriptEngineFactory().getScriptEngine()
//    val groovyEngine = ScriptEngineManager().getEngineByName("groovy")
//    val buildProjectUseCase = BuildProjectUseCase(
//        getProjectUseCase = LoadProjectUseCaseImpl(),
//        storeInTreeUseCase = StoreAstTreeUseCase(),
//        loadMetaFilesUseCase = LoadMetaFilesForTargetUseCase(
//            groovyEngine,
//            kotlinScriptEngine
//        ),
//        storeOutTreeUseCase = StoreOutTreeUseCase(),
//        transformInTreeToOutTreeUseCase = TransformInTreeToOutTreeUseCase(),
//    )
//    buildProjectUseCase("./test/project.json")
//}