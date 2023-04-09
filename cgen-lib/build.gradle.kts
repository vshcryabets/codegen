plugins {
    kotlin("jvm") version "1.7.20"
    `java-library`
    `maven-publish`
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
//    implementation(gradleApi())
    implementation("com.fasterxml.jackson.core:jackson-core:2.13.4")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.13.4.2")

    implementation(kotlin("scripting-jsr223"))

    // https://mvnrepository.com/artifact/org.jfree/org.jfree.svg
    implementation("org.jfree:org.jfree.svg:5.0.3")

    // https://mvnrepository.com/artifact/org.abego.treelayout/org.abego.treelayout.core
    implementation("org.abego.treelayout:org.abego.treelayout.core:1.0.3")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/vshcryabets/codegen")
            credentials {
                username = project.findProperty("gpr.user") as String? ?: System.getenv("USERNAME")
                password = project.findProperty("gpr.key") as String? ?: System.getenv("TOKEN")
            }
        }
    }
    publications {
        register<MavenPublication>("gpr") {
            from(components["java"])
        }
    }
}