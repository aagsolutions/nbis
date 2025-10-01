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

package eu.aagsolutions.img.nbis.model.builders

import eu.aagsolutions.img.nbis.calculators.TextRecordLengthCalculator
import eu.aagsolutions.img.nbis.exceptions.NistException
import eu.aagsolutions.img.nbis.model.enums.RecordType
import eu.aagsolutions.img.nbis.model.enums.records.TransactionInformationFields
import eu.aagsolutions.img.nbis.model.fields.TextField
import eu.aagsolutions.img.nbis.model.records.TransactionInformationRecord
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * A builder class for creating instances of `TransactionInformationRecord`. This class provides
 * methods to add various fields to a transaction information record and manage its construction.
 *
 * The record adheres to NIST standard specifications, and properties such as length and
 * field values are managed during the build process.
 */
@Suppress("TooManyFunctions")
class TransactionInformationRecordBuilder :
    NistRecordBuilder<TransactionInformationRecord, TransactionInformationRecordBuilder>(
        RecordType.RT1.id,
        RecordType.RT1.label,
        TextRecordLengthCalculator(),
    ) {
    override fun build() = TransactionInformationRecord(this.fields)

    /**
     * Adds a version number field to the transaction information record being built.
     *
     * The version number is stored in the `VER` field of the record, represented as a text field.
     *
     * @param version the version number to be stored in the `VER` field of the record.
     * @return an instance of `TransactionInformationRecordBuilder` with the specified version number field set.
     */
    fun withVersionNumberField(version: String) = withField(TransactionInformationFields.VER.id, TextField(version))

    /**
     * Adds a file content field to the transaction information record being built.
     * This field specifies the order of the records in the file and is calculated by
     * @see eu.aagsolutions.img.nbis.calculators.calculateContentField method.
     *
     * The file content is stored in the `CNT` field of the record, represented as a text field.
     *
     * @param fileContent the content to be stored in the `CNT` field of the record.
     * @return an instance of `TransactionInformationRecordBuilder` with the specified file content field set.
     */
    fun withFileContentField(fileContent: String) = withField(TransactionInformationFields.CNT.id, TextField(fileContent))

    /**
     * Adds a transaction type field to the transaction information record being built.
     *
     * The transaction type is stored in the `TOT` field of the record, represented as a text field.
     *
     * @param typeOfTransaction the transaction type to be stored in the `TOT` field of the record.
     * @return an instance of `TransactionInformationRecordBuilder` with the specified transaction type field set.
     */
    fun withTypeOfTransactionField(typeOfTransaction: String) = withField(TransactionInformationFields.TOT.id, TextField(typeOfTransaction))

    /**
     * Adds a date field to the transaction information record being built.
     * The Date format is YYYYMMDD.
     *
     * @param date the date value to be stored in the `DAT` field of the record.
     * @return an instance of `TransactionInformationRecordBuilder` with the specified date field set.
     */
    fun withDateField(date: LocalDate) =
        withField(TransactionInformationFields.DAT.id, TextField(date.format(DateTimeFormatter.ofPattern("yyyyMMdd"))))

    /**
     * Adds a priority field to the transaction information record being built.
     *
     * The priority is stored in the `PRY` field of the record and must be within the range of 0 to 2.
     * If the priority is outside this range, a `NistException` will be thrown.
     *
     * @param priority the priority value to be stored in the `PRY` field of the record.
     *                 It must be a `Short` in the range 0 to 2 inclusive.
     * @return an instance of `TransactionInformationRecordBuilder` with the specified priority field set.
     * @throws NistException if the provided priority is not within the valid range of 0 to 2.
     */
    fun withPriorityField(priority: Short) =
        withField(
            TransactionInformationFields.PRY.id,
            TextField(
                priority
                    .also {
                        require(it in 0..2) { throw NistException("Priority must be between 0 and 2") }
                    }.toString(),
            ),
        )

    /**
     * Adds a destination agency identifier (DAI) field to the transaction information record being built.
     *
     * The destination agency identifier is stored in the `DAI` field of the record, represented as a text field.
     *
     * @param agency the destination agency identifier to be stored in the `DAI` field of the record.
     * @return an instance of `TransactionInformationRecordBuilder` with the specified destination agency field set.
     */
    fun withDestinationAgencyField(agency: String) = withField(TransactionInformationFields.DAI.id, TextField(agency))

    /**
     * Adds an originating agency identifier (ORI) field to the transaction information record being built.
     *
     * The originating agency identifier is stored in the `ORI` field of the record, represented as a text field.
     *
     * @param agency the originating agency identifier to be stored in the `ORI` field of the record.
     * @return an instance of `TransactionInformationRecordBuilder` with the specified originating agency field set.
     */
    fun withOriginatingAgencyField(agency: String) = withField(TransactionInformationFields.ORI.id, TextField(agency))

    /**
     * Adds a transaction control number (TCN) field to the transaction information record being built.
     *
     * The transaction control number is stored in the `TCN` field of the record, represented as a text field.
     *
     * @param transactionControlNumber the transaction control number to be stored in the `TCN` field of the record.
     * @return an instance of `TransactionInformationRecordBuilder` with the specified transaction control number field set.
     */
    fun withTransactionControlNumberField(transactionControlNumber: String) =
        withField(TransactionInformationFields.TCN.id, TextField(transactionControlNumber))

    /**
     * Adds a transaction control reference (TCR) field to the transaction information record being built.
     *
     * The transaction control reference is stored in the `TCR` field of the record, represented as a text field.
     *
     * @param transactionControlReference the transaction control reference to be stored in the `TCR` field of the record.
     * @return an instance of `TransactionInformationRecordBuilder` with the specified transaction control reference field set.
     */
    fun withTransactionControlReferenceField(transactionControlReference: String) =
        withField(TransactionInformationFields.TCR.id, TextField(transactionControlReference))

    /**
     * Adds a native scanning resolution (NSR) field to the transaction information record being built.
     *
     * The native scanning resolution is stored in the `NSR` field of the record, represented as a text field.
     *
     * @param resolution the native scanning resolution to be stored in the `NSR` field of the record.
     * @return an instance of `TransactionInformationRecordBuilder` with the specified native scanning resolution field set.
     */
    fun withNativeScanningResolutionField(resolution: String) = withField(TransactionInformationFields.NSR.id, TextField(resolution))

    /**
     * Adds a nominal transmission resolution (NTR) field to the transaction information record being built.
     *
     * The nominal transmission resolution is stored in the `NTR` field of the record, represented as a text field.
     *
     * @param resolution the nominal transmission resolution to be stored in the `NTR` field of the record.
     * @return an instance of `TransactionInformationRecordBuilder` with the specified nominal transmission resolution field set.
     */
    fun withNominalTransmissionResolutionField(resolution: String) = withField(TransactionInformationFields.NTR.id, TextField(resolution))

    /**
     * Adds a domain name field to the transaction information record being built.
     *
     * The domain name is stored in the `DOM` field of the record, represented as a text field.
     *
     * @param domainName the domain name to be stored in the `DOM` field of the record.
     * @return an instance of `TransactionInformationRecordBuilder` with the specified domain name field set.
     */
    fun withDomainNameField(domainName: String) = withField(TransactionInformationFields.DOM.id, TextField(domainName))

    /**
     * Adds a GMT date and time field to the transaction information record being built.
     *
     * The GMT date and time is formatted as `yyyyMMddHHmmssZ` and stored in the `GMT` field of the record, represented as a text field.
     *
     * @param gmtDateTime the GMT date and time to be stored in the `GMT` field of the record.
     * @return an instance of `TransactionInformationRecordBuilder` with the specified GMT date and time field set.
     */
    fun withGmtDateTimeField(gmtDateTime: LocalDateTime) =
        withField(
            TransactionInformationFields.GMT.id,
            TextField("${gmtDateTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))}Z"),
        )

    /**
     * Adds a directory of character sets field to the transaction information record being built.
     *
     * The directory of character sets is stored in the `DCS` field of the record, represented as a text field.
     *
     * @param directoryOfCharacterSets the directory of character sets to be stored in the `DCS` field of the record.
     * @return an instance of `TransactionInformationRecordBuilder` with the specified directory of character sets field set.
     */
    fun withDirectoryOfCharacterSetsField(directoryOfCharacterSets: String) =
        withField(TransactionInformationFields.DCS.id, TextField(directoryOfCharacterSets))

    /**
     * Adds an application profile specification (APS) field to the transaction information record being built.
     *
     * The application profile specification is stored in the `APS` field of the record, represented as a text field.
     *
     * @param applicationProfileSpecification the application profile specification to be stored in the `APS` field of the record.
     * @return an instance of `TransactionInformationRecordBuilder` with the specified application profile specification field set.
     */
    fun withApplicationProfileSpecificationField(applicationProfileSpecification: String) =
        withField(TransactionInformationFields.APS.id, TextField(applicationProfileSpecification))

    /**
     * Adds an agency names field to the transaction information record being built.
     *
     * The agency names are stored in the `ANM` field of the record, represented as a text field.
     *
     * @param agencyNames the agency names to be stored in the `ANM` field of the record.
     * @return an instance of `TransactionInformationRecordBuilder` with the specified agency names field set.
     */
    fun withAgencyNamesField(agencyNames: String) = withField(TransactionInformationFields.ANM.id, TextField(agencyNames))

    /**
     * Adds a geographic name set field to the transaction information record being built.
     *
     * The geographic name set is stored in the `GNS` field of the record, represented as a text field.
     *
     * @param nameSet the geographic name set to be stored in the `GNS` field of the record.
     * @return an instance of `TransactionInformationRecordBuilder` with the specified geographic name set field set.
     */
    fun withGeographicNameSetField(nameSet: String) = withField(TransactionInformationFields.GNS.id, TextField(nameSet))
}
