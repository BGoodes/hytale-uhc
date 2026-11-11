val hytaleVersion = "0.7.7"

subprojects {
    apply(plugin = "java")

    group = "fr.bgoodes.uhc"
    version = "0.1.0"

    repositories {
        mavenCentral()
        maven {
            name = "hytale"
            url = uri("https://maven.hytale.com/release")
        }
    }

    dependencies {
        add("compileOnly", "com.hypixel.hytale:Server:$hytaleVersion")

        if (name != "uhc-core") {
            add("compileOnly", project(":uhc-core"))
        }
    }

    tasks.named<ProcessResources>("processResources") {
        val props = mapOf(
            "version" to project.version.toString(),
            "hytaleVersion" to hytaleVersion,
        )

        inputs.properties(props)
        filteringCharset = "UTF-8"
        filesMatching("manifest.json") {
            expand(props)
        }
    }
}
