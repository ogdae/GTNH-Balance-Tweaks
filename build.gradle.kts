plugins {
    id("com.gtnewhorizons.gtnhconvention")
}

dependencies {
    // GregTech API (dev jar)
    compileOnly("com.github.GTNewHorizons:GT5-Unofficial:5.09.45.74:dev")
    compileOnly("com.github.GTNewHorizons:GTplusplus:1.9.85:dev")
}

// === Versioning ===

// Read the Java file and extract VERSION constant
val modVersion: String = file("src/main/java/com/rilliko/gtnhbalancetweaks/GTNHBalanceTweaks.java")
    .readText()
    .let { text ->
        val regex = Regex("""VERSION\s*=\s*"([^"]+)"""")
        val match = regex.find(text)
        match?.groups?.get(1)?.value
    } ?: "0.0.0-UNKNOWN"

// Default version from Java
var fullVersion = modVersion

// Add git metadata unless this is a release build
if (System.getenv("RELEASE_BUILD").isNullOrEmpty()) {
    try {
        val (_, gitBranch) = "git rev-parse --abbrev-ref HEAD".runCommand()
        val (_, gitHash) = "git rev-parse --short HEAD".runCommand()
        val (dirtyExit, _) = "git diff --quiet".runCommand(exitOnFail = false)
        val isDirty = dirtyExit != 0

        fullVersion += "-$gitBranch+$gitHash" + if (isDirty) "-dirty" else ""
    } catch (e: Exception) {
        println("Warning: could not get git metadata: ${e.message}")
    }
}

version = fullVersion
group = "com.rilliko"

base {
    archivesName.set("GTNHBalanceTweaks")
}

// === Helper to run shell commands ===
fun String.runCommand(exitOnFail: Boolean = true): Pair<Int, String> {
    val parts = this.split(" ")
    val process = ProcessBuilder(*parts.toTypedArray())
        .redirectErrorStream(true)
        .start()
    val output = process.inputStream.bufferedReader().readText().trim()
    val exitCode = process.waitFor()
    if (exitOnFail && exitCode != 0) {
        throw RuntimeException("Command failed: $this (exit=$exitCode)")
    }
    return exitCode to output
}

