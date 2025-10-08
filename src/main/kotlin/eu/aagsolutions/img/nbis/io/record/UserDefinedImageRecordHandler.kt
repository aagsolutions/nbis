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

import eu.aagsolutions.img.nbis.datakit.Converters.fourBytesToLong
import eu.aagsolutions.img.nbis.datakit.Token
import eu.aagsolutions.img.nbis.model.enums.RecordType
import eu.aagsolutions.img.nbis.model.enums.records.DefaultFields
import eu.aagsolutions.img.nbis.model.enums.records.UserDefinedImageFields
import eu.aagsolutions.img.nbis.model.fields.Field
import eu.aagsolutions.img.nbis.model.fields.ImageField
import eu.aagsolutions.img.nbis.model.fields.TextField
import eu.aagsolutions.img.nbis.model.records.BaseRecord
import eu.aagsolutions.img.nbis.model.records.UserDefinedImageRecord
import java.io.OutputStream

class UserDefinedImageRecordHandler : ImageRecordHandler(RecordType.RT7) {
    @Suppress("MagicNumber")
    override fun read(token: Token): BaseRecord {
        val fields = mutableMapOf<Int, Field<*>>()
        isPositionInIndex(token).let {
            val length = fourBytesToLong(token.buffer, token.position)
            fields[DefaultFields.LEN.id] = TextField(length.toString())

            addUnsignedIntField(fields, UserDefinedImageFields.IDC, token, 4)
            addUnsignedIntField(fields, UserDefinedImageFields.IMT, token, 5)
            addUnsignedIntField(fields, UserDefinedImageFields.IMD, token, 6)
            addUnsignedIntField(fields, UserDefinedImageFields.PCN, token, 7)
            addUnsignedIntField(fields, UserDefinedImageFields.PCN2, token, 8)
            addUnsignedIntField(fields, UserDefinedImageFields.PCN3, token, 9)
            addUnsignedIntField(fields, UserDefinedImageFields.PCN4, token, 10)
            addUnsignedIntField(fields, UserDefinedImageFields.PCN5, token, 11)

            addUnsignedIntField(fields, UserDefinedImageFields.IMR, token, 17)
            addUnsignedIntField(fields, UserDefinedImageFields.IMR2, token, 18)
            addUnsignedIntField(fields, UserDefinedImageFields.IMR3, token, 19)
            addUnsignedIntField(fields, UserDefinedImageFields.IMR4, token, 20)
            addUnsignedIntField(fields, UserDefinedImageFields.IMR5, token, 21)
            addUnsignedIntField(fields, UserDefinedImageFields.IMR6, token, 22)
            addUnsignedIntField(fields, UserDefinedImageFields.IMR7, token, 23)
            addUnsignedIntField(fields, UserDefinedImageFields.IMR8, token, 24)
            addUnsignedIntField(fields, UserDefinedImageFields.IMR9, token, 25)
            addUnsignedIntField(fields, UserDefinedImageFields.IMR10, token, 26)
            addUnsignedIntField(fields, UserDefinedImageFields.IMR11, token, 27)

            addTwoBytesLongField(fields, UserDefinedImageFields.HLL, token, 28)
            addTwoBytesLongField(fields, UserDefinedImageFields.VLL, token, 30)

            addUnsignedIntField(fields, UserDefinedImageFields.GCA, token, 32)

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
                fields[UserDefinedImageFields.DATA.id] = ImageField(data)
            }
            token.position += length.toInt()
        }
        return UserDefinedImageRecord(fields.toMap())
    }

    override fun write(
        outputStream: OutputStream,
        record: BaseRecord,
    ) {
        writeFourBytesToken(outputStream, record, UserDefinedImageFields.LEN)
        writeOneByteToken(outputStream, record, UserDefinedImageFields.IDC)
        writeOneByteToken(outputStream, record, UserDefinedImageFields.IMT)
        writeOneByteToken(outputStream, record, UserDefinedImageFields.IMD)

        writeOneByteToken(outputStream, record, UserDefinedImageFields.PCN)
        writeOneByteToken(outputStream, record, UserDefinedImageFields.PCN2)
        writeOneByteToken(outputStream, record, UserDefinedImageFields.PCN3)
        writeOneByteToken(outputStream, record, UserDefinedImageFields.PCN4)
        writeOneByteToken(outputStream, record, UserDefinedImageFields.PCN5)

        outputStream.write(
            byteArrayOf(
                END_OF_TEXT_CONTROL_CHAR,
                END_OF_TEXT_CONTROL_CHAR,
                END_OF_TEXT_CONTROL_CHAR,
                END_OF_TEXT_CONTROL_CHAR,
                END_OF_TEXT_CONTROL_CHAR,
            ),
        )

        writeOneByteToken(outputStream, record, UserDefinedImageFields.IMR)
        writeOneByteToken(outputStream, record, UserDefinedImageFields.IMR2)
        writeOneByteToken(outputStream, record, UserDefinedImageFields.IMR3)
        writeOneByteToken(outputStream, record, UserDefinedImageFields.IMR4)
        writeOneByteToken(outputStream, record, UserDefinedImageFields.IMR5)
        writeOneByteToken(outputStream, record, UserDefinedImageFields.IMR6)
        writeOneByteToken(outputStream, record, UserDefinedImageFields.IMR7)
        writeOneByteToken(outputStream, record, UserDefinedImageFields.IMR8)
        writeOneByteToken(outputStream, record, UserDefinedImageFields.IMR9)
        writeOneByteToken(outputStream, record, UserDefinedImageFields.IMR10)
        writeOneByteToken(outputStream, record, UserDefinedImageFields.IMR11)

        writeTwoBytesToken(outputStream, record, UserDefinedImageFields.HLL)

        writeTwoBytesToken(outputStream, record, UserDefinedImageFields.VLL)

        writeOneByteToken(outputStream, record, UserDefinedImageFields.GCA)

        // Image
        writeImageToken(outputStream, record, UserDefinedImageFields.DATA)
    }

    companion object {
        const val FIXED_SIZE_OF_FIELDS: Int = 33
        const val END_OF_TEXT_CONTROL_CHAR = 0x03.toByte()
    }
}
