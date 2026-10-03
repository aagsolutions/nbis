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

package eu.aagsolutions.img.nbis.validation

import eu.aagsolutions.img.nbis.model.enums.Standard
import eu.aagsolutions.img.nbis.model.enums.records.StandardNistValidatorError
import eu.aagsolutions.img.nbis.model.enums.records.StandardNistValidatorError.STD_ERR_CNT_CONTENT_RT1
import eu.aagsolutions.img.nbis.model.enums.records.StandardNistValidatorError.STD_ERR_CNT_FORMAT_RT1
import eu.aagsolutions.img.nbis.model.enums.records.StandardNistValidatorError.STD_ERR_DAI_RT1
import eu.aagsolutions.img.nbis.model.enums.records.StandardNistValidatorError.STD_ERR_DAT_RT1
import eu.aagsolutions.img.nbis.model.enums.records.StandardNistValidatorError.STD_ERR_GMT_RT1
import eu.aagsolutions.img.nbis.model.enums.records.StandardNistValidatorError.STD_ERR_GNS_RT1
import eu.aagsolutions.img.nbis.model.enums.records.StandardNistValidatorError.STD_ERR_LEN
import eu.aagsolutions.img.nbis.model.enums.records.StandardNistValidatorError.STD_ERR_NSR_WITH_RT4_RT1
import eu.aagsolutions.img.nbis.model.enums.records.StandardNistValidatorError.STD_ERR_NTR_WITH_RT4_RT1
import eu.aagsolutions.img.nbis.model.enums.records.StandardNistValidatorError.STD_ERR_ORI_RT1
import eu.aagsolutions.img.nbis.model.enums.records.StandardNistValidatorError.STD_ERR_PRY_RT1
import eu.aagsolutions.img.nbis.model.enums.records.StandardNistValidatorError.STD_ERR_TCN_RT1
import eu.aagsolutions.img.nbis.model.enums.records.StandardNistValidatorError.STD_ERR_TOT_RT1
import eu.aagsolutions.img.nbis.model.enums.records.StandardNistValidatorError.STD_ERR_VER_RT1
import eu.aagsolutions.img.nbis.model.records.TransactionInformationRecord
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.format.ResolverStyle

/** Basic Type 1 field validation. Does not verify CNT against other records in a file. */
@Suppress("MagicNumber")
class TransactionInformationRecordValidator {
    fun validate(record: TransactionInformationRecord): List<StandardNistValidatorError> {
        val checks = RecordChecks(record)
        with(checks) {
            check(STD_ERR_LEN) { it.matches(Regex("[0-9]{1,10}")) && it.toLong() > 0 }
            check(STD_ERR_VER_RT1) { Standard.findByCode(it) != null }
            check(STD_ERR_CNT_FORMAT_RT1) { it.isNotEmpty() }
            check(STD_ERR_CNT_CONTENT_RT1) { validContent(it) }
            check(STD_ERR_TOT_RT1) { it.matches(Regex("[A-Z0-9]{1,16}")) }
            date(STD_ERR_DAT_RT1)
            for (error in listOf(STD_ERR_DAI_RT1, STD_ERR_ORI_RT1, STD_ERR_TCN_RT1)) {
                check(error) { it.isNotBlank() && it.all { char -> char in ' '..'~' } }
            }
            number(STD_ERR_PRY_RT1, 0..2, optional = true)
            check(STD_ERR_NSR_WITH_RT4_RT1) { it.matches(Regex("[0-9]{2}\\.[0-9]{2}")) }
            check(STD_ERR_NTR_WITH_RT4_RT1) { it.matches(Regex("[0-9]{2}\\.[0-9]{2}")) }
            check(STD_ERR_GNS_RT1, optional = true) { it in setOf("ISO", "GENC") }
            check(STD_ERR_GMT_RT1, optional = true) { validGmt(it) }
        }
        return checks.errors.toList()
    }

    private fun validContent(value: String): Boolean {
        val rows = value.split('\u001e').map { it.split('\u001f') }
        if (rows.any { row -> row.size != 2 || row.any { !it.matches(Regex("[0-9]+")) } }) return false
        return rows.first()[0] == "1" &&
            rows.first()[1].toIntOrNull() == rows.size - 1 &&
            rows.drop(1).all { it[0].toIntOrNull() in 2..99 && it[1].toIntOrNull() in 0..99 }
    }

    private fun validGmt(value: String): Boolean {
        if (!value.matches(Regex("[0-9]{14}Z"))) return false
        return try {
            LocalDateTime.parse(value.dropLast(1), DateTimeFormatter.ofPattern("uuuuMMddHHmmss").withResolverStyle(ResolverStyle.STRICT))
            true
        } catch (_: DateTimeParseException) {
            false
        }
    }
}
