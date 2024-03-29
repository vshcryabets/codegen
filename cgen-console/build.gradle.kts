import org.jetbrains.kotlin.kapt.cli.main

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
//    implementation("com.github.vshcryabets:codegen:daa6c19bcf")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
    implementation("com.opencsv:opencsv:5.7.1")
}

kotlin {
    jvmToolchain(Versions.jvmLevel)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(Versions.jvmLevel))
    }
}

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

