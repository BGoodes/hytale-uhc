plugins {
    id("java")
}

group = "fr.bgoodes.uhc"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()

    maven {
        name = "hytale"
        url = uri("https://maven.hytale.com/release")
    }
}

dependencies {
    compileOnly("org.jetbrains:annotations:24.1.0")
    implementation("com.hypixel.hytale:Server:0.7.0")
}

tasks.processResources.configure {
    val props = mapOf(Pair("version", "$version"))

    inputs.properties(props)
    filteringCharset = "UTF-8"
    filesMatching("manifest.json") {
        expand(props)
    }
}