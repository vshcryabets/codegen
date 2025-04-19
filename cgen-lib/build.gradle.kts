import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    kotlin("jvm")
    `java-library`
    `maven-publish`
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
    implementation(gradleApi())
    implementation("com.fasterxml.jackson.core:jackson-core:2.15.0-rc1")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.13.4.2")
    // https://mvnrepository.com/artifact/com.fasterxml.jackson.module/jackson-module-kotlin
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.5")


    // https://mvnrepository.com/artifact/org.jfree/org.jfree.svg
    implementation("org.jfree:org.jfree.svg:5.0.3")

    // https://mvnrepository.com/artifact/org.abego.treelayout/org.abego.treelayout.core
    implementation("org.abego.treelayout:org.abego.treelayout.core:1.0.3")
}

kotlin {
    jvmToolchain(Versions.jvmLevel)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(Versions.jvmLevel))
    }
}

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
