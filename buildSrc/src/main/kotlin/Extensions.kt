import org.gradle.api.Project
import java.util.Properties

/**
 * Extension to add file tree dependency
 */
fun Project.defaultFileTree() = fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar")))

/**
 * Provides root path of the project and append path.
 */
fun Project.rootPath(appendPath: String = ""): String = rootProject.rootDir.path + appendPath

/**
 * Get local property form the given [fileName].
 *
 * File should must be present in root project directory
 */
fun Project.getProperty(key: String, fileName: String): String {
    val properties = Properties()
    properties.load(project.rootProject.file(fileName).reader())
    return properties.getProperty(key)
}
