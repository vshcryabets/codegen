plugins {
    kotlin("jvm")
    `java-library`
    `maven-publish`
    application
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":cgen-lib"))
    implementation(project(":cgen-nnparser"))
    implementation(project(":cgen-parser"))
    testImplementation(libs.junitJupiterApi)
    testRuntimeOnly(libs.junitJupiterEngine)

    // script engines
    implementation(libs.kotlinScriptingJsr223)
    implementation(libs.kotlinCompilerEmbedable)
    implementation(libs.groovyJsr223)
}

kotlin { jvmToolchain(libs.versions.jvmLevel.get().toInt()) }
java { toolchain { languageVersion.set(JavaLanguageVersion.of(libs.versions.jvmLevel.get().toInt())) } }

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}
publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}
//
//publishing {
//    repositories {
//        maven {
//            name = "GitHubPackages"
//            url = uri("https://maven.pkg.github.com/vshcryabets/codegen")
//            credentials {
//                username = project.findProperty("gpr.user") as String? ?: System.getenv("USERNAME")
//                password = project.findProperty("gpr.key") as String? ?: System.getenv("TOKEN")
//            }
//        }
//    }
//    publications {
//        register<MavenPublication>("gpr") {
//            from(components["java"])
//        }
//    }
//}

task("execute", JavaExec::class) {
    mainClass.set(project.gradle.startParameter.projectProperties.get("classToExecute"))
    group = "Run project"
    classpath = sourceSets["main"].runtimeClasspath
    setWorkingDir("../")
}

tasks.withType(Tar::class.java).configureEach {
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
}

tasks.withType(Zip::class.java).configureEach {
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
}

