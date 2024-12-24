package com.github.yukkimoru.sdtm.utility

object Translate {

    val dictJP: Map<String, String> = mapOf(
        "DIAMOND" to "ダイヤモンド",
        "EMERALD" to "エメラルド"
    )

    fun transEN2JP(text: String): String {
        return dictJP[text] ?: text
    }
}