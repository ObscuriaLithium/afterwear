plugins {
	id("mod-platform")
	id("net.neoforged.moddev")
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
	loader = "neoforge"
	dependencies {
		required("minecraft") {
			forgeLikeVersionRange = prop("deps.minecraft")
		}
		required("neoforge") {
			forgeLikeVersionRange.set("[${prop("deps.neoforge")},)")
		}
		required("fragmentum") {
			forgeLikeVersionRange.set("[${prop("deps.fragmentum")},)")
			slug("fragmentum", "fragmentum-neoforge")
		}
	}
}

neoForge {
	version = prop("deps.neoforge")
	accessTransformers.from(transformerSource("cfg"))
	validateAccessTransformers = true

	if (hasProperty("deps.parchment")) parchment {
		val (mc, ver) = prop("deps.parchment").split(':')
		mappingsVersion = ver
		minecraftVersion = mc
	}

	runs {
		register("client") {
			client()
			gameDirectory = file("run/")
			ideName = "NeoForge Client (${stonecutter.current.version})"
			programArgument("--username=Dev")
		}
		register("server") {
			server()
			gameDirectory = file("run/")
			ideName = "NeoForge Server (${stonecutter.current.version})"
		}
	}

	mods {
		register(prop("mod.id")) {
			sourceSet(sourceSets["main"])
		}
	}
	sourceSets["main"].resources.srcDir("${rootDir}/versions/datagen/${sc.current.version.split("-")[0]}/src/main/generated")
}

repositories {
	mavenLocal()
	mavenCentral()
	strictMaven("https://api.modrinth.com/maven", "maven.modrinth") { name = "Modrinth" }
	maven("https://thedarkcolour.github.io/KotlinForForge/") { name = "Kotlin for Forge" }
	maven("https://maven.isxander.dev/releases") { name = "Yet Another Config Lib" }
}

dependencies {
	implementation("dev.obscuria:fragmentum:${prop("deps.fragmentum")}+${prop("deps.minecraft")}-neoforge")
	implementation("thedarkcolour:kotlinforforge-neoforge:${prop("deps.kotlinforforge")}")
	implementation("dev.isxander:yet-another-config-lib:${prop("deps.yacl")}")
}

tasks.named("createMinecraftArtifacts") {
	dependsOn(tasks.named("stonecutterGenerate"))
}
