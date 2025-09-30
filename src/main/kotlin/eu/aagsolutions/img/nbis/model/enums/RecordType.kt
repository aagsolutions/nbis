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

package eu.aagsolutions.img.nbis.model.enums

import eu.aagsolutions.img.nbis.exceptions.NistException

/**
 * Represents a record type as defined by the ANSI/NIST-ITL standards.
 *
 * Each record type includes a unique identifier, descriptive label,
 * the standard from which it is defined, and optionally, the standard from
 * which it becomes deprecated.
 *
 * @property id The unique numeric identifier of the record type.
 * @property label A descriptive label for the record type.
 * @property createdFromStandard The ANSI/NIST-ITL standard in which this record type was introduced.
 * @property deprecatedFromStandard The ANSI/NIST-ITL standard in which this record type was deprecated, or null if it is not deprecated.
 */
@Suppress("MagicNumber")
enum class RecordType(
    val id: Int,
    val label: String,
    val createdFromStandard: Standard,
    val deprecatedFromStandard: Standard?,
) {
    RT1(1, "Type 1 - Transaction Information", Standard.ANSI_NIST_ITL_2000, null),
    RT2(2, "Type 2 - Descriptive Text (User)", Standard.ANSI_NIST_ITL_2000, null),
    RT3(3, "Type 3 - Low-resolution grayscale", Standard.ANSI_NIST_ITL_2000, Standard.ANSI_NIST_ITL_2011),
    RT4(4, "Type 4 - High-resolution grayscale", Standard.ANSI_NIST_ITL_2000, null),
    RT5(5, "Type 5 - Low-resolution binary", Standard.ANSI_NIST_ITL_2000, Standard.ANSI_NIST_ITL_2011),
    RT6(6, "Type 6 - High-resolution binary", Standard.ANSI_NIST_ITL_2000, Standard.ANSI_NIST_ITL_2011),
    RT7(7, "Type 7 - User-defined Image data", Standard.ANSI_NIST_ITL_2000, null),
    RT8(8, "Type 8 - Signature image data", Standard.ANSI_NIST_ITL_2000, null),
    RT9(9, "Type 9 - Minutiae data", Standard.ANSI_NIST_ITL_2000, null),
    RT10(10, "Type 10 - Facial and SMT image data", Standard.ANSI_NIST_ITL_2000, null),
    RT11(11, "Type 11 -  Forensic and investigatory voice record", Standard.ANSI_NIST_ITL_2013, null),
    RT12(12, "Type 12 - Forensic dental and oral record", Standard.ANSI_NIST_ITL_2013, null),
    RT13(13, "Type 13 - Latent image data (VR)", Standard.ANSI_NIST_ITL_2000, null),
    RT14(14, "Type 14 - Tenprint Fingerprint (VR)", Standard.ANSI_NIST_ITL_2000, null),
    RT15(15, "Type 15 - Palm print image data (VR)", Standard.ANSI_NIST_ITL_2000, null),
    RT16(16, "Type 16 - User-defined testing image record", Standard.ANSI_NIST_ITL_2000, null),
    RT17(17, "Type 17 - Iris image data", Standard.ANSI_NIST_ITL_2007, null),
    RT18(18, "Type 18 - DNA data", Standard.ANSI_NIST_ITL_2011, null),
    RT19(19, "Type 19 - Plantar image data", Standard.ANSI_NIST_ITL_2011, null),
    RT20(20, "Type 20 - Source representation", Standard.ANSI_NIST_ITL_2011, null),
    RT21(21, "Type 21 - Associated context", Standard.ANSI_NIST_ITL_2011, null),
    RT22(22, "Type 22 - Non-photographic imagery data", Standard.ANSI_NIST_ITL_2013, null),
    RT98(98, "Type 98 - Information assurance record", Standard.ANSI_NIST_ITL_2011, null),
    RT99(99, "Type 99 - CBEFF biometric data record", Standard.ANSI_NIST_ITL_2007, null),
    ;

    private fun isRecordTypeAllowedForStandard(standard: Standard): Boolean {
        return standard.isBetweenStandards(
            this.createdFromStandard,
            this.deprecatedFromStandard ?: return true,
        )
    }

    /**
     * Provides utility functions for interacting with `RecordType` in the context of standards and records.
     */
    companion object {
        /**
         * Filters and retrieves a list of record types that are not allowed for a specific standard.
         *
         * @param standard The standard to evaluate against the record types.
         * @return A list of record types that are forbidden for the given standard.
         */
        fun forbiddenRecordTypesByStandard(standard: Standard): List<RecordType> =
            entries.filter { !it.isRecordTypeAllowedForStandard(standard) }

        /**
         * Finds a record of type RecordType by its recordId.
         *
         * @param recordId The unique identifier for the record to find.
         * @return The record of type RecordType that matches the given recordId.
         * @throws NistException if no record is found for the given recordId.
         */
        fun findByRecordId(recordId: Int): RecordType =
            entries.find { it.id == recordId }
                ?: throw NistException("Record type not found: $recordId")
    }
}
