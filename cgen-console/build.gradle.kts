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
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")

    // script engines
    implementation("org.jetbrains.kotlin:kotlin-scripting-jsr223:${Versions.kotlin}")
    implementation("org.jetbrains.kotlin:kotlin-compiler-embeddable:${Versions.kotlin}")
    implementation("org.codehaus.groovy:groovy-jsr223:3.0.17")
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

