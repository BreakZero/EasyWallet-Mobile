buildscript {
    repositories {
        mavenCentral()
        google()
    }
    dependencies {
        classpath(kotlin("gradle-plugin", version = "1.5.20"))
    }
}
plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    google()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.20")
}

java.sourceCompatibility = JavaVersion.VERSION_11

gradlePlugin {
    plugins {
        create("VersionPlugin") {
            id = "version-plugin"
            implementationClass = "com.easy.version.DependencyVersionPlugin"
        }
    }
}