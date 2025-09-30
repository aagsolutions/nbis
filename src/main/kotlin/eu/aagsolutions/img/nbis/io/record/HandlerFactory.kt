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

@Suppress("MagicNumber")
enum class HandlerFactory(
    val id: Int,
    val handler: RecordHandler,
) {
    RT1(1, TransactionInformationRecordHandler()),
    RT2(2, UserDefinedDescriptionTextRecordHandler()),
    RT3(3, LowResolutionGrayscaleFingerprintRecordHandler()),
    RT4(4, HighResolutionGrayscaleFingerprintRecordHandler()),
    RT5(5, LowResolutionBinaryFingerprintRecordHandler()),
    RT6(6, HighResolutionBinaryFingerprintRecordHandler()),
    RT7(7, UserDefinedImageRecordHandler()),
    RT8(8, SignatureImageRecordHandler()),
    RT9(9, MinutiaeDataRecordHandler()),
    RT10(10, FacialSMTImageRecordHandler()),
    RT11(11, ForensicAndInvestigatoryVoiceRecordHandler()),
    RT12(12, ForensicDentalOralRecordHandler()),
    RT13(13, LatentImageDataRecordHandler()),
    RT14(14, VariableResolutionFingerprintRecordHandler()),
    RT15(15, PalmPrintImageRecordHandler()),
    RT16(16, UserDefinedTestingImageRecordHandler()),
    RT17(17, IrisImageRecordHandler()),
    ;

    companion object {
        /**
         * Finds a record of type HandlerFactory by its recordId.
         *
         * @param recordId The unique identifier for the record to find.
         * @return The record of type HandlerFactory that matches the given recordId.
         * @throws NistException if no record is found for the given recordId.
         */
        fun findByRecordId(recordId: Int): HandlerFactory =
            HandlerFactory.entries.find { it.id == recordId }
                ?: throw NistException("Record type not supported: $recordId")
    }
}
