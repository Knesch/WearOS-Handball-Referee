// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    id("com.android.library") version "9.4.1" apply false
    alias(libs.plugins.kotlin.compose) apply false
}

tasks.register("bumpVersionCode") {
    group = "versioning"
    description = "Increments the shared VERSION_CODE by 1 in version.properties"
    doLast {
        bumpVersionCodeOnly()
    }
}

tasks.register("bumpPatchVersion") {
    group = "versioning"
    description = "Increments VERSION_NAME patch (e.g. 1.6.0 -> 1.6.1) and VERSION_CODE by 1"
    doLast {
        bumpVersion(bumpType = "patch")
    }
}

tasks.register("bumpMinorVersion") {
    group = "versioning"
    description = "Increments VERSION_NAME minor (e.g. 1.6.0 -> 1.7.0) and VERSION_CODE by 1"
    doLast {
        bumpVersion(bumpType = "minor")
    }
}

tasks.register("bumpMajorVersion") {
    group = "versioning"
    description = "Increments VERSION_NAME major (e.g. 1.6.0 -> 2.0.0) and VERSION_CODE by 1"
    doLast {
        bumpVersion(bumpType = "major")
    }
}

fun bumpVersionCodeOnly() {
    val versionFile = file("version.properties")
    val props = java.util.Properties()
    if (versionFile.exists()) {
        versionFile.inputStream().use { props.load(it) }
    }

    val currentCode = (props.getProperty("VERSION_CODE") ?: "13").toInt()
    val currentName = props.getProperty("VERSION_NAME") ?: "1.6.0"
    val newCode = currentCode + 1

    props.setProperty("VERSION_CODE", newCode.toString())
    versionFile.outputStream().use { props.store(it, "Auto-generated version properties") }
    println("VersionCode bumped: $currentCode -> $newCode (VersionName: $currentName)")
}

fun bumpVersion(bumpType: String = "patch") {
    val versionFile = file("version.properties")
    val props = java.util.Properties()
    if (versionFile.exists()) {
        versionFile.inputStream().use { props.load(it) }
    }

    val currentCode = (props.getProperty("VERSION_CODE") ?: "13").toInt()
    val currentName = props.getProperty("VERSION_NAME") ?: "1.6.0"

    val parts = currentName.split(".").asSequence().map { it.toIntOrNull() ?: 0 }.toMutableList()
    while (parts.size < 3) {
        parts.add(0)
    }

    var major = parts[0]
    var minor = parts[1]
    var patch = parts[2]

    when (bumpType) {
        "major" -> {
            major++
            minor = 0
            patch = 0
        }
        "minor" -> {
            minor++
            patch = 0
        }
        else -> {
            patch++
        }
    }

    val newCode = currentCode + 1
    val newName = "$major.$minor.$patch"

    props.setProperty("VERSION_CODE", newCode.toString())
    props.setProperty("VERSION_NAME", newName)

    versionFile.outputStream().use { props.store(it, "Auto-generated version properties") }
    println("Version bumped -> Name: $currentName -> $newName | Code: $currentCode -> $newCode")
}
