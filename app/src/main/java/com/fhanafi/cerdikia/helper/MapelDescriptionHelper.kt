package com.fhanafi.cerdikia.helper

object MapelDescriptionHelper {

    private val descriptionMap = mapOf(
        "matematika" to "Pelajari konsep matematika dari dasar hingga mahir.",
        "ipa" to "Eksplorasi ilmu pengetahuan alam dan fenomena sains.",
        "bahasa indonesia" to "Kuasai tata bahasa dan keterampilan berbahasa.",
        "ips" to "Pelajari sejarah, geografi, dan ekonomi secara menyeluruh.",
        "bahasa inggris" to "Tingkatkan kemampuan berbahasa Inggris dengan latihan interaktif.",
        "pendidikan pancasila" to "Pahami nilai-nilai luhur Pancasila sebagai dasar negara.",
        "seni budaya" to "Ekspresikan kreativitas melalui seni dan budaya lokal."
    )

    private const val defaultDescription =
        "Deskripsi mapel ini belum tersedia."

    fun getDescription(namaMapel: String): String {
        return descriptionMap[namaMapel.lowercase()] ?: defaultDescription
    }
}