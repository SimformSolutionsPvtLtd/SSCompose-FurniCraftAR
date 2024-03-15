
import java.net.URI
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.RepositoryHandler

/**
 * Extension to add file tree dependency
 */
fun Project.defaultFileTree() = fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar")))

/**
 * Provides root path of the project and append path.
 */
fun Project.rootPath(appendPath: String = ""): String = rootProject.rootDir.path + appendPath

