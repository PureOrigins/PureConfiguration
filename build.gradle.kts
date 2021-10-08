plugins {
    id("fabric-loom")
    val kotlinVersion: String by System.getProperties()
    kotlin("jvm").version(kotlinVersion)
    `maven-publish`
    id("com.github.johnrengelman.shadow") version "7.0.0"
}
base {
    val archivesBaseName: String by project
    archivesName.set(archivesBaseName)
}
val modVersion: String by project
version = modVersion
val mavenGroup: String by project
group = mavenGroup
minecraft {}
repositories {}
val fat by configurations.creating {
    configurations.modApi.get().extendsFrom(this)
    exclude("org.jetbrains.kotlin")
    exclude("org.jetbrains.kotlinx")
}
dependencies {
    val minecraftVersion: String by project
    minecraft("com.mojang:minecraft:$minecraftVersion")
    val yarnMappings: String by project
    mappings("net.fabricmc:yarn:$yarnMappings:v2")
    val loaderVersion: String by project
    modImplementation("net.fabricmc:fabric-loader:$loaderVersion")
    val fabricVersion: String by project
    modImplementation("net.fabricmc.fabric-api:fabric-api:$fabricVersion")
    val fabricKotlinVersion: String by project
    modImplementation("net.fabricmc:fabric-language-kotlin:$fabricKotlinVersion")
    val luckpermsVersion: String by project
    compileOnly("net.luckperms:api:$luckpermsVersion")
    
    fat("org.freemarker:freemarker:2.3.31")
    fat("org.slf4j:slf4j-nop:1.7.30")
}
tasks {
    val javaVersion = JavaVersion.VERSION_16
    withType<JavaCompile> {
        options.encoding = "UTF-8"
        sourceCompatibility = javaVersion.toString()
        targetCompatibility = javaVersion.toString()
        options.release.set(javaVersion.toString().toInt())
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions { jvmTarget = javaVersion.toString() }
        sourceCompatibility = javaVersion.toString()
        targetCompatibility = javaVersion.toString()
    }
    jar { from("LICENSE") { rename { "${it}_${base.archivesName}" } } }
    shadowJar {
        archiveClassifier.set("fat")
        mergeServiceFiles()
        configurations = listOf(fat)
    }
    processResources {
        inputs.property("version", project.version)
        filesMatching("fabric.mod.json") { expand(mutableMapOf("version" to project.version)) }
    }
    java {
        toolchain { languageVersion.set(JavaLanguageVersion.of(javaVersion.toString())) }
        sourceCompatibility = javaVersion
        targetCompatibility = javaVersion
        withSourcesJar()
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.github.PureOrigins"
            artifactId = project.name
            version = modVersion
            
            artifact(tasks.named("jar", Jar::class).get().archiveFile) {
                builtBy(tasks["remapJar"])
            }
            
            artifact(tasks["sourcesJar"]) {
                builtBy(tasks["remapSourcesJar"])
            }
        }
    }
}