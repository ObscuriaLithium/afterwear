import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.file.DuplicatesStrategy
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.named
import org.gradle.kotlin.dsl.the
import org.gradle.language.jvm.tasks.ProcessResources
import java.io.File

private const val PREDICATE_FILE = "predicate.txt"

fun Project.applyVersionedResources(
	ctx: Context,
	dirName: String = "versioned"
) {
	val matchedDirs: List<File> = the<JavaPluginExtension>().sourceSets.named("main").get()
		.resources.srcDirs
		.map { it.resolve(dirName) }
		.filter { it.isDirectory }
		.flatMap { root -> root.listFiles().orEmpty().filter { it.isDirectory }.sortedBy { it.name } }
		.filter { dir -> dir.matches(ctx) }

	tasks.named<ProcessResources>("processResources") {
		exclude(dirName, "$dirName/**")
		duplicatesStrategy = DuplicatesStrategy.INCLUDE
		matchedDirs.forEach { dir -> from(dir) { exclude(PREDICATE_FILE) } }
	}
}

private fun File.readPredicate(): String {
	val file = resolve(PREDICATE_FILE)
	if (!file.isFile) {
		throw GradleException("Versioned resources: directory '$name' ($path) has no '$PREDICATE_FILE'")
	}
	val expression = file.readLines()
		.map { it.removePrefix("\uFEFF").substringBefore('#').trim() }
		.filter { it.isNotEmpty() }
		.joinToString(" ")
	if (expression.isEmpty()) {
		throw GradleException("Versioned resources: '$PREDICATE_FILE' in '$name' ($path) is empty")
	}
	return expression
}

private fun File.matches(ctx: Context): Boolean {
	val expression = readPredicate()
	return try {
		expression.split("||").any { orPart ->
			orPart.split("&&").all { term ->
				val predicate = term.trim()
				require(predicate.isNotEmpty()) { "empty term" }
				ctx.stonecutter.eval(ctx.currentMcVersion, predicate)
			}
		}
	} catch (e: Exception) {
		throw GradleException(
			"Versioned resources: invalid predicate '$expression' in '$name/$PREDICATE_FILE' (${e.message})", e
		)
	}
}
