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

import eu.aagsolutions.img.nbis.converters.stringToListOfNistEntries
import eu.aagsolutions.img.nbis.exceptions.NistException
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
import eu.aagsolutions.img.nbis.model.enums.records.TransactionInformationFields
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.BufferedOutputStream
import java.io.OutputStream

/**
 * The `NistFileWriter` class provides functionality for writing `NistFile` objects to an output stream.
 *
 * This class is responsible for handling the serialization of `NistFile` contents into their corresponding
 * binary format as defined by the NIST standards. The writing process ensures compliance with record type
 * handling and ordering as described in the transaction information record. It supports multiple record
 * types and delegates their individual serialization to appropriate record handlers tailored for each type.
 */
class NistFileWriter(
    val outputStream: OutputStream,
) : AutoCloseable {
    /**
     * Writes the content of the provided `NistFile` to the specified `OutputStream`.
     *
     * The method processes records within the `NistFile` according to the record type and writes
     * the corresponding data using the appropriate handlers. It ensures the handling of all supported
     * record types and maintains the order defined in the transaction information record, field 3 (1.003).
     *
     * Exceptions during the writing process are logged, and any critical issue will result in a
     * `NistException` being thrown.
     *
     * @param file the `NistFile` containing records to be written.
     * @param outputStream the `OutputStream` where the `NistFile` content will be written.
     * @return the provided `OutputStream` after writing the data.
     * @throws NistException if an error occurs during the writing process.
     */
    @Suppress("CyclomaticComplexMethod")
    fun write(file: NistFile) {
        log.debug("Writing a nistFile to provided outputStream")

        BufferedOutputStream(outputStream).use { bufferedOS ->
            TransactionInformationRecordHandler().write(bufferedOS, file.getTransactionInformationRecord())
            val writingOrder =
                file
                    .getTransactionInformationRecord()
                    .getFieldText(TransactionInformationFields.CNT)
                    ?.let { stringToListOfNistEntries(it) }
                    ?: throw NistException("Missing CNT field on RT1 code '1.003'")

            for (field in writingOrder) {
                val recordId = field.key.toInt()
                val idcId = field.value.toInt()
                val recordType = RecordType.findByRecordId(recordId)

                if (recordType != RecordType.RT1) {
                    val nistRecord =
                        file.getRecordByTypeAndIdc(recordType, idcId)
                            ?: throw NistException("Missing record $recordId with idc $idcId")

                    when (recordType) {
                        RecordType.RT2 -> UserDefinedDescriptionTextRecordHandler().write(bufferedOS, nistRecord)
                        RecordType.RT3 -> LowResolutionGrayscaleFingerprintRecordHandler().write(bufferedOS, nistRecord)
                        RecordType.RT4 -> HighResolutionGrayscaleFingerprintRecordHandler().write(bufferedOS, nistRecord)
                        RecordType.RT5 -> LowResolutionBinaryFingerprintRecordHandler().write(bufferedOS, nistRecord)
                        RecordType.RT6 -> HighResolutionBinaryFingerprintRecordHandler().write(bufferedOS, nistRecord)
                        RecordType.RT7 -> UserDefinedImageRecordHandler().write(bufferedOS, nistRecord)
                        RecordType.RT8 -> SignatureImageRecordHandler().write(bufferedOS, nistRecord)
                        RecordType.RT9 -> MinutiaeDataRecordHandler().write(bufferedOS, nistRecord)
                        RecordType.RT10 -> FacialSMTImageRecordHandler().write(bufferedOS, nistRecord)
                        RecordType.RT11 -> TextRecordHandler(RecordType.RT11).write(bufferedOS, nistRecord)
                        RecordType.RT12 -> TextRecordHandler(RecordType.RT12).write(bufferedOS, nistRecord)
                        RecordType.RT13 -> LatentImageDataRecordHandler().write(bufferedOS, nistRecord)
                        RecordType.RT14 -> VariableResolutionFingerprintRecordHandler().write(bufferedOS, nistRecord)
                        RecordType.RT15 -> PalmRecordHandler().write(bufferedOS, nistRecord)
                        RecordType.RT16 -> TextRecordHandler(RecordType.RT16).write(bufferedOS, nistRecord)
                        RecordType.RT17 -> TextRecordHandler(RecordType.RT17).write(bufferedOS, nistRecord)
                        else -> TextRecordHandler(RecordType.findByRecordId(recordId)).write(bufferedOS, nistRecord)
                    }
                }
            }
            bufferedOS.flush()
        }
    }

    override fun close() {
        outputStream.close()
    }

    companion object {
        val log: Logger = LoggerFactory.getLogger(NistFileWriter::class.java)
    }
}
