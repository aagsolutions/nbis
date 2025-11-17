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

import eu.aagsolutions.img.nbis.exceptions.NistException
import eu.aagsolutions.img.nbis.model.enums.RecordType

object BuilderFactory {
    /**
     * Finds a record builder of type NistRecordBuilder by its recordId.
     *
     * @param recordId The unique identifier for the record to find.
     * @return The record builder of type NistRecordBuilder that matches the given recordId.
     * @throws NistException if no builder is found for the given recordId.
     */
    @Suppress("CyclomaticComplexMethod")
    fun findByRecordId(recordId: Int): NistRecordBuilder<*, *> =
        when (recordId) {
            RecordType.RT1.id -> TransactionInformationRecordBuilder()
            RecordType.RT2.id -> UserDefinedTextRecordBuilder()
            RecordType.RT3.id -> LowResolutionGrayscaleFingerprintRecordBuilder()
            RecordType.RT4.id -> HighResolutionGrayscaleFingerprintRecordBuilder()
            RecordType.RT5.id -> LowResolutionBinaryFingerprintRecordBuilder()
            RecordType.RT6.id -> HighResolutionBinaryFingerprintRecordBuilder()
            RecordType.RT7.id -> UserDefinedImageRecordBuilder()
            RecordType.RT8.id -> SignatureImageRecordBuilder()
            RecordType.RT9.id -> MinutiaeDataRecordBuilder()
            RecordType.RT10.id -> FacialAndSMTImageRecordBuilder()
            RecordType.RT11.id -> ForensicAndInvestigatoryVoiceRecordBuilder()
            RecordType.RT12.id -> ForensicDentalOralRecordBuilder()
            RecordType.RT13.id -> LatentImageRecordBuilder()
            RecordType.RT14.id -> VariableResolutionFingerprintRecordBuilder()
            RecordType.RT15.id -> PalmPrintRecordBuilder()
            RecordType.RT16.id -> UserDefinedTestImageRecordBuilder()
            RecordType.RT17.id -> IrisImageRecordBuilder()
            else -> throw NistException("No builder found for record id $recordId")
        }
}
