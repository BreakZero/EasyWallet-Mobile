import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
        maven(url = uri("https://jitpack.io"))
        maven(url = uri("https://plugins.gradle.org/m2/"))
    }
    dependencies {
        val kotlinVersion = "1.5.20"
        classpath("com.android.tools.build:gradle:7.0.1")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.3.5")

        classpath("com.squareup.sqldelight:gradle-plugin:1.4.4")
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven(url = uri("https://jitpack.io"))

        maven(url = uri("https://maven.pkg.github.com/trustwallet/wallet-core")) {
            credentials {
                username = gradleLocalProperties(rootDir).getProperty("gpr.user").also {
                    println("${this@repositories} user ====== $it")
                }
                password = gradleProperties(rootDir, com.android.SdkConstants.FN_LOCAL_PROPERTIES).getProperty("gpr.key").also {
                    println("${this@repositories} key ====== $it")
                }
            }
        }
    }
}

fun gradleProperties(projectRootDir : File, fileName: String) : java.util.Properties {
    val properties = java.util.Properties()
    val localProperties = File(projectRootDir, fileName)

    if (localProperties.isFile) {
        java.io.InputStreamReader(
            java.io.FileInputStream(localProperties),
            com.google.common.base.Charsets.UTF_8
        ).use { reader ->
            properties.load(reader)
        }
    }
    return properties
}

tasks.register("clean", Delete::class.java) {
    delete(rootProject.buildDir)
}