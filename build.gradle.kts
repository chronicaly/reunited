plugins {
    id("fabric-loom") version "1.17.0-alpha.14"
}

version = property("mod_version") as String
group = property("maven_group") as String

base {
    archivesName.set(property("archives_base_name") as String)
}

repositories {
    maven("https://maven.fabricmc.net/")
    maven("https://libraries.minecraft.net/")
    mavenCentral()
}

dependencies {
    minecraft("com.mojang:minecraft:${property("minecraft_version")}")
    mappings("net.fabricmc:yarn:${property("yarn_mappings")}:v2")
    modImplementation("net.fabricmc:fabric-loader:${property("loader_version")}")
    modImplementation("net.fabricmc.fabric-api:fabric-api:${property("fabric_version")}")
}

val resourceProps = mapOf(
    "version" to project.version,
    "minecraft_version" to project.property("minecraft_version"),
    "loader_version" to project.property("loader_version")
)

tasks.processResources {
    val props = resourceProps
    inputs.properties(props)
    filesMatching("fabric.mod.json") {
        expand(props)
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    options.release.set(21)
}

val prismModsDir = file("C:/Users/xchro/AppData/Roaming/PrismLauncher/instances/Unity/minecraft/mods")

val copyBuiltJarToPrism by tasks.registering(Copy::class) {
    group = "build"
    description = "Copies the remapped Unity Client jar into the Unity PrismLauncher instance mods folder."
    dependsOn(tasks.named("remapJar"))
    from(tasks.named("remapJar"))
    into(prismModsDir)
}

tasks.named("build") {
    finalizedBy(copyBuiltJarToPrism)
}
