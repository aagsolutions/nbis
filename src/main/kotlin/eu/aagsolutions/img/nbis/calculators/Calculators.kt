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

package eu.aagsolutions.img.nbis.calculators

import eu.aagsolutions.img.nbis.datakit.AsciiHelper.GROUP_SEPARATOR
import eu.aagsolutions.img.nbis.datakit.AsciiHelper.RECORD_SEPARATOR
import eu.aagsolutions.img.nbis.datakit.AsciiHelper.UNIT_SEPARATOR
import eu.aagsolutions.img.nbis.exceptions.NistException
import eu.aagsolutions.img.nbis.model.NistEntry
import eu.aagsolutions.img.nbis.model.enums.RecordType
import eu.aagsolutions.img.nbis.model.enums.records.DefaultFields
import eu.aagsolutions.img.nbis.model.fields.Field
import eu.aagsolutions.img.nbis.model.records.BaseRecord
import kotlin.math.pow

object Calculators {
    val CHECK_DIGIT_TABLE =
        charArrayOf(
            'Z',
            'A',
            'B',
            'C',
            'D',
            'E',
            'F',
            'G',
            'H',
            'J',
            'K',
            'L',
            'M',
            'N',
            'P',
            'Q',
            'R',
            'T',
            'U',
            'V',
            'W',
            'X',
            'Y',
        )

    const val FIELD_SIZE = 11
    const val YY_SIZE = 2
    const val TCN_AGENCY_NUMBER_OF_CHARS = 4
    const val TCN_SEQUENCE_NUMBER_OF_CHARS = 6
    const val ASCII_BASE = 1000

    @Suppress("MagicNumber")
    val POW_10_8 = 10.0.pow(8.0).toLong()

    /**
     * Calculates the content field based on the map of all records, excluding records of type RT1.
     * Converts relevant records into a list of `NistEntry` objects with their record IDs and
     * associated values, and prepends a summary entry indicating the total number of records.
     *
     * @param mapOfAllRecords A map where the key is a `RecordType` and the value is a list of `BaseRecord`.
     *                        The records of type RT1 are excluded from processing.
     * @return A list of `NistEntry` objects. The first entry represents the summary with the total
     *         number of records, followed by entries representing each applicable record.
     */
    fun calculateContentField(mapOfAllRecords: Map<RecordType, List<BaseRecord>>): List<NistEntry> {
        val allRecords =
            mapOfAllRecords
                .filterKeys { it != RecordType.RT1 }
                .flatMap { it.value }

        val tocList =
            allRecords.map { nistRecord ->
                val recordId = nistRecord.recordId.toString()
                val idcValue = nistRecord.getFieldText(DefaultFields.IDC)?.toIntOrNull()?.toString() ?: "0"
                NistEntry(recordId, idcValue)
            }

        val nbRecords = tocList.size.toString()
        val recordId = RecordType.RT1.id.toString()
        val tocEntry = NistEntry(recordId, nbRecords)

        return listOf(tocEntry) + tocList
    }

    /**
     * Calculates the length of the prefix for a given record identifier.
     *
     * The prefix format includes a group separator, the record identifier, and specific
     * formatting (e.g., ".001:"). The function computes the length of this formatted prefix.
     *
     * @param recordId The identifier of the record for which the prefix length is being calculated.
     *                 It can be null, in which case the behavior depends on how the string interpolation resolves nulls.
     * @return The length of the prefix as an integer.
     */
    fun calculatePrefixLength(recordId: Int?): Int = "${GROUP_SEPARATOR}$recordId.001:".length

    /**
     * Computes the total length of a logical record by incorporating the lengths of its fields,
     * the prefix length, and additional adjustments based on the size of the length itself.
     *
     * @param recordId The identifier of the record for which the length is being calculated.
     * @param fields A map of field identifiers to their corresponding Field instances,
     *               where each entry represents a field within the record.
     * @return The calculated total length of the logical record as an integer.
     */
    fun calculateLength(
        recordId: Int,
        fields: Map<Int, Field<*>>,
    ): Int {
        val defaultPrefixLength = calculatePrefixLength(recordId)

        val allFieldsLengthWithoutLEN =
            fields.entries
                .filter { it.key != 1 }
                .sumOf { it.value.getLength() + defaultPrefixLength }

        val initialLenSize = allFieldsLengthWithoutLEN.toString().length
        val totalLengthWithLEN = allFieldsLengthWithoutLEN + initialLenSize + defaultPrefixLength

        return if (totalLengthWithLEN.toString().length > initialLenSize) {
            totalLengthWithLEN + 1
        } else {
            totalLengthWithLEN
        }
    }

