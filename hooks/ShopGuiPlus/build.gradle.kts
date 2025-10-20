dependencies {
    compileOnly("com.github.brcdev-minecraft:shopgui-api:3.2.0") {
        exclude(group = "org.spigotmc", module = "spigot-api")
    }
}