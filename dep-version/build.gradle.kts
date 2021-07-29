buildscript {
    repositories {
        mavenCentral()
        google()
    }
    dependencies {
        classpath(kotlin("gradle-plugin", version = "1.5.10"))
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
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.10")
}

java.sourceCompatibility = JavaVersion.VERSION_1_8

gradlePlugin {
    plugins {
        create("VersionPlugin") {
            id = "version-plugin"
            implementationClass = "com.easy.version.DependencyVersionPlugin"
        }
    }
}