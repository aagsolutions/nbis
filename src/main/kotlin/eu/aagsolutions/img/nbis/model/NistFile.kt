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

import eu.aagsolutions.img.nbis.exceptions.NistException
import eu.aagsolutions.img.nbis.model.enums.RecordType
import eu.aagsolutions.img.nbis.model.enums.records.DefaultFields
import eu.aagsolutions.img.nbis.model.records.BaseRecord
import eu.aagsolutions.img.nbis.model.records.TransactionInformationRecord

/**
 * Nist file container.
 *
 * Holds an immutable map of all records grouped by RecordType and provides
 * helpers to retrieve records by type and IDC, the transaction information record, and convenience
 * getters for common record-type lists.
 */
@Suppress("TooManyFunctions")
class NistFile(
    recordsByType: Map<RecordType, List<BaseRecord>>,
) {
    val records: Map<RecordType, List<BaseRecord>> =
        recordsByType.entries
            .associate { (k, v) -> k to v.toList() }
            .toMap()

    /**
     * Returns a list of NIST records by the given type.
     */
    fun getRecordListByRecordType(recordType: RecordType): List<BaseRecord> = records[recordType] ?: emptyList()

    fun getRecordByTypeAndIdc(
        recordType: RecordType,
        idcId: Int,
    ): BaseRecord? =
        (records[recordType] ?: emptyList())
            .firstOrNull { r -> checkIfRT1orIdcIdIsEquals(r, idcId) }

    private fun checkIfRT1orIdcIdIsEquals(
        record: BaseRecord,
        idcId: Int,
    ): Boolean {
        val recordIdcId = record.getFieldAsInt(DefaultFields.IDC) ?: -1
        return record.recordId == RecordType.RT1.id || idcId == recordIdcId
    }

    /**
     * Returns a map of default/extended NIST records (record types with number > 17).
     *
     * @return A map of default/extended NIST records.
     */
    fun getRxMapDefaultRecords(): Map<RecordType, List<BaseRecord>> =
        RecordType.entries
            .filter { it.id > RecordType.RT17.id }
            .associateWith { records[it] ?: emptyList() }
            .toSortedMap(compareBy { it.id })

    /**
     * Retrieves NIST `TransactionInformationRecord`.
     *
     * @return The `TransactionInformationRecord`.
     * @throws NistException If there are no records of type `TransactionInformationRecord`, multiple records of type `TransactionInformationRecord`,
     * or if the record is not a `TransactionInformationRecord`.
     */
    fun getTransactionInformationRecord(): TransactionInformationRecord {
        val records = records[RecordType.RT1]
        if (records == null || records.size != 1 || records.first().recordId != RecordType.RT1.id) {
            throw NistException("Only one record of type 1 is supported")
        }
        return TransactionInformationRecord(records.first().fields)
    }

    /**
     * Returns a list of user-defined text records.
     *
     * @return a list of text records.
     */
    fun getUserDefinedDescriptionTextRecords(): List<BaseRecord> = getRecordsOrEmpty(RecordType.RT2)

    /**
     * Returns a list of low and high-resolution grayscale fingerprint records.
     *
     * @return a list of grayscale fingerprint records.
     */
    fun getLowResolutionGrayscaleFingerprintRecords(): List<BaseRecord> = getRecordsOrEmpty(RecordType.RT3)

    /**
     * Returns a list of high-resolution grayscale fingerprint records.
     *
     * @return a list of high-resolution grayscale fingerprint records.
     */
    fun getHighResolutionGrayscaleFingerprintRecords(): List<BaseRecord> = getRecordsOrEmpty(RecordType.RT4)

    /**
     * Returns a list of low-resolution binary fingerprint records.
     *
     * @return a list of binary fingerprint records.
     */
    fun getLowResolutionBinaryFingerprintRecords(): List<BaseRecord> = getRecordsOrEmpty(RecordType.RT5)

    /**
     * Returns a list of high-resolution binary fingerprint records.
     *
     * @return a list of high-resolution binary fingerprint records.
     */
    fun getHighResolutionBinaryFingerprintRecords(): List<BaseRecord> = getRecordsOrEmpty(RecordType.RT6)

    /**
     * Returns a list of user-defined image records.
     *
     * @return a list of user-defined image records.
     */
    fun getUserDefinedImageRecords(): List<BaseRecord> = getRecordsOrEmpty(RecordType.RT7)

    /**
     * Returns a list of signature image records.
     *
     * @return a list of signature image records.
     */
    fun getSignatureImageRecords(): List<BaseRecord> = getRecordsOrEmpty(RecordType.RT8)

    /**
     * Returns a list of Minutiae Data records.
     *
     * @return a list of Minutiae Data records.
     */
    fun getMinutiaeDataRecords(): List<BaseRecord> = getRecordsOrEmpty(RecordType.RT9)

    /**
     * Returns a list of facial and scars, marks and tattoo image records.
     *
     * @return a list of facial and scars, marks and tattoos image records.
     */
    fun getFacialAndSmtImageRecords(): List<BaseRecord> = getRecordsOrEmpty(RecordType.RT10)

    /**
     * Returns a list of voice records.
     *
     * @return a list of voice records.
     */
    fun getVoiceRecords(): List<BaseRecord> = getRecordsOrEmpty(RecordType.RT11)

    /**
     * Returns a list of dental records.
     *
     * @return a list of dental records.
     */
    fun getDentalRecords(): List<BaseRecord> = getRecordsOrEmpty(RecordType.RT12)

    /**
     * Returns a list of variable resolution latent image records.
     *
     * @return a list of variable resolution latent image records.
     */
    fun getLatentImageRecords(): List<BaseRecord> = getRecordsOrEmpty(RecordType.RT13)

    /**
     * Returns a list of variable-resolution fingerprint records.
     *
     * @return a list of variable-resolution fingerprint records.
     */
    fun getVariableResolutionFingerprintRecords(): List<BaseRecord> = getRecordsOrEmpty(RecordType.RT14)

    /**
     * Returns a list of variable-resolution palm print records.
     *
     * @return a list of variable-resolution palm print records.
     */
    fun getVariableResolutionPalmPrintRecords(): List<BaseRecord> = getRecordsOrEmpty(RecordType.RT15)

    /**
     * Returns a list of user-defined testing image records.
     *
     * @return a list of user-defined testing image records.
     */
    fun getUserDefinedTestingImageRecords(): List<BaseRecord> = getRecordsOrEmpty(RecordType.RT16)

    /**
     * Returns a list of iris image records.
     *
     * @return a list of iris image records.
     */
    fun getIrisImageRecords(): List<BaseRecord> = getRecordsOrEmpty(RecordType.RT17)

    /**
     * Retrieves a list of records for the specified record type or an empty list if none exist.
     *
     * @param recordType the type of record to retrieve.
     * @return A list of records for the specified type, or an empty list if no records are available.
     */
    private fun getRecordsOrEmpty(recordType: RecordType): List<BaseRecord> = records[recordType] ?: emptyList()
}
