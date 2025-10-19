rootProject.extra.properties["sha"]?.let { sha ->
    version = sha
}

tasks {
    shadowJar {
        destinationDirectory.set(rootProject.extra["apiFolder"] as File)
    }

    build {
        dependsOn(shadowJar)
    }
}