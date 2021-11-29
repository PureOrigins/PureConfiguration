package it.pureorigins.framework.configuration

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.*
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

object MojangApi {
    suspend fun getPlayerInfo(username: String) = compactJson.httpGet<PlayerInfo>("https://api.mojang.com/users/profiles/minecraft/$username")
    suspend fun getPlayerInfo(uuid: UUID) = compactJson.httpGet<PlayerInfo>("https://sessionserver.mojang.com/session/minecraft/profile/$uuid")
    
    suspend fun getPlayerInfoOrNull(uuid: UUID) = try { getPlayerInfo(uuid) } catch (_: IOException) { null }
    suspend fun getPlayerInfoOrNull(username: String) = try { getPlayerInfo(username) } catch (_: IOException) { null }
    
    private suspend fun <T> Json.httpGet(url: String, deserializer: DeserializationStrategy<T>): T? = withContext(Dispatchers.IO) {
        val connection = (URL(url).openConnection() as HttpURLConnection).apply {
            requestMethod = "GET"
            connectTimeout = 5000
            readTimeout = 5000
        }
        val status = connection.responseCode
        if (status == 204) null else decodeFromString(deserializer, connection.inputStream.bufferedReader().readText())
    }
    
    private suspend inline fun <reified T> Json.httpGet(url: String): T? = httpGet(url, serializer())
}

@Serializable
data class PlayerInfo(
    val name: String,
    @Serializable(UUIDSerializer::class) @SerialName("id") val uuid: UUID
)

private object UUIDSerializer : KSerializer<UUID> {
    override val descriptor = PrimitiveSerialDescriptor("UUID", PrimitiveKind.STRING)
    override fun deserialize(decoder: Decoder) = fromStringWithoutDashes(decoder.decodeString())
    override fun serialize(encoder: Encoder, value: UUID) = encoder.encodeString(value.toString().replace("-", ""))
    
    private fun fromStringWithoutDashes(string: String): UUID {
        return UUID(java.lang.Long.parseUnsignedLong(string.substring(0, 16), 16), java.lang.Long.parseUnsignedLong(string.substring(16, 32), 16))
    }
}
