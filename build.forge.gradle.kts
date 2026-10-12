plugins {
	id("mod-platform")
	id("net.neoforged.moddev.legacyforge")
}

stonecutter {
	val (version, loader) = current.project.split('-', limit = 2)
	properties.tags(version, loader)

	replacements.string(current.parsed >= "1.21.11") {
		replace("ResourceLocation", "Identifier")
		replace("location()", "identifier()")
	}
}

platform {
	loader = "forge"
	dependencies {
		required("minecraft") {
			forgeLikeVersionRange = prop("deps.minecraft")
		}
		required("forge") {
			forgeLikeVersionRange.set("[${prop("deps.forge")},)")
		}
		required("fragmentum") {
			forgeLikeVersionRange.set("[${prop("deps.fragmentum")},)")
			slug("fragmentum")
		}
	}
}

legacyForge {
	version = "${prop("deps.minecraft")}-${prop("deps.forge")}"

	validateAccessTransformers = true

	accessTransformers.from(transformerSource("cfg"))

	runs {
		register("client") {
			client()
			gameDirectory = file("run/")
			ideName = "Forge Client (${sc.current.version})"
			programArgument("--username=Dev")
		}
		register("server") {
			server()
			gameDirectory = file("run/")
			ideName = "Forge Server (${sc.current.version})"
		}
	}


	mods {
		register(prop("mod.id")) {
			sourceSet(sourceSets["main"])
		}
	}
}

mixin {
	add(sourceSets.main.get(), "${prop("mod.id")}.mixins.refmap.json")
	config("${prop("mod.id")}.mixins.json")
}

repositories {
	mavenLocal()
	mavenCentral()
	strictMaven("https://api.modrinth.com/maven", "maven.modrinth") { name = "Modrinth" }
	maven("https://thedarkcolour.github.io/KotlinForForge/") { name = "Kotlin for Forge" }
	maven("https://maven.isxander.dev/releases") { name = "Yet Another Config Lib" }
	maven("https://redempt.dev") { name = "Crunch" }
}

dependencies {

	fun includeLibrary(dependency: String) {
		add("additionalRuntimeClasspath", dependency)
		implementation(dependency)
		jarJar(dependency)
	}

	annotationProcessor("org.spongepowered:mixin:${libs.versions.mixin.get()}:processor")
	annotationProcessor("io.github.llamalad7:mixinextras-common:0.5.0")
	compileOnly("io.github.llamalad7:mixinextras-common:0.5.0")
	implementation("io.github.llamalad7:mixinextras-forge:0.5.0")

	modImplementation("dev.obscuria:fragmentum:${prop("deps.fragmentum")}+${prop("deps.minecraft")}-forge")
	modImplementation("thedarkcolour:kotlinforforge:${prop("deps.kotlinforforge")}")
	modImplementation("dev.isxander:yet-another-config-lib:${prop("deps.yacl")}")

	includeLibrary("com.github.Redempt:Crunch:2.0.3")
}

sourceSets {
	main {
		resources.srcDir(
			"${rootDir}/versions/datagen/${sc.current.version.split("-")[0]}/src/main/generated"
		)
	}
}

tasks.named("createMinecraftArtifacts") {
	dependsOn(tasks.named("stonecutterGenerate"))
}
