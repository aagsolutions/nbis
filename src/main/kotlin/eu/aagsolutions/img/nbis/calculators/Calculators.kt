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

import eu.aagsolutions.img.nbis.exceptions.NistException
import eu.aagsolutions.img.nbis.io.GROUP_SEPARATOR
import eu.aagsolutions.img.nbis.io.RECORD_SEPARATOR
import eu.aagsolutions.img.nbis.io.UNIT_SEPARATOR
import eu.aagsolutions.img.nbis.model.NistEntry
import eu.aagsolutions.img.nbis.model.enums.RecordType
import eu.aagsolutions.img.nbis.model.enums.records.DefaultFields
import eu.aagsolutions.img.nbis.model.fields.Field
import eu.aagsolutions.img.nbis.model.records.BaseRecord
import kotlin.math.pow

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

@Suppress("MagicNumber")
val POW_10_8 = 10.0.pow(8.0).toLong()

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

fun calculatePrefixLength(recordId: Int?): Int = "${GROUP_SEPARATOR}$recordId.001:".length

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

fun calculateControlNumberLastCharacter(
    left: Long,
    right: Long,
): Char {
    val sum: Long = (left * POW_10_8) + right
    val modulo = (sum % CHECK_DIGIT_TABLE.size).toInt()
    return CHECK_DIGIT_TABLE[modulo]
}

fun calculateControlNumberLastCharacter(controlCharacterField: String): String {
    if (controlCharacterField.length != FIELD_SIZE - 1) {
        throw NistException("Field should be 10 char long")
    }
    try {
        val left =
            controlCharacterField
                .take(YY_SIZE)
                .toLong()
        val right =
            controlCharacterField
                .substring(YY_SIZE)
                .toLong()
        return controlCharacterField +
            calculateControlNumberLastCharacter(
                left,
                right,
            )
    } catch (e: NumberFormatException) {
        throw NistException("Field should contain only number", e)
    }
}

fun fromListOfNistEntry(entries: List<NistEntry>): String =
    if (entries.isEmpty()) {
        ""
    } else {
        entries.joinToString(separator = RECORD_SEPARATOR.toString()) {
            it.key + UNIT_SEPARATOR + it.value
        }
    }
