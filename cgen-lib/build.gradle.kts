import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    kotlin("jvm")
    `java-library`
    `maven-publish`
    id("com.github.johnrengelman.shadow") version libs.versions.shadowJar
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(libs.junitJupiterApi)
    testRuntimeOnly(libs.junitJupiterEngine)
    testImplementation("org.mockito:mockito-core:5.0.0")

    implementation(gradleApi())
    implementation(libs.jacksonCore)
    implementation(libs.jacksonDataBind)
    implementation(libs.jacksonModuleKotlin)

    // https://mvnrepository.com/artifact/org.jfree/org.jfree.svg
    implementation("org.jfree:org.jfree.svg:5.0.3")

    // https://mvnrepository.com/artifact/org.abego.treelayout/org.abego.treelayout.core
    implementation("org.abego.treelayout:org.abego.treelayout.core:1.0.3")
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

tasks {
    named<ShadowJar>("shadowJar") {
        archiveBaseName.set("shadow")
        mergeServiceFiles()
        manifest {
//            attributes(mapOf("Main-Class" to "com.github.csolem.gradle.shadow.kotlin.example.App"))
        }
    }
    test {
        useJUnitPlatform()
        testLogging.showStandardStreams = true
        testLogging {
            events("passed", "skipped", "failed", "started")
        }
    }
}
