plugins {
    kotlin("jvm")
    `java-library`
    `maven-publish`
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")

    implementation("org.jetbrains.kotlin:kotlin-scripting-jsr223:1.8.0")
    implementation("org.jetbrains.kotlin:kotlin-compiler-embeddable:1.8.0")
    implementation(project(":cgen-lib"))

    implementation("com.opencsv:opencsv:5.7.1")

    // https://mvnrepository.com/artifact/org.codehaus.groovy/groovy-jsr223
    implementation("org.codehaus.groovy:groovy-jsr223:3.0.17")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")

}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(Versions.jvmLevel)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(Versions.jvmLevel))
    }
}
