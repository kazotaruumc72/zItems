plugins {
    id("java-library")
    id("com.gradleup.shadow") version "9.0.0-beta11"
    id("re.alwyn974.groupez.repository") version "1.0.0"
}

apply("gradle/copy-build.gradle")

extra.set("targetFolder", file("target/"))
extra.set("apiFolder", file("target-api/"))
extra.set("classifier", System.getProperty("archive.classifier"))
extra.set("sha", System.getProperty("github.sha"))

allprojects {
    apply(plugin = "java-library")
    apply(plugin = "com.gradleup.shadow")
    apply(plugin = "re.alwyn974.groupez.repository")

    group = "fr.traqueur.items"
    version = "1.0.0"

    repositories {
        mavenCentral()

        maven {
            name = "papermc"
            url = uri("https://repo.papermc.io/repository/maven-public/")
        }
        maven(url = "https://repo.extendedclip.com/content/repositories/placeholderapi/")
        maven(url = "https://jitpack.io")
    }

    java {
        withSourcesJar()
        withJavadocJar()
    }

    tasks.shadowJar {
        archiveBaseName.set("zItems")
        archiveAppendix.set(if (project.path == ":") "" else project.name)
        archiveClassifier.set("")

        relocate("fr.traqueur.structura", "fr.traqueur.items.libs.structura")
        relocate("fr.traqueur.commands", "fr.traqueur.items.libs.commands")
    }

    tasks.compileJava {
        options.encoding = "UTF-8"
        options.release = 21
    }

    tasks.javadoc {
        options.encoding = "UTF-8"
        if (JavaVersion.current().isJava9Compatible)
            (options as StandardJavadocDocletOptions).addBooleanOption("html5", true)
    }

    dependencies {
        compileOnly("io.papermc.paper:paper-api:1.21.5-R0.1-SNAPSHOT")

        /* Depends */
        compileOnly("me.clip:placeholderapi:2.11.6")
        compileOnly("fr.maxlego08.menu:zmenu-api:1.1.0.4")
        compileOnly(files(rootProject.files("libs/zMenu-1.1.0.4.jar")))

        /* Adventure for Spigot compatibility */
        compileOnly("net.kyori:adventure-platform-bukkit:4.3.4")

        /* Libraries */
        implementation("com.github.Traqueur-dev:Structura:1.4.0")
        implementation("com.github.Traqueur-dev.CommandsAPI:platform-spigot:4.2.3")
        compileOnly("org.reflections:reflections:0.10.2")

    }

}

dependencies {
    api(project(":api"))

    rootProject.subprojects.filter { it.path.startsWith(":hooks:") }.forEach { subproject ->
        implementation(project(subproject.path))
    }
}

tasks {
    shadowJar {
        rootProject.extra.properties["sha"]?.let { sha ->
            archiveClassifier.set("${rootProject.extra.properties["classifier"]}-${sha}")
        } ?: run {
            archiveClassifier.set(rootProject.extra.properties["classifier"] as String?)
        }
        destinationDirectory.set(rootProject.extra["targetFolder"] as File)
    }

    build {
        dependsOn(shadowJar)
    }

    processResources {
        from("resources")
        filesMatching("plugin.yml") {
            expand("version" to project.version)
        }
    }
}