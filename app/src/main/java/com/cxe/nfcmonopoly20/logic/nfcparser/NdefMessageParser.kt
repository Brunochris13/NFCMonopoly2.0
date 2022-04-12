package com.cxe.nfcmonopoly20.logic.nfcparser

import android.annotation.SuppressLint
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import androidx.core.util.Preconditions
import java.io.UnsupportedEncodingException
import java.util.*
import kotlin.experimental.and

class NdefMessageParser {

    @SuppressLint("RestrictedApi")
    fun parse(message: NdefMessage): List<String> {
        val records = message.records
        val stringMessages = mutableListOf<String>()

        for (record in records) {
            Preconditions.checkArgument(record.tnf == NdefRecord.TNF_WELL_KNOWN)
            Preconditions.checkArgument(Arrays.equals(record.type, NdefRecord.RTD_TEXT))

            try {
                val payload = record.payload
                /*
                     * payload[0] contains the "Status Byte Encodings" field, per the
                     * NFC Forum "Text Record Type Definition" section 3.2.1.
                     *
                     * bit7 is the Text Encoding Field.
                     *
                     * if (Bit_7 == 0): The text is encoded in UTF-8 if (Bit_7 == 1):
                     * The text is encoded in UTF16
                     *
                     * Bit_6 is reserved for future use and must be set to zero.
                     *
                     * Bits 5 to 0 are the length of the IANA language code.
                     */
                val textEncoding = if (payload[0] and 128.toByte() == 0.toByte()) "UTF-8" else "UTF-16"
                val languageCodeLength: Byte = payload[0] and 63.toByte()
//                val languageCode = String(payload, 1, languageCodeLength.toInt(), "US-ASCII")
                val text = String(
                    payload, languageCodeLength + 1,
                    payload.size - languageCodeLength - 1, charset(textEncoding)
                )
//                TextRecord(languageCode, text)

                stringMessages.add(text)
            } catch (e: UnsupportedEncodingException) {
                // should never happen unless we get a malformed tag.
                throw IllegalArgumentException(e)
            }
        }
        return stringMessages
    }
}