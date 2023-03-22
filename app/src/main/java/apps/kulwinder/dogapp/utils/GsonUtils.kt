package apps.kulwinder.dogapp.utils

import com.google.gson.Gson

fun <T> Gson.toJson(any: T): String {
    return toJson(any)
}

inline fun <reified T> Gson.fromJson(any: String): T {
    return fromJson(any, T::class.java)
}