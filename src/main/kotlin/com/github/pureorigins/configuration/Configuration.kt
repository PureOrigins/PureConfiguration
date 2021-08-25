package com.github.pureorigins.configuration

import kotlinx.serialization.*
import kotlinx.serialization.json.Json
import net.fabricmc.loader.api.FabricLoader
import java.io.FileNotFoundException
import java.lang.Thread.currentThread
import java.nio.file.Path
import kotlin.io.path.*
import kotlin.reflect.typeOf

val json = Json {
    ignoreUnknownKeys = true
}

val prettyPrintJson = Json(json) {
    prettyPrint = true
}

val configDir: Path get() = FabricLoader.getInstance().configDir
val gameDir: Path get() = FabricLoader.getInstance().gameDir

fun configFile(name: String): Path = configDir.resolve(name)
fun gameFile(name: String): Path = gameDir.resolve(name)

inline fun <reified T> Json.readFileAs(file: Path, deserializer: DeserializationStrategy<T> = serializersModule.serializer(), crossinline exceptionHandler: (Throwable) -> T): T {
    val text = try {
        file.readText()
    } catch (e: Throwable) {
        return exceptionHandler(e)
    }
    return try {
        decodeFromString(deserializer, text)
    } catch (e: Throwable) {
        @OptIn(ExperimentalStdlibApi::class)
        exceptionHandler(SerializationException("An error occurred while deserializing $text to ${typeOf<T>()}", e))
    }
}

inline fun <reified T> Json.readFileAs(
    file: Path,
    default: T,
    serializer: KSerializer<T> = serializersModule.serializer(),
    crossinline exceptionHandler: (Throwable) -> T
): T {
    return if (!file.exists()) {
        try {
            val text = encodeToString(serializer, default)
            file.parent?.createDirectories()
            file.writeText(text)
        } catch (e: Throwable) {
            return exceptionHandler(SerializationException("An error occurred while serializing $default", e))
        }
        default
    } else {
        readFileAs(file, serializer, exceptionHandler)
    }
}

inline fun <reified T> Json.readFileOrCopy(
    file: Path,
    defaultPath: String,
    deserializer: DeserializationStrategy<T> = serializersModule.serializer(),
    classLoader: ClassLoader = currentThread().contextClassLoader,
    crossinline exceptionHandler: (Throwable) -> T
): T {
    if (!file.exists()) {
        try {
            file.parent?.createDirectories()
            classLoader.getResourceAsStream("config/$defaultPath")?.use {
                it.copyTo(file.outputStream())
            } ?: throw FileNotFoundException("cannot find resource on 'config/$defaultPath'")
        } catch (e: Throwable) {
            return exceptionHandler(e)
        }
    }
    return readFileAs(file, deserializer, exceptionHandler)
}

inline fun <reified T> Json.writeFile(
    file: Path,
    content: T,
    serializer: SerializationStrategy<T> = serializersModule.serializer(),
    crossinline exceptionHandler: (Throwable) -> Unit
) {
    val text = try {
        encodeToString(serializer, content)
    } catch (e: Throwable) {
        return exceptionHandler(SerializationException("An error occurred while serializing $content", e))
    }
    try {
        file.parent?.createDirectories()
        file.writeText(text)
    } catch (e: Throwable) {
        exceptionHandler(e)
    }
}