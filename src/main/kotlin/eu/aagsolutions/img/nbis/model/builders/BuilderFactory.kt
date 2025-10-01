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

@Suppress("MagicNumber")
enum class BuilderFactory(
    val id: Int,
    val builder: NistRecordBuilder<*, *>,
) {
    RT1(1, TransactionInformationRecordBuilder()),
    RT2(2, UserDefinedTextRecordBuilder()),
    RT3(3, LowResolutionGrayscaleFingerprintRecordBuilder()),
    RT4(4, HighResolutionGrayscaleFingerprintRecordBuilder()),
    RT5(5, LowResolutionBinaryFingerprintRecordBuilder()),
    RT6(6, HighResolutionBinaryFingerprintRecordBuilder()),
    RT7(7, UserDefinedImageRecordBuilder()),
    RT8(8, SignatureImageRecordBuilder()),
    RT9(9, MinutiaeDataRecordBuilder()),
    RT10(10, FacialAndSMTImageRecordBuilder()),
    RT11(11, ForensicAndInvestigatoryVoiceRecordBuilder()),
    RT12(12, ForensicDentalOralRecordBuilder()),
    RT13(13, LatentImageRecordBuilder()),
    RT14(14, VariableResolutionFingerprintRecordBuilder()),
    RT15(15, PalmPrintRecordBuilder()),
    RT16(16, UserDefinedTestImageRecordBuilder()),
    RT17(17, IrisImageRecordBuilder()),
    ;

    companion object {
        /**
         * Finds a record of type HandlerFactory by its recordId.
         *
         * @param recordId The unique identifier for the record to find.
         * @return The record of type HandlerFactory that matches the given recordId.
         * @throws NistException if no record is found for the given recordId.
         */
        fun findByRecordId(recordId: Int): BuilderFactory =
            entries.find { it.id == recordId }
                ?: throw NistException("Record type not supported: $recordId")
    }
}
