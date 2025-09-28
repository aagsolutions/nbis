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

import eu.aagsolutions.img.nbis.exceptions.NistException
import eu.aagsolutions.img.nbis.io.FIELD_MAX_LENGTH
import eu.aagsolutions.img.nbis.io.FILE_SEPARATOR
import eu.aagsolutions.img.nbis.io.GROUP_SEPARATOR
import eu.aagsolutions.img.nbis.io.TAG_SEPARATOR_COLON
import eu.aagsolutions.img.nbis.io.TAG_SEPARATOR_DOT
import eu.aagsolutions.img.nbis.io.TAG_SEPARATOR_GROUP_FIELD
import eu.aagsolutions.img.nbis.io.Tag
import eu.aagsolutions.img.nbis.io.Token
import eu.aagsolutions.img.nbis.model.enums.RecordType
import eu.aagsolutions.img.nbis.model.enums.records.ImageFields
import eu.aagsolutions.img.nbis.model.fields.Field
import eu.aagsolutions.img.nbis.model.fields.ImageField
import eu.aagsolutions.img.nbis.model.fields.TextField
import eu.aagsolutions.img.nbis.model.records.BaseRecord
import eu.aagsolutions.img.nbis.model.records.DefaultRecord
import eu.aagsolutions.img.nbis.model.records.FacialAndSMTImageRecord
import eu.aagsolutions.img.nbis.model.records.HighResolutionBinaryFingerprintRecord
import eu.aagsolutions.img.nbis.model.records.HighResolutionGrayscaleFingerprintRecord
import eu.aagsolutions.img.nbis.model.records.LatentImageRecord
import eu.aagsolutions.img.nbis.model.records.LowResolutionBinaryFingerprintRecord
import eu.aagsolutions.img.nbis.model.records.LowResolutionGrayscaleFingerprintRecord
import eu.aagsolutions.img.nbis.model.records.MinutiaeDataRecord
import eu.aagsolutions.img.nbis.model.records.PalmPrintRecord
import eu.aagsolutions.img.nbis.model.records.SignatureImageRecord
import eu.aagsolutions.img.nbis.model.records.TransactionInformationRecord
import eu.aagsolutions.img.nbis.model.records.UserDefinedImageRecord
import eu.aagsolutions.img.nbis.model.records.UserDefinedTextRecord
import eu.aagsolutions.img.nbis.model.records.VariableResolutionFingerprintRecord
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.OutputStream
import kotlin.Int

open class TextRecordHandler(
    recordType: RecordType,
) : RecordHandler(recordType) {
    override fun read(token: Token): BaseRecord {
        val start = token.position
        val fields = mutableMapOf<Int, Field<*>>()
        isPositionInIndex(token).let {
            val tag = getTagInfo(token)
            isTypeInFieldName(tag, recordType.id).let {
                val length = nextWord(token, TAG_SEPARATOR_GROUP_FIELD, FIELD_MAX_LENGTH).toInt()
                val textField = TextField(length.toString())
                fields[tag.field] = textField
                token.position++
                do {
                    val tag = getTagInfo(token)
                    if (extractField(start, length, token, tag, fields)) {
                        break
                    }
                } while (token.buffer[token.position++].toInt().toChar() != FILE_SEPARATOR)
            }
        }
        return buildRecord(recordType, fields.toMap())
    }

    override fun write(
        outputStream: OutputStream,
        record: BaseRecord,
    ) {
        var isFirst = true
        for (field in record.fields.entries.sortedBy { it.key }) {
            when (isFirst) {
                true -> isFirst = false
                false -> outputStream.write(GROUP_SEPARATOR.code)
            }

            when (field.value) {
                is TextField -> {
                    log.debug("Write record {} with text field with {} and value {}", recordType, field.key, field.value.getData())
                    val buffer = generateFieldTextToken(record.recordId, field.key, field.value as TextField)
                    outputStream.write(buffer)
                }

                is ImageField -> {
                    log.debug("Write record {} with image field with {}", recordType, field.key)
                    outputStream.write(generatePrefixFieldImageToken(record.recordId, field.key))
                    outputStream.write((field.value as ImageField).getData())
                }

                else -> throw NistException("Not implemented")
            }
        }
        outputStream.write(FILE_SEPARATOR.code)
    }

    private fun extractImageField(
        start: Int,
        length: Int,
        token: Token,
        tag: Tag,
        fields: MutableMap<Int, Field<*>>,
    ) {
        val data = token.buffer.copyOfRange(token.position, token.position + length - 1 - (token.position - start))
        token.position += data.size
        if (token.buffer[token.position].toInt().toChar() == FILE_SEPARATOR) {
            token.position++
        }
        val imageField = ImageField(data)
        fields[tag.field] = imageField
    }

    private fun extractField(
        start: Int,
        length: Int,
        token: Token,
        tag: Tag,
        fields: MutableMap<Int, Field<*>>,
    ): Boolean {
        var lastField = false
        isTypeInFieldName(tag, recordType.id).let {
            when (tag.field) {
                ImageFields.DATA.id -> {
                    extractImageField(start, length, token, tag, fields)
                    lastField = true
                }

                else -> {
                    val value = nextWord(token, TAG_SEPARATOR_GROUP_FIELD, FIELD_MAX_LENGTH - 1)
                    fields[tag.field] = TextField(value)
                    log.debug("Read field type {} with id {} and value {}", tag.type, tag.field, value)
                }
            }
        }
        return lastField
    }

    @Suppress("CyclomaticComplexMethod")
    private fun buildRecord(
        recordType: RecordType,
        fields: Map<Int, Field<*>>,
    ): BaseRecord =
        when (recordType.id) {
            RecordType.RT1.id -> TransactionInformationRecord(fields)
            RecordType.RT2.id -> UserDefinedTextRecord(fields)
            RecordType.RT3.id -> LowResolutionGrayscaleFingerprintRecord(fields)
            RecordType.RT4.id -> HighResolutionGrayscaleFingerprintRecord(fields)
            RecordType.RT5.id -> LowResolutionBinaryFingerprintRecord(fields)
            RecordType.RT6.id -> HighResolutionBinaryFingerprintRecord(fields)
            RecordType.RT7.id -> UserDefinedImageRecord(fields)
            RecordType.RT8.id -> SignatureImageRecord(fields)
            RecordType.RT9.id -> MinutiaeDataRecord(fields)
            RecordType.RT10.id -> FacialAndSMTImageRecord(fields)
            RecordType.RT13.id -> LatentImageRecord(fields)
            RecordType.RT14.id -> VariableResolutionFingerprintRecord(fields)
            RecordType.RT15.id -> PalmPrintRecord(fields)
            else -> DefaultRecord(recordType.id, fields)
        }

    private fun generateFieldTextToken(
        recordId: Int,
        key: Int,
        textField: TextField,
    ): ByteArray =
        buildString {
            append(recordId)
            append(TAG_SEPARATOR_DOT[0])
            append("%03d".format(key))
            append(TAG_SEPARATOR_COLON[0])
            append(textField.getData())
        }.toByteArray(Charsets.UTF_8)

    private fun generatePrefixFieldImageToken(
        recordId: Int,
        key: Int,
    ): ByteArray =
        buildString {
            append(recordId)
            append(TAG_SEPARATOR_DOT[0])
            append("%03d".format(key))
            append(TAG_SEPARATOR_COLON[0])
        }.toByteArray(Charsets.UTF_8)

    companion object {
        val log: Logger = LoggerFactory.getLogger(TextRecordHandler::class.java)
    }
}
