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
import eu.aagsolutions.img.nbis.model.records.IrisImageRecord
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

/**
 * Builder class for creating and modifying NIST files.
 */
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

    /**
     * Adds a `TransactionInformationRecord` to the builder and allows chaining additional configurations.
     *
     * @param record The `TransactionInformationRecord` instance to be added.
     * @return The current `NistFileBuilder` instance, allowing for method chaining.
     */
    fun withTransactionInformationRecord(record: TransactionInformationRecord): NistFileBuilder {
        addTransactionInformationRecord(record)
        return this
    }

    /**
     * Adds a list of `UserDefinedTextRecord` (Interpol standard)
     * instances to the builder and allows chaining additional configurations.
     *
     * @param records One or more `UserDefinedTextRecord` instances to be added.
     * @return The current `NistFileBuilder` instance, allowing for method chaining.
     */
    fun withUserDefinedDescriptionTextRecords(vararg records: UserDefinedTextRecord): NistFileBuilder {
        records.forEach { r -> addRecord(RecordType.RT2, r) }
        return this
    }

    /**
     * Adds a list of `LowResolutionGrayscaleFingerprintRecord` instances to the builder.
     *
     * @param records The list of `LowResolutionGrayscaleFingerprintRecord` instances to be added.
     * @return The current `NistFileBuilder` instance, allowing for method chaining.
     */
    fun withLowResolutionGrayscaleFingerprintRecords(vararg records: LowResolutionGrayscaleFingerprintRecord): NistFileBuilder {
        records.forEach { r -> addRecord(RecordType.RT3, r) }
        return this
    }

    /**
     * Adds a list of `HighResolutionGrayscaleFingerprintRecord` instances to the builder.
     *
     * @param records The list of `HighResolutionGrayscaleFingerprintRecord` instances to be added.
     * @return The current `NistFileBuilder` instance, allowing for method chaining.
     */
    fun withHighResolutionGrayscaleFingerprintRecords(vararg records: HighResolutionGrayscaleFingerprintRecord): NistFileBuilder {
        records.forEach { r -> addRecord(RecordType.RT4, r) }
        return this
    }

    /**
     * Adds a list of `LowResolutionBinaryFingerprintRecord` instances to the builder.
     *
     * @param records The list of `LowResolutionBinaryFingerprintRecord` instances to be added.
     * @return The current `NistFileBuilder` instance, allowing for method chaining.
     */
    fun withLowResolutionBinaryFingerprintRecords(vararg records: LowResolutionBinaryFingerprintRecord): NistFileBuilder {
        records.forEach { r -> addRecord(RecordType.RT5, r) }
        return this
    }

    /**
     * Adds a list of `HighResolutionBinaryFingerprintRecord` instances to the builder.
     *
     * @param records The list of `HighResolutionBinaryFingerprintRecord` instances to be added.
     * @return The current `NistFileBuilder` instance, allowing for method chaining.
     */
    fun withHighResolutionBinaryFingerprintRecords(vararg records: HighResolutionBinaryFingerprintRecord): NistFileBuilder {
        records.forEach { r -> addRecord(RecordType.RT6, r) }
        return this
    }

    /**
     * Adds a list of `UserDefinedImageRecord` instances to the builder.
     *
     * @param records The list of `UserDefinedImageRecord` instances to be added.
     * @return The current `NistFileBuilder` instance, allowing for method chaining.
     */
    fun withUserDefinedImageRecords(vararg records: UserDefinedImageRecord): NistFileBuilder {
        records.forEach { r -> addRecord(RecordType.RT7, r) }
        return this
    }

    /**
     * Adds a list of `SignatureImageRecord` instances to the builder.
     *
     * @param records The list of `SignatureImageRecord` instances to be added.
     * @return The current `NistFileBuilder` instance, allowing for method chaining.
     */
    fun withSignatureImageRecords(vararg records: SignatureImageRecord): NistFileBuilder {
        records.forEach { r -> addRecord(RecordType.RT8, r) }
        return this
    }

    /**
     * Adds a list of `MinutiaeDataRecord` instances to the builder.
     *
     * @param records The list of `MinutiaeDataRecord` instances to be added.
     * @return The current `NistFileBuilder` instance, allowing for method chaining.
     */
    fun withMinutiaeDataRecords(vararg records: MinutiaeDataRecord): NistFileBuilder {
        records.forEach { r -> addRecord(RecordType.RT9, r) }
        return this
    }

    /**
     * Adds a list of `FacialAndSMTImageRecord` instances to the builder.
     *
     * @param records The list of `FacialAndSMTImageRecord` instances to be added.
     * @return The current `NistFileBuilder` instance, allowing for method chaining.
     */
    fun withFacialAndSmtImageRecords(vararg records: FacialAndSMTImageRecord): NistFileBuilder {
        records.forEach { r -> addRecord(RecordType.RT10, r) }
        return this
    }

    /**
     * Adds a list of `LatentImageRecord` instances to the builder.
     *
     * @param records The list of `LatentImageRecord` instances to be added.
     * @return The current `NistFileBuilder` instance, allowing for method chaining.
     */
    fun withLatentImageRecords(vararg records: LatentImageRecord): NistFileBuilder {
        records.forEach { r -> addRecord(RecordType.RT13, r) }
        return this
    }

    /**
     * Adds a list of `VariableResolutionFingerprintRecord` instances to the builder.
     *
     * @param records The list of `VariableResolutionFingerprintRecord` instances to be added.
     * @return The current `NistFileBuilder` instance, allowing for method chaining.
     */
    fun withVariableResolutionFingerprintRecords(vararg records: VariableResolutionFingerprintRecord): NistFileBuilder {
        records.forEach { r -> addRecord(RecordType.RT14, r) }
        return this
    }

    /**
     * Adds a list of `PalmPrintRecord` instances to the builder.
     *
     * @param records The list of `PalmPrintRecord` instances to be added.
     * @return The current `NistFileBuilder` instance, allowing for method chaining.
     */
    fun withVariableResolutionPalmPrintRecords(vararg records: PalmPrintRecord): NistFileBuilder {
        records.forEach { r -> addRecord(RecordType.RT15, r) }
        return this
    }

    /**
     * Adds a list of `UserDefinedTestingImageRecord` instances to the builder.
     *
     * @param records The list of `UserDefinedTestingImageRecord` instances to be added.
     * @return The current `NistFileBuilder` instance, allowing for method chaining.
     */
    fun withUserDefinedTestingImageRecords(vararg records: UserDefinedImageRecord): NistFileBuilder {
        records.forEach { r -> addRecord(RecordType.RT16, r) }
        return this
    }

    /**
     * Adds a list of `IrisImageRecord` instances to the builder.
     *
     * @param records The list of `IrisImageRecord` instances to be added.
     * @return The current `NistFileBuilder` instance, allowing for method chaining.
     */
    fun withIrisImageRecords(vararg records: IrisImageRecord): NistFileBuilder {
        records.forEach { r -> addRecord(RecordType.RT17, r) }
        return this
    }

    /**
     * Builds a new instance of `NistFile` using the records stored in the builder.
     *
     * The method processes the existing records to generate the appropriate content
     * for the `TransactionInformationRecord`, incorporates the constructed record into
     * the builder, and finally creates and returns a `NistFile` object.
     *
     * @return A new `NistFile` instance containing the records and configurations set in the builder.
     */
    fun build(): NistFile {
        val fileContent = fromListOfNistEntry(calculateContentField(this.recordsMap))
        val transactionInformationBuilder =
            TransactionInformationRecordBuilder().fromRecord(
                this.recordsMap[RecordType.RT1]!![0] as TransactionInformationRecord,
            )
        val transactionInformationRecord =
            transactionInformationBuilder
                .withFileContentField(
                    fileContent,
                ).build()
        this.withTransactionInformationRecord(transactionInformationRecord)
        return NistFile(this.recordsMap)
    }
}
