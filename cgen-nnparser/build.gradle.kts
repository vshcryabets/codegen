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