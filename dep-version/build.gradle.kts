buildscript {
    repositories {
        jcenter()
        google()
    }
    dependencies {
        classpath(kotlin("gradle-plugin", version = "1.4.32"))
    }
}
plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
}

repositories {
    jcenter()
    google()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.32")
}

java.sourceCompatibility = JavaVersion.VERSION_1_8

gradlePlugin {
    plugins {
        create("VersionPlugin") {
            id = "version-plugin"
            implementationClass = "com.dougie.version.DependencyVersionPlugin"
        }
    }
}