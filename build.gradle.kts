// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
        maven {
            url = uri("https://plugins.gradle.org/m2/")
        }
    }
    dependencies {
        val kotlinVersion = "1.4.32"
        classpath("com.android.tools.build:gradle:4.1.3")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.10")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.3.5")

        classpath("com.squareup.sqldelight:gradle-plugin:1.4.4")

        classpath("org.jmailen.gradle:kotlinter-gradle:3.2.0")
    }
}

allprojects {
    repositories {
        google()
        maven { url = uri("https://jitpack.io") }
        maven {
            url = uri("https://plugins.gradle.org/m2/")
        }
    }
}

tasks.register("clean", Delete::class.java, Action<Delete> {
    delete(rootProject.buildDir)
})