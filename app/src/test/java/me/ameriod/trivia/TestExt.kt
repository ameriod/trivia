package me.ameriod.trivia

import java.io.InputStream
import java.nio.charset.Charset

/**
 * Takes a file in the test/resources folder and returns the String of it.
 *
 * @param clzz     class calling this method, needs to be the same package as the resource file
 * @param fileName the name of the resource file
 * @return the String of the file's contents
 */
fun Any.resourceToString(clzz: Class<*>? = null, fileName: String): String =
        try {
            val inputStream = (clzz ?: this::class.java).classLoader!!.getResourceAsStream(fileName)
            inputStream.readTextAndClose()
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }

/**
 * Converts an [InputStream] to a String
 */
fun InputStream.readTextAndClose(charset: Charset = Charsets.UTF_8): String =
        this.bufferedReader(charset).use { it.readText() }


