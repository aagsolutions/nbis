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
import eu.aagsolutions.img.nbis.model.enums.RecordType
import eu.aagsolutions.img.nbis.model.enums.records.UserDefinedTextFields
import eu.aagsolutions.img.nbis.model.fields.TextField
import eu.aagsolutions.img.nbis.model.records.UserDefinedTextRecord

/**
 * Builder for creating instances of UserDefinedTextRecord (NIST Type-2 record).
 *
 * This builder provides fluent helper methods to populate common Type-2 fields
 * defined by the NIST standard (see UserDefinedTextFields). The record length (LEN)
 * is automatically calculated and injected at build() time using TextRecordLengthCalculator.
 */
@Suppress("TooManyFunctions")
class UserDefinedTextRecordBuilder :
    NistRecordBuilder<UserDefinedTextRecord, UserDefinedTextRecordBuilder>(
        RecordType.RT2.id,
        RecordType.RT2.label,
        TextRecordLengthCalculator(),
    ) {
    override fun build() = UserDefinedTextRecord(this.fields)

    /**
     * Sets IDC (Information Designation Character) – the record sequence number within the transaction.
     *
     * @param designationChar value for the IDC field
     */
    fun withInformationDesignationCharField(designationChar: String) = withField(UserDefinedTextFields.IDC.id, TextField(designationChar))

    /**
     * Sets SYS – system information field identifying the originating system/application.
     *
     * @param systemInformationField value for the SYS field
     */
    fun withSystemInformationField(systemInformationField: String) =
        withField(UserDefinedTextFields.SYS.id, TextField(systemInformationField))

    /**
     * Sets DAR – date of record creation.
     * The expected format should follow the profile being used (e.g., YYYYMMDD).
     *
     * @param dateOfRecord value for the DAR field
     */
    fun withDateOfRecordField(dateOfRecord: String) = withField(UserDefinedTextFields.DAR.id, TextField(dateOfRecord))

    /**
     * Sets DLU – date of the last update to this record.
     *
     * @param dateOfLastUpdate value for the DLU field
     */
    fun withDateOfLastUpdateField(dateOfLastUpdate: String) = withField(UserDefinedTextFields.DLU.id, TextField(dateOfLastUpdate))

    /**
     * Sets SCT – send copy to.
     *
     * @param sendCopy value for the SCT field
     */
    fun withSendCopyToField(sendCopy: String) = withField(UserDefinedTextFields.SCT.id, TextField(sendCopy))

    /**
     * Sets SQN – sequence number.
     *
     * @param sequenceNumber value for the SQN field
     */
    fun withSequenceNumberField(sequenceNumber: String) = withField(UserDefinedTextFields.SQN.id, TextField(sequenceNumber))

    /**
     * Sets MID – latent identifier.
     *
     * @param latenIdentifier value for the MID field
     */
    fun withLatentIdentifierField(latenIdentifier: String) = withField(UserDefinedTextFields.MID.id, TextField(latenIdentifier))

    /**
     * Sets CRN – criminal reference number.
     *
     * @param criminalReferenceNumber value for the CRN field
     */
    fun withCriminalReferenceNumber(criminalReferenceNumber: String) =
        withField(UserDefinedTextFields.CRN.id, TextField(criminalReferenceNumber))

    /**
     * Sets ORN – order reference number.
     *
     * @param orderReferenceNumber value for the ORN field
     */
    fun withOrderReferenceNumber(orderReferenceNumber: String) = withField(UserDefinedTextFields.ORN.id, TextField(orderReferenceNumber))

    /**
     * Sets CNO – case number.
     *
     * @param caseNumber value for the CNO field
     */
    fun withCaseNumber(caseNumber: String) = withField(UserDefinedTextFields.CNO.id, TextField(caseNumber))

    /**
     * Sets MN1 – miscellaneous identification number 1.
     *
     * @param miscellaneousId1 value for the MN1 field
     */
    fun withMiscellaneousIdentificationNumber1(miscellaneousId1: String) =
        withField(UserDefinedTextFields.MN1.id, TextField(miscellaneousId1))

    /**
     * Sets MN2 – miscellaneous identification number 2.
     *
     * @param miscellaneousId2 value for the MN2 field
     */
    fun withMiscellaneousIdentificationNumber2(miscellaneousId2: String) =
        withField(UserDefinedTextFields.MN2.id, TextField(miscellaneousId2))

    /**
     * Sets MN3 – miscellaneous identification number 3.
     *
     * @param miscellaneousId3 value for the MN3 field
     */
    fun withMiscellaneousIdentificationNumber3(miscellaneousId3: String) =
        withField(UserDefinedTextFields.MN3.id, TextField(miscellaneousId3))

    /**
     * Sets MN4 – miscellaneous identification number 4.
     *
     * @param miscellaneousId4 value for the MN4 field
     */
    fun withMiscellaneousIdentificationNumber4(miscellaneousId4: String) =
        withField(UserDefinedTextFields.MN4.id, TextField(miscellaneousId4))

    /**
     * Sets MN5 – miscellaneous identification number 5.
     *
     * @param miscellaneousId5 value for the MN5 field
     */
    fun withMiscellaneousIdentificationNumber5(miscellaneousId5: String) =
        withField(UserDefinedTextFields.MN5.id, TextField(miscellaneousId5))

    /**
     * Sets FNU – finger number.
     *
     * @param fingerNumber value for the FNU field
     */
    fun withFingerNumber(fingerNumber: String) = withField(UserDefinedTextFields.FNU.id, TextField(fingerNumber))

    /**
     * Sets FIB – fingerprint identification byte.
     *
     * @param fingerprintIdByte value for the FIB field
     */
    fun withFingerprintIdentificationByte(fingerprintIdByte: String) = withField(UserDefinedTextFields.FIB.id, TextField(fingerprintIdByte))

    /**
     * Sets DPR – date fingerprinted.
     * The expected format should follow the profile being used.
     *
     * @param dateFingerprinted value for the DPR field
     */
    fun withDateFingerprinted(dateFingerprinted: String) = withField(UserDefinedTextFields.DPR.id, TextField(dateFingerprinted))

    /**
     * Sets TOF – time of fingerprinting.
     *
     * @param timeOfFingerprinting value for the TOF field
     */
    fun withTimeOfFingerprinting(timeOfFingerprinting: String) = withField(UserDefinedTextFields.TOF.id, TextField(timeOfFingerprinting))

    /**
     * Sets RFP – reason fingerprinted.
     *
     * @param reasonFingerprinted value for the RFP field
     */
    fun withReasonFingerprinted(reasonFingerprinted: String) = withField(UserDefinedTextFields.RFP.id, TextField(reasonFingerprinted))

    /**
     * Sets POA – place of arrest.
     *
     * @param placeOfArrest value for the POA field
     */
    fun withPlaceOfArrest(placeOfArrest: String) = withField(UserDefinedTextFields.POA.id, TextField(placeOfArrest))
}
