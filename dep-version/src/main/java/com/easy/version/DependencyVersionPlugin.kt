package com.easy.version

import org.gradle.api.Plugin
import org.gradle.api.Project
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader
import java.util.*

class DependencyVersionPlugin : Plugin<Project> {
  override fun apply(target: Project) {

  }
}

fun keyStoreProperties(): Properties {
  val properties = Properties()
  val keyProperties = File("./keystore", "configs.properties")

  if (keyProperties.isFile) {
    InputStreamReader(FileInputStream(keyProperties), Charsets.UTF_8).use { reader ->
      properties.load(reader)
    }
  }
  return properties
}