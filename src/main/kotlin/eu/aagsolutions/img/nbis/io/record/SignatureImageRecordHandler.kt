/*
 * Copyright (c) 2025 Aurel Avramescu.
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the “Software”), to deal
 * in the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do
 * so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package eu.aagsolutions.img.nbis.io.record

import eu.aagsolutions.img.nbis.converters.Converters.byteToString
import eu.aagsolutions.img.nbis.converters.Converters.fourBytesToLong
import eu.aagsolutions.img.nbis.converters.Converters.twoBytesToLong
import eu.aagsolutions.img.nbis.datakit.Token
import eu.aagsolutions.img.nbis.model.enums.RecordType
import eu.aagsolutions.img.nbis.model.enums.records.SignatureImageFields
import eu.aagsolutions.img.nbis.model.fields.Field
import eu.aagsolutions.img.nbis.model.fields.ImageField
import eu.aagsolutions.img.nbis.model.fields.TextField
import eu.aagsolutions.img.nbis.model.records.BaseRecord
import eu.aagsolutions.img.nbis.model.records.SignatureImageRecord
import java.io.OutputStream

class SignatureImageRecordHandler : ImageRecordHandler(RecordType.RT8) {
    @Suppress("MagicNumber")
    override fun read(token: Token): BaseRecord {
        val fields = mutableMapOf<Int, Field<*>>()
        isPositionInIndex(token).let {
            val length = fourBytesToLong(token.buffer, token.position)
            fields[SignatureImageFields.LEN.id] = TextField(length.toString())
            if (checkRecordSizeLength(token, 4)) {
                val idc = token.buffer[token.position + 4].toInt()
                fields[SignatureImageFields.IDC.id] = TextField(idc.toString())
            }
            if (checkRecordSizeLength(token, 5)) {
                val signatureType = token.buffer[token.position + 5]
                fields[SignatureImageFields.SIG.id] = TextField(signatureType.toString())
            }
            if (checkRecordSizeLength(token, 6)) {
                val srt = token.buffer[token.position + 6]
                fields[SignatureImageFields.SRT.id] = TextField(byteToString(srt))
            }
            if (checkRecordSizeLength(token, 7)) {
                val isr = token.buffer[token.position + 7]
                fields[SignatureImageFields.ISR.id] = TextField(byteToString(isr))
            }
            if (checkRecordSizeLength(token, 8)) {
                val hll = twoBytesToLong(token.buffer, token.position + 8)
                fields[SignatureImageFields.HLL.id] = TextField(hll.toString())
            }
            if (checkRecordSizeLength(token, 10)) {
                val vll = twoBytesToLong(token.buffer, token.position + 10)
                fields[SignatureImageFields.VLL.id] = TextField(vll.toString())
            }
            var dataSize = length - FIXED_SIZE_OF_FIELDS
            if (token.position + dataSize + FIXED_SIZE_OF_FIELDS - 1 > token.buffer.size) {
                dataSize += token.buffer.size - token.position - FIXED_SIZE_OF_FIELDS
            }
            if (dataSize > 0) {
                val data =
                    token.buffer.copyOfRange(
                        token.position + FIXED_SIZE_OF_FIELDS,
                        token.position + FIXED_SIZE_OF_FIELDS + dataSize.toInt(),
                    )
                fields[SignatureImageFields.DATA.id] = ImageField(data)
            }
            token.position += length.toInt()
        }
        return SignatureImageRecord(fields.toMap())
    }

    override fun write(
        outputStream: OutputStream,
        record: BaseRecord,
    ) {
        writeFourBytesToken(outputStream, record, SignatureImageFields.LEN)

        writeOneByteToken(outputStream, record, SignatureImageFields.IDC)

        writeOneByteToken(outputStream, record, SignatureImageFields.SIG)

        writeOneByteToken(outputStream, record, SignatureImageFields.SRT)

        writeOneByteToken(outputStream, record, SignatureImageFields.ISR)

        writeTwoBytesToken(outputStream, record, SignatureImageFields.HLL)

        writeTwoBytesToken(outputStream, record, SignatureImageFields.VLL)

        // Image
        writeImageToken(outputStream, record, SignatureImageFields.DATA)
    }

    companion object {
        const val FIXED_SIZE_OF_FIELDS: Int = 12
    }
}
