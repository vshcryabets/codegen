plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
    application
}

repositories {
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

dependencies {
    implementation(project(":cgen-lib"))
//    implementation("com.github.vshcryabets:codegen:daa6c19bcf")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
    implementation("com.opencsv:opencsv:5.7.1")

    implementation(compose.desktop.currentOs)
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

//tasks.jar { // could also be a new task rather than the default one
//    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
//
//    manifest {
//        attributes["Main-Class"] = "ce.entrypoints.BuildProjectKt"
//    }
//
//    from(sourceSets.main.get().output)
//    dependsOn(configurations.runtimeClasspath)
//    from({
//        configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
//    })
//}