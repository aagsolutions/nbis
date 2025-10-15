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

package eu.aagsolutions.img.nbis.io

import eu.aagsolutions.img.nbis.datakit.Token
import eu.aagsolutions.img.nbis.io.record.HandlerFactory
import eu.aagsolutions.img.nbis.io.record.TransactionInformationRecordHandler
import eu.aagsolutions.img.nbis.model.NistFile
import eu.aagsolutions.img.nbis.model.enums.RecordType
import eu.aagsolutions.img.nbis.model.records.BaseRecord
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.InputStream
import kotlin.io.encoding.Base64

/**
 * A class designed to read and parse NIST files. The NIST file format is typically used for handling
 * biometric and forensic data, containing multiple records categorized by their respective `RecordType`.
 * This class processes the input file and organizes the parsed data into a structured `NistFile` object.
 */
class NistFileReader(
    val inputStream: InputStream,
) : AutoCloseable {
    /**
     * Reads a NIST file from the given input stream and parses its content into a structured `NistFile`.
     * The parsed records are classified by their `RecordType` and added to the resulting `NistFile`.
     *
     * @return A `NistFile` object containing all parsed records organized by their `RecordType`.
     */
    fun read(): NistFile {
        log.debug("Read a nist file")
        val content = inputStream.readAllBytes()
        return decode(content)
    }

    fun readToBase64(): String = byteArrayToBase64(inputStream.readAllBytes())

    override fun close() {
        inputStream.close()
    }

    companion object {
        val log: Logger = LoggerFactory.getLogger(NistFileReader::class.java)

        private const val NAX_SUPPORTED_RECORD_TYPE = 17
        private const val RECORD_SEPARATOR = "\u001E"
        private const val UNIT_SEPARATOR = "\u001F"

        private fun addRecord(
            recordsMap: MutableMap<RecordType, List<BaseRecord>>,
            recordType: RecordType,
            nistRecord: BaseRecord,
        ) {
            recordsMap[recordType] = recordsMap.getOrDefault(recordType, emptyList()) + nistRecord
        }

        private fun nextRecord(token: Token): Boolean {
            if (token.header.isNullOrEmpty()) {
                return false
            }

            val recordSeparatorPosition =
                token.header!!.indexOf(RECORD_SEPARATOR).let {
                    if (it == -1) token.header!!.length - 1 else it
                }

            val unitSeparatorPosition = token.header!!.indexOf(UNIT_SEPARATOR)
            token.crt = token.header!!.substring(0, unitSeparatorPosition).toInt()
            token.header = token.header!!.substring(recordSeparatorPosition + 1)

            return true
        }

        fun byteArrayToBase64(content: ByteArray): String = Base64.encode(content)

        fun decode(content: ByteArray): NistFile {
            val recordsMap = mutableMapOf<RecordType, List<BaseRecord>>()
            val token = Token(content)
            addRecord(recordsMap, RecordType.RT1, TransactionInformationRecordHandler().read(token))
            while (nextRecord(token)) {
                when (token.crt) {
                    0, 1 -> continue
                    in 2..NAX_SUPPORTED_RECORD_TYPE -> {
                        HandlerFactory.findByRecordId(token.crt).let { f ->
                            val handler = f.handler
                            addRecord(recordsMap, handler.recordType, handler.read(token))
                        }
                    }
                    else -> {
                        log.error("RecordType not implemented {}", token.crt)
                    }
                }
            }

            return NistFile(recordsMap.toMap())
        }
    }
}