    /**
     * Calculates the last character of the control number based on the provided `left` and `right` numeric values.
     *
     * The control number calculation involves combining the `left` part and the `right` part into a single number,
     * applying a modulo operation with the size of a predefined table, and using the result as an index to retrieve
     * a character from the table.
     *
     * @param left the left numeric part of the control number
     * @param right the right numeric part of the control number
     * @return the calculated last character of the control number
     */
    fun calculateControlNumberLastCharacter(
        left: Long,
        right: Long,
    ): Char {
        val sum: Long = (left * POW_10_8) + right
        val modulo = (sum % CHECK_DIGIT_TABLE.size).toInt()
        return CHECK_DIGIT_TABLE[modulo]
    }

    /**
     * Validates a control character field and calculates the last character of the control number.
     *
     * This method handles both numeric and non-numeric characters by converting characters to their
     * numeric representation for calculation purposes while preserving the original format.
     *
     * @param controlCharacterField The input string representing the control character field,
     *                              which should be of a valid length (10 characters).
     * @return A string representing the original control character field with the calculated
     *         control number's last character appended.
     * @throws NistException If the input field is not of the required length.
     */
    fun calculateControlNumberLastCharacter(controlCharacterField: String): String {
        if (controlCharacterField.length != FIELD_SIZE - 1) {
            throw NistException("Field should be 10 char long, but was ${controlCharacterField.length}")
        }
        // Convert characters to numeric values for calculation
        val leftPart = controlCharacterField.take(YY_SIZE)
        val rightPart = controlCharacterField.substring(YY_SIZE)

        // Convert each character to its ASCII value and combine into numbers
        val left = convertStringToNumeric(leftPart)
        val right = convertStringToNumeric(rightPart)

        return controlCharacterField +
            calculateControlNumberLastCharacter(left, right)
    }

    /**
     * Converts a string to a numeric representation by using ASCII values of characters.
     * For numeric characters, uses their face value. For non-numeric characters, uses ASCII values.
     *
     * @param input The input string to convert
     * @return A Long representing the numeric value
     */
    private fun convertStringToNumeric(input: String): Long =
        input.fold(0L) { acc, char ->
            val value =
                if (char.isDigit()) {
                    char.digitToInt().toLong()
                } else {
                    char.code.toLong()
                }
            acc * ASCII_BASE + value
        }

    /**
     * Generates an Agency Transaction Control Number (TCN) by combining the agency code, sequence number,
     * and a calculated control character.
     *
     * @param agencyCode A string representing the agency code used to generate the Agency TCN.
     *                   It will be truncated or padded to a fixed length.
     * @param sequence A long representing the sequence number to be included in the Agency TCN.
     *                 It will be zero-padded to a fixed length.
     * @return A string representing the generated Agency TCN including the control character.
     */
    fun generateAgencyTCN(
        agencyCode: String,
        sequence: Long,
    ): String {
        val agency = agencyCode.take(TCN_AGENCY_NUMBER_OF_CHARS).padEnd(TCN_AGENCY_NUMBER_OF_CHARS, '0')
        val seq = sequence.toString().padStart(TCN_SEQUENCE_NUMBER_OF_CHARS, '0')
        val tcnBase = "$agency$seq"
        return calculateControlNumberLastCharacter(tcnBase)
    }

    /**
     * Converts a list of NistEntry objects into a single string.
     *
     * @param entries The list of NistEntry objects to convert.
     * @return The converted string.
     */
    fun fromListOfNistEntry(entries: List<NistEntry>): String =
        if (entries.isEmpty()) {
            ""
        } else {
            entries.joinToString(separator = RECORD_SEPARATOR.toString()) {
                it.key + UNIT_SEPARATOR + it.value
            }
        }
}
