package com.nfa.konflagration.io

import com.nfa.konflagration.Konflagration
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

/**
 * Created by nathan on 10/23/15.
 */
public object FileLocations {

    public val RootDirectory = findRootDirectory()

    public val DataFolder = RootDirectory.resolve("data")
    public val AirfoilsFolder = DataFolder.resolve("airfoils")

    private fun findRootDirectory(): Path {
        try {
            val jar = Paths.get(Konflagration::class.java.protectionDomain.codeSource.location.toURI())
            return jar.parent
        } catch (ex: Exception) {
            println("Exception: " + ex.javaClass.canonicalName)
            return Paths.get(".")
        }
    }

    init {
        val fields = FileLocations::class.java.declaredFields
        for (field in fields) {
            if (Path::class.java.isAssignableFrom(field.type) && java.lang.reflect.Modifier.isPublic(field.modifiers)) {
                try {
                    val file = field.get(FileLocations) as Path
                    val parent = file.parent
                    if (!Files.isDirectory(parent)) {
                        Files.createDirectories(parent)
                    }
                } catch (ex: IllegalArgumentException) {
                } catch (ex: IllegalAccessException) {
                } catch (ex: IOException) {
                } finally {
                }
            }
        }
    }

}