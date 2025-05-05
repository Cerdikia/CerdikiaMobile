package com.fhanafi.cerdikia.helper

fun stripHtmlTags(input: String): String {
    return input.replace(Regex("<.*?>"), "").trim()
}