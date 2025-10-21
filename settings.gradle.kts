enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        maven {
            name = "groupezReleases"
            url = uri("https://repo.groupez.dev/releases")
        }
        gradlePluginPortal()
    }
}

rootProject.name = "zItems"
include("api")

file("hooks").listFiles()?.forEach { file ->
    if (file.isDirectory and !file.name.equals("build")) {
        println("Include hooks:${file.name}")
        include(":hooks:${file.name}")
    }
}
include("hooks:WorldGuard")
include("hooks:SuperiorSkyBlock2")