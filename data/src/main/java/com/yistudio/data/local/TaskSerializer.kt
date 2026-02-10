package com.yistudio.data.local
import androidx.datastore.core.Serializer
import com.yistudio.data.entity.TaskPreferences
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

object TaskSerializer : Serializer<TaskPreferences> {
    override val defaultValue: TaskPreferences = TaskPreferences()

    override suspend fun readFrom(input: InputStream): TaskPreferences {
        return try {
            Json.decodeFromString(
                deserializer = TaskPreferences.serializer(),
                string = input.readBytes().decodeToString()
            )
        } catch (e: SerializationException) {
            defaultValue
        }
    }

    override suspend fun writeTo(t: TaskPreferences, output: OutputStream) {
        output.write(Json.encodeToString(TaskPreferences.serializer(), t).toByteArray())
    }
}