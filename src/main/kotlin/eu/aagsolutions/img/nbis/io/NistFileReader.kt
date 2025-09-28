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

import eu.aagsolutions.img.nbis.io.record.FacialSMTImageRecordHandler
import eu.aagsolutions.img.nbis.io.record.HighResolutionBinaryFingerprintRecordHandler
import eu.aagsolutions.img.nbis.io.record.HighResolutionGrayscaleFingerprintRecordHandler
import eu.aagsolutions.img.nbis.io.record.LatentImageDataRecordHandler
import eu.aagsolutions.img.nbis.io.record.LowResolutionBinaryFingerprintRecordHandler
import eu.aagsolutions.img.nbis.io.record.LowResolutionGrayscaleFingerprintRecordHandler
import eu.aagsolutions.img.nbis.io.record.MinutiaeDataRecordHandler
import eu.aagsolutions.img.nbis.io.record.PalmRecordHandler
import eu.aagsolutions.img.nbis.io.record.SignatureImageRecordHandler
import eu.aagsolutions.img.nbis.io.record.TextRecordHandler
import eu.aagsolutions.img.nbis.io.record.TransactionInformationRecordHandler
import eu.aagsolutions.img.nbis.io.record.UserDefinedDescriptionTextRecordHandler
import eu.aagsolutions.img.nbis.io.record.UserDefinedImageRecordHandler
import eu.aagsolutions.img.nbis.io.record.VariableResolutionFingerprintRecordHandler
import eu.aagsolutions.img.nbis.model.NistFile
import eu.aagsolutions.img.nbis.model.enums.RecordType
import eu.aagsolutions.img.nbis.model.records.BaseRecord
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.InputStream
import java.util.Base64

/**
 * A class designed to read and parse NIST files. The NIST file format is typically used for handling
 * biometric and forensic data, containing multiple records categorized by their respective `RecordType`.
 * This class processes the input file and organizes the parsed data into a structured `NistFile` object.
 */
class NistFileReader(
    val inputStream: InputStream,
) : AutoCloseable {
    private val recordsMap = mutableMapOf<RecordType, List<BaseRecord>>()

    private fun addRecord(
        recordType: RecordType,
        nistRecord: BaseRecord,
    ) {
        recordsMap[recordType] = recordsMap.getOrDefault(recordType, emptyList()) + nistRecord
    }

    /**
     * Reads a NIST file from the given input stream and parses its content into a structured `NistFile`.
     * The parsed records are classified by their `RecordType` and added to the resulting `NistFile`.
     *
     * @return A `NistFile` object containing all parsed records organized by their `RecordType`.
     */
    @Suppress("CyclomaticComplexMethod")
    fun read(): NistFile {
        log.debug("Read a nist file")

        val content = inputStream.readNBytes(inputStream.available())
        val token = Token(content)
        addRecord(RecordType.RT1, TransactionInformationRecordHandler().read(token))
        while (nextRecord(token)) {
            when (token.crt) {
                0, 1 -> continue
                RecordType.RT2.id -> addRecord(RecordType.findByRecordId(token.crt), UserDefinedDescriptionTextRecordHandler().read(token))
                RecordType.RT3.id ->
                    addRecord(
                        RecordType.findByRecordId(token.crt),
                        LowResolutionGrayscaleFingerprintRecordHandler().read(token),
                    )
                RecordType.RT4.id ->
                    addRecord(
                        RecordType.findByRecordId(token.crt),
                        HighResolutionGrayscaleFingerprintRecordHandler().read(token),
                    )
                RecordType.RT5.id ->
                    addRecord(
                        RecordType.findByRecordId(token.crt),
                        LowResolutionBinaryFingerprintRecordHandler().read(token),
                    )
                RecordType.RT6.id ->
                    addRecord(
                        RecordType.findByRecordId(token.crt),
                        HighResolutionBinaryFingerprintRecordHandler().read(token),
                    )
                RecordType.RT7.id -> addRecord(RecordType.findByRecordId(token.crt), UserDefinedImageRecordHandler().read(token))
                RecordType.RT8.id -> addRecord(RecordType.findByRecordId(token.crt), SignatureImageRecordHandler().read(token))
                RecordType.RT9.id -> addRecord(RecordType.findByRecordId(token.crt), MinutiaeDataRecordHandler().read(token))
                RecordType.RT10.id -> addRecord(RecordType.findByRecordId(token.crt), FacialSMTImageRecordHandler().read(token))
                RecordType.RT11.id -> addRecord(RecordType.findByRecordId(token.crt), TextRecordHandler(RecordType.RT11).read(token))
                RecordType.RT12.id -> addRecord(RecordType.findByRecordId(token.crt), TextRecordHandler(RecordType.RT12).read(token))
                RecordType.RT13.id -> addRecord(RecordType.findByRecordId(token.crt), LatentImageDataRecordHandler().read(token))
                RecordType.RT14.id ->
                    addRecord(
                        RecordType.findByRecordId(token.crt),
                        VariableResolutionFingerprintRecordHandler().read(token),
                    )
                RecordType.RT15.id -> addRecord(RecordType.findByRecordId(token.crt), PalmRecordHandler().read(token))
                RecordType.RT16.id -> addRecord(RecordType.findByRecordId(token.crt), TextRecordHandler(RecordType.RT16).read(token))
                RecordType.RT17.id -> addRecord(RecordType.findByRecordId(token.crt), TextRecordHandler(RecordType.RT17).read(token))
                else -> {
                    log.error("RecordType not implemented {}", token.crt)
                    addRecord(RecordType.findByRecordId(token.crt), TextRecordHandler(RecordType.findByRecordId(token.crt)).read(token))
                }
            }
        }

        return NistFile(recordsMap.toMap())
    }

    fun readToBase64(): String = Base64.getEncoder().encodeToString(inputStream.readAllBytes())

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

    override fun close() {
        inputStream?.close()
    }

    companion object {
        val log: Logger = LoggerFactory.getLogger(NistFileReader::class.java)
    }
}
