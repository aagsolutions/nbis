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
import eu.aagsolutions.img.nbis.io.AsciiHelper.FIELD_MAX_LENGTH
import eu.aagsolutions.img.nbis.io.AsciiHelper.FIELD_SEPARATOR
import eu.aagsolutions.img.nbis.io.AsciiHelper.TAG_SEPARATOR_GROUP_FIELD
import eu.aagsolutions.img.nbis.io.Token
import eu.aagsolutions.img.nbis.model.enums.RecordType
import eu.aagsolutions.img.nbis.model.enums.records.TransactionInformationFields
import eu.aagsolutions.img.nbis.model.fields.Field
import eu.aagsolutions.img.nbis.model.fields.TextField
import eu.aagsolutions.img.nbis.model.records.BaseRecord
import eu.aagsolutions.img.nbis.model.records.TransactionInformationRecord
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class TransactionInformationRecordHandler : TextRecordHandler(RecordType.RT1) {
    override fun read(token: Token): BaseRecord {
        val fields = mutableMapOf<Int, Field<*>>()
        isPositionInIndex(token).let {
            do {
                val tag = getTagInfo(token)
                if (tag.type != recordType.id) {
                    throw NistException("Invalid record type: ${tag.type}")
                }
                val value = nextWord(token, TAG_SEPARATOR_GROUP_FIELD, FIELD_MAX_LENGTH - 1)
                if (tag.field == TransactionInformationFields.CNT.id) {
                    token.header = value
                }
                log.debug("NIST - tag.type: {} ,tag.field: {}, value : {}", tag.type, tag.field, value)
                fields[tag.field] = TextField(value)
                if (tag.field == TransactionInformationFields.DCS.id) {
                    token.setCharSetDecoder(value)
                }
            } while (token.buffer[token.position++].toInt().toChar() != FIELD_SEPARATOR)
        }
        return TransactionInformationRecord(fields)
    }

    companion object {
        val log: Logger = LoggerFactory.getLogger(TransactionInformationRecordHandler::class.java)
    }
}
