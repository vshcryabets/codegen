plugins {
    kotlin("jvm")
    `java-library`
    `maven-publish`
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(libs.junitJupiterApi)
    testRuntimeOnly(libs.junitJupiterEngine)
    implementation(libs.kotlinScriptingJsr223)
    implementation(libs.kotlinCompilerEmbedable)
    implementation(libs.groovyJsr223)
    implementation(project(":cgen-lib"))
    implementation("com.opencsv:opencsv:5.7.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")

    implementation("com.fasterxml.jackson.core:jackson-core:2.15.0")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.0")
    // https://mvnrepository.com/artifact/com.fasterxml.jackson.module/jackson-module-kotlin
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.5")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

kotlin { jvmToolchain(libs.versions.jvmLevel.get().toInt()) }
java { toolchain { languageVersion.set(JavaLanguageVersion.of(libs.versions.jvmLevel.get().toInt())) } }

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}