plugins {
	id("mod-platform")
	id("dev.kikugie.loom-back-compat")
}

stonecutter {
	val (version, loader) = current.project.split('-', limit = 2)
	properties.tags(version, loader)

	replacements.string(current.parsed >= "1.21.11") {
		replace("ResourceLocation", "Identifier")
		replace("location()", "identifier()")
	}
	replacements.string(current.parsed >= "26.1") {
		replace("FabricDataOutput", "FabricPackOutput")
	}
}

platform {
	loader = "fabric"
	dependencies {
		required("minecraft") {
			fabricLikeVersionRange = prop("deps.minecraft")
		}
		required("fabricloader") {
			fabricLikeVersionRange = ">=${prop("deps.fabric-loader")}"
		}
		required("fragmentum") {
			fabricLikeVersionRange = ">=${prop("deps.fragmentum")}"
			slug("fragmentum", "fragmentum-fabric")
		}
	}
}

loom {
	accessWidenerPath = transformerSource("accesswidener")
	runs.named("client") {
		client()
		ideConfigGenerated(true)
		runDir = "run/"
		environment = "client"
		programArgs("--username=Dev")
		configName = "Fabric Client"
	}
	runs.named("server") {
		server()
		ideConfigGenerated(true)
		runDir = "run/"
		environment = "server"
		configName = "Fabric Server"
	}
}

fabricApi {
	configureDataGeneration {
		outputDirectory = file("${rootDir}/versions/datagen/${sc.current.version.split("-")[0]}/src/main/generated")
		client = true
	}
}

repositories {
	mavenLocal()
	mavenCentral()
	strictMaven("https://maven.terraformersmc.com/", "com.terraformersmc") { name = "TerraformersMC" }
	strictMaven("https://api.modrinth.com/maven", "maven.modrinth") { name = "Modrinth" }
	maven("https://raw.githubusercontent.com/Fuzss/modresources/main/maven/") { name = "Forge Config API Port" }
	maven("https://maven.isxander.dev/releases") { name = "Yet Another Config Lib" }
	maven("https://cursemaven.com") { name = "CurseMaven" }
	maven("https://redempt.dev") { name = "Crunch" }
}

configurations.all {
	resolutionStrategy {
		force("net.fabricmc:fabric-loader:${prop("deps.fabric-loader")}")
	}
}

dependencies {

	fun includeLibrary(dependency: String) {
		implementation(dependency)
		include(dependency)
	}

	minecraft("com.mojang:minecraft:${prop("deps.minecraft")}")
	if (sc.current.parsed < "26") {
		mappings(loom.layered {
			officialMojangMappings()
			if (hasProperty("deps.parchment"))
				parchment("org.parchmentmc.data:parchment-${prop("deps.parchment")}@zip")
		})
	}
	modImplementation("net.fabricmc:fabric-loader:${prop("deps.fabric-loader")}")
	modImplementation("net.fabricmc.fabric-api:fabric-api:${prop("deps.fabric-api")}")
	modImplementation("dev.obscuria:fragmentum:${prop("deps.fragmentum")}+${prop("deps.minecraft")}-fabric")
	modImplementation("com.terraformersmc:modmenu:${prop("deps.modmenu")}")

	modImplementation("net.fabricmc:fabric-language-kotlin:${prop("deps.fabric-language-kotlin")}")
	modImplementation("fuzs.forgeconfigapiport:forgeconfigapiport-fabric:${prop("deps.forge-config-api-port")}")
	modImplementation("dev.isxander:yet-another-config-lib:${prop("deps.yacl")}")

	includeLibrary("com.github.Redempt:Crunch:2.0.3")

	if (sc.current.parsed.eq("1.21.1")) {
		modLocalRuntime("curse.maven:worldedit-225608:5830452")
	}
}
