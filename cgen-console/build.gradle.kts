plugins {
    kotlin("jvm") version "1.7.20"
    id("org.jetbrains.compose") version "1.2.1"
    application
}

repositories {
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

dependencies {
    implementation(project(":cgen-lib"))
//    implementation("ce:cgen-lib:0.1.2-SNAPSHOT")
//    implementation("com.github.vshcryabets:cgen-lib:dc65e025a7")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")

    implementation(compose.desktop.currentOs)
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}