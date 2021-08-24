package com.easy.wallet.data.constant

enum class MediaTypeSuffix(suffix: String) {
  JPG(".jpg"), PNG(".png"), GIF(".gif"),
  SVG(".svg"), MP4(".mp4"), WEBM(".webm"),
  MP3(".mp3"), WAV(".wav"), OGG(".ogg"),
  GLB(".glb"), GLTF(".gltf")
}

internal val VIDEO_TYPE = listOf(MediaTypeSuffix.MP4, MediaTypeSuffix.WEBM)
internal val AUDIO_TYPE = listOf(MediaTypeSuffix.MP3, MediaTypeSuffix.WAV, MediaTypeSuffix.OGG)
