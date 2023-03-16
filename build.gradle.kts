allprojects {
    version = "0.1.2-SNAPSHOT"
    group = "ce"

    repositories {
        mavenCentral()
        google()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven { url = uri("https://jitpack.io") }
    }
}
