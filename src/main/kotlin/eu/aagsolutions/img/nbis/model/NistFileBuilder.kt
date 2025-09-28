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

package eu.aagsolutions.img.nbis.model

import eu.aagsolutions.img.nbis.calculators.calculateContentField
import eu.aagsolutions.img.nbis.calculators.fromListOfNistEntry
import eu.aagsolutions.img.nbis.model.builders.TransactionInformationRecordBuilder
import eu.aagsolutions.img.nbis.model.enums.RecordType
import eu.aagsolutions.img.nbis.model.records.BaseRecord
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

@Suppress("TooManyFunctions")
class NistFileBuilder {
    private val recordsMap = mutableMapOf<RecordType, List<BaseRecord>>()

    private fun addRecord(
        recordType: RecordType,
        nistRecord: BaseRecord,
    ) {
        recordsMap[recordType] = recordsMap.getOrDefault(recordType, emptyList()) + nistRecord
    }

    private fun addTransactionInformationRecord(nistRecord: BaseRecord) {
        recordsMap[RecordType.RT1] = emptyList<BaseRecord>() + nistRecord
    }

    fun withTransactionInformationRecord(record: TransactionInformationRecord): NistFileBuilder {
        addTransactionInformationRecord(record)
        return this
    }

    fun withUserDefinedDescriptionTextRecords(records: List<UserDefinedTextRecord>): NistFileBuilder {
        records.forEach { r -> addRecord(RecordType.RT2, r) }
        return this
    }

    fun withLowResolutionGrayscaleFingerprintRecords(records: List<LowResolutionGrayscaleFingerprintRecord>): NistFileBuilder {
        records.forEach { r -> addRecord(RecordType.RT3, r) }
        return this
    }

    fun withHighResolutionGrayscaleFingerprintRecords(records: List<HighResolutionGrayscaleFingerprintRecord>): NistFileBuilder {
        records.forEach { r -> addRecord(RecordType.RT4, r) }
        return this
    }

    fun withLowResolutionBinaryFingerprintRecords(records: List<LowResolutionBinaryFingerprintRecord>): NistFileBuilder {
        records.forEach { r -> addRecord(RecordType.RT5, r) }
        return this
    }

    fun withHighResolutionBinaryFingerprintRecords(records: List<HighResolutionBinaryFingerprintRecord>): NistFileBuilder {
        records.forEach { r -> addRecord(RecordType.RT6, r) }
        return this
    }

    fun withUserDefinedImageRecords(records: List<UserDefinedImageRecord>): NistFileBuilder {
        records.forEach { r -> addRecord(RecordType.RT7, r) }
        return this
    }

    fun withSignatureImageRecords(records: List<SignatureImageRecord>): NistFileBuilder {
        records.forEach { r -> addRecord(RecordType.RT8, r) }
        return this
    }

    fun withMinutiaeDataRecords(records: List<MinutiaeDataRecord>): NistFileBuilder {
        records.forEach { r -> addRecord(RecordType.RT9, r) }
        return this
    }

    fun withFacialAndSmtImageRecords(records: List<FacialAndSMTImageRecord>): NistFileBuilder {
        records.forEach { r -> addRecord(RecordType.RT10, r) }
        return this
    }

    fun withLatentImageRecords(records: List<LatentImageRecord>): NistFileBuilder {
        records.forEach { r -> addRecord(RecordType.RT13, r) }
        return this
    }

    fun withVariableResolutionFingerprintRecords(records: List<VariableResolutionFingerprintRecord>): NistFileBuilder {
        records.forEach { r -> addRecord(RecordType.RT14, r) }
        return this
    }

    fun withVariableResolutionPalmPrintRecords(records: List<PalmPrintRecord>): NistFileBuilder {
        records.forEach { r -> addRecord(RecordType.RT15, r) }
        return this
    }

    fun withUserDefinedTestingImageRecords(records: List<BaseRecord>): NistFileBuilder {
        records.forEach { r -> addRecord(RecordType.RT16, r) }
        return this
    }

    fun withIrisImageRecords(records: List<BaseRecord>): NistFileBuilder {
        records.forEach { r -> addRecord(RecordType.RT17, r) }
        return this
    }

    fun build(): NistFile {
        val fileContent = fromListOfNistEntry(calculateContentField(this.recordsMap))
        val transactionInformationBuilder =
            TransactionInformationRecordBuilder().fromRecord(
                this.recordsMap[RecordType.RT1]!![0] as TransactionInformationRecord,
            ) as TransactionInformationRecordBuilder
        val transactionInformationRecord =
            transactionInformationBuilder
                .withFileContentField(
                    fileContent,
                ).build()
        this.withTransactionInformationRecord(transactionInformationRecord)
        return NistFile(this.recordsMap)
    }
}
