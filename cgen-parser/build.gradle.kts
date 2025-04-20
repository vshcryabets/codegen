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

    implementation(project(":cgen-lib"))
    implementation("javax.inject:javax.inject:1")
    implementation(libs.jacksonCore)
    implementation(libs.jacksonDataBind)
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