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

import eu.aagsolutions.img.nbis.converters.byteToString
import eu.aagsolutions.img.nbis.converters.fourBytesToLong
import eu.aagsolutions.img.nbis.converters.listToString
import eu.aagsolutions.img.nbis.converters.stringToList
import eu.aagsolutions.img.nbis.converters.twoBytesToLong
import eu.aagsolutions.img.nbis.io.Token
import eu.aagsolutions.img.nbis.model.enums.RecordType
import eu.aagsolutions.img.nbis.model.enums.records.FieldType
import eu.aagsolutions.img.nbis.model.enums.records.ImageFields
import eu.aagsolutions.img.nbis.model.fields.Field
import eu.aagsolutions.img.nbis.model.fields.ImageField
import eu.aagsolutions.img.nbis.model.fields.TextField
import eu.aagsolutions.img.nbis.model.records.BaseRecord
import eu.aagsolutions.img.nbis.model.records.DefaultRecord
import eu.aagsolutions.img.nbis.model.records.HighResolutionBinaryFingerprintRecord
import eu.aagsolutions.img.nbis.model.records.HighResolutionGrayscaleFingerprintRecord
import eu.aagsolutions.img.nbis.model.records.LowResolutionBinaryFingerprintRecord
import eu.aagsolutions.img.nbis.model.records.LowResolutionGrayscaleFingerprintRecord
import eu.aagsolutions.img.nbis.model.records.SignatureImageRecord
import eu.aagsolutions.img.nbis.model.records.UserDefinedImageRecord
import java.io.OutputStream
import java.util.concurrent.atomic.AtomicInteger

/**
 * Abstract class responsible for handling image-related records in binary data format.
 *
 * This class provides functionality to read and write various types of image records
 * (e.g., fingerprint records, user-defined image records, signature image records)
 * encoded in binary format.
 *
 * @constructor Initializes the handler with a specific type of record.
 * @param recordType The type of record that the handler will process.
 */
abstract class ImageRecordHandler(
    recordType: RecordType,
) : BinaryRecordHandler(recordType) {
    @Suppress("MagicNumber")
    override fun read(token: Token): BaseRecord {
        val fields = mutableMapOf<Int, Field<*>>()
        isPositionInIndex(token).let {
            // X.001 : LEN
            val length = fourBytesToLong(token.buffer, token.position)
            val textField = TextField(length.toString())
            fields[ImageFields.LEN.id] = textField

            addIntField(fields, ImageFields.IDC, token, 4)

            addIntField(fields, ImageFields.IMP, token, 5)

            val fingerPrintPositions: MutableList<String> = ArrayList()
            for (i in 6 until 12) {
                if (checkRecordSizeLength(token, i)) {
                    fingerPrintPositions.add(byteToString(token.buffer[token.position + i]))
                }
            }

            val fingerPrintPositionsTextField = TextField(listToString(fingerPrintPositions))
            fields[ImageFields.FGP.id] = fingerPrintPositionsTextField

            addUnsignedIntField(fields, ImageFields.ISR, token, 12)

            addTwoBytesLongField(fields, ImageFields.HLL, token, 13)

            addTwoBytesLongField(fields, ImageFields.VLL, token, 15)

            addUnsignedIntField(fields, ImageFields.GCA, token, 17)

            // X.999 : DATA
            var dataSize = length - 18
            if (token.position + dataSize + 17 > token.buffer.size) {
                dataSize += token.buffer.size - token.position - 18
            }

            if (dataSize > 0) {
                val data =
                    token.buffer.copyOfRange(
                        token.position + 18,
                        token.position + 18 + dataSize.toInt(),
                    )
                fields[ImageFields.DATA.id] = ImageField(data)
            }
            token.position += length.toInt()
        }
        return when (recordType.id) {
            RecordType.RT3.id -> LowResolutionGrayscaleFingerprintRecord(fields.toMap())
            RecordType.RT4.id -> HighResolutionGrayscaleFingerprintRecord(fields.toMap())
            RecordType.RT5.id -> LowResolutionBinaryFingerprintRecord(fields.toMap())
            RecordType.RT6.id -> HighResolutionBinaryFingerprintRecord(fields.toMap())
            RecordType.RT7.id -> UserDefinedImageRecord(fields.toMap())
            RecordType.RT8.id -> SignatureImageRecord(fields.toMap())
            else -> DefaultRecord(recordType.id, fields.toMap())
        }
    }

    override fun write(
        outputStream: OutputStream,
        record: BaseRecord,
    ) {
        writeFourBytesToken(outputStream, record, ImageFields.LEN)
        writeOneByteToken(outputStream, record, ImageFields.IDC)
        writeOneByteToken(outputStream, record, ImageFields.IMP)

        val fingerPrintsBytes = ByteArray(FINGERPRINT_SIZE) { EMPTY_INT.toByte() }
        record.getFieldText(ImageFields.FGP)?.let { fingerPrintField ->
            val atomicInteger = AtomicInteger(0)
            stringToList(fingerPrintField).forEach { n ->
                fingerPrintsBytes[atomicInteger.getAndIncrement()] = n!!.toInt().toByte()
            }
        }

        outputStream.write(fingerPrintsBytes)

        writeOneByteToken(outputStream, record, ImageFields.ISR)

        writeTwoBytesToken(outputStream, record, ImageFields.HLL)

        writeTwoBytesToken(outputStream, record, ImageFields.VLL)

        writeOneByteToken(outputStream, record, ImageFields.GCA)

        writeImageToken(outputStream, record, ImageFields.DATA)
    }

    private fun addIntField(
        fields: MutableMap<Int, Field<*>>,
        field: ImageFields,
        token: Token,
        offset: Int,
    ) {
        if (checkRecordSizeLength(token, offset)) {
            val intValue = token.buffer[token.position + offset].toInt()
            fields[field.id] = TextField(intValue.toString())
        }
    }

    protected fun addTwoBytesLongField(
        fields: MutableMap<Int, Field<*>>,
        field: FieldType,
        token: Token,
        offset: Int,
    ) {
        if (checkRecordSizeLength(token, offset)) {
            val longValue = twoBytesToLong(token.buffer, token.position + offset)
            fields[field.id] = TextField(longValue.toString())
        }
    }

    protected fun addUnsignedIntField(
        fields: MutableMap<Int, Field<*>>,
        field: FieldType,
        token: Token,
        offset: Int,
    ) {
        if (checkRecordSizeLength(token, offset)) {
            val byteValue = token.buffer[token.position + offset]
            fields[field.id] = TextField(byteToString(byteValue))
        }
    }

    companion object {
        const val FINGERPRINT_SIZE = 6
    }
}
