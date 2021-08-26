# Configuration

Handle PureOrigins mod configuration files in JSON. Example usage
```kotlin
fun main() {
  val (setting1, setting2, setting3) = json.readJsonFileAs(configFile("settings.json"), Config())
  // reads config file in the config folder if exists, otherwise it is created
 
  // enjoy
}
 
@Serializable
data class Config(
  val setting1: Int = 0,
  val setting2: List<String> = listOf("abc")
  val setting3: String? = null
)
```
