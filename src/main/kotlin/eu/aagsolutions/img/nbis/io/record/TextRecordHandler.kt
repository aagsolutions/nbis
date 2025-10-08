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
import eu.aagsolutions.img.nbis.datakit.AsciiHelper.FIELD_MAX_LENGTH
import eu.aagsolutions.img.nbis.datakit.AsciiHelper.FIELD_SEPARATOR
import eu.aagsolutions.img.nbis.datakit.AsciiHelper.GROUP_SEPARATOR
import eu.aagsolutions.img.nbis.datakit.AsciiHelper.TAG_SEPARATOR_COLON
import eu.aagsolutions.img.nbis.datakit.AsciiHelper.TAG_SEPARATOR_DOT
import eu.aagsolutions.img.nbis.datakit.AsciiHelper.TAG_SEPARATOR_GROUP_FIELD
import eu.aagsolutions.img.nbis.datakit.Tag
import eu.aagsolutions.img.nbis.datakit.Token
import eu.aagsolutions.img.nbis.model.builders.BuilderFactory
import eu.aagsolutions.img.nbis.model.enums.RecordType
import eu.aagsolutions.img.nbis.model.enums.records.ImageFields
import eu.aagsolutions.img.nbis.model.fields.Field
import eu.aagsolutions.img.nbis.model.fields.ImageField
import eu.aagsolutions.img.nbis.model.fields.TextField
import eu.aagsolutions.img.nbis.model.records.BaseRecord
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.OutputStream
import kotlin.Int

/**
 * This class provides functionality to handle text-based records within a specific record type.
 * It extends the `RecordHandler` base class and specializes in reading and writing operations
 * for text and image fields associated with a record.
 *
 * @constructor
 * @param recordType The type of record being processed by this handler.
 */
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
                } while (token.buffer[token.position++].toInt().toChar() != FIELD_SEPARATOR)
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
        outputStream.write(FIELD_SEPARATOR.code)
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
        if (token.buffer[token.position].toInt().toChar() == FIELD_SEPARATOR) {
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

    private fun buildRecord(
        recordType: RecordType,
        fields: Map<Int, Field<*>>,
    ) = BuilderFactory
        .findByRecordId(recordType.id)
        .builder
        .withFields(fields)
        .build()

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
