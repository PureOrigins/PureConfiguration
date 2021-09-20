# PureConfiguration

Handle PureOrigins mod configuration files in JSON. Example usage
```kotlin
fun main() { 
    val (setting1, setting2, setting3, setting4) = json.readJsonFileAs(configFile("settings.json"), Config()) 
    // reads config file in the config folder if exists, otherwise it is created
    
    val message = setting4.template("users" to listOf(User("AgeOfWar", 5), User("ekardnamm", 3)))
    println(message)
    // Users:
    // AgeOfWar - 5
    // ekardnamm - 3
}
 
@Serializable
data class Config(
    val setting1: Int = 0, 
    val setting2: List<String> = listOf("abc", "def"), 
    val setting3: String? = null, 
    val setting4: String = "Users:\n<#list users as user>\${user.name} - \${user.points}<#sep>\n</#list>"
)

data class User(
    val name: String,
    val points: String
)
```

## Gradle
```kotlin
plugins {
    id("fabric-loom")
    val kotlinVersion: String by System.getProperties()
    kotlin("jvm").version(kotlinVersion)
    kotlin("plugin.serialization").version(kotlinVersion)
}

repositories {
    maven(url = "https://jitpack.io")
}

dependencies {
    // [...]
    val configurationVersion: String by project // latest version: 1.0.1
    modImplementation("com.github.PureOrigins:PureConfiguration:$configurationVersion")
}
```