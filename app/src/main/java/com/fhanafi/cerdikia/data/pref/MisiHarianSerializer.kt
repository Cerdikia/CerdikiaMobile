package com.fhanafi.cerdikia.data.pref

import androidx.datastore.core.Serializer
import java.io.InputStream
import java.io.OutputStream

object MisiHarianDataSerializer : Serializer<MisiHarianData> {
    override val defaultValue: MisiHarianData = MisiHarianData.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): MisiHarianData =
        MisiHarianData.parseFrom(input)

    override suspend fun writeTo(t: MisiHarianData, output: OutputStream) =
        t.writeTo(output)
}
