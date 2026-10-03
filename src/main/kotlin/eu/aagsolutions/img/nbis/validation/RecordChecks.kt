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

import eu.aagsolutions.img.nbis.model.enums.records.FieldType
import eu.aagsolutions.img.nbis.model.enums.records.StandardNistValidatorError
import eu.aagsolutions.img.nbis.model.fields.ImageField
import eu.aagsolutions.img.nbis.model.fields.TextField
import eu.aagsolutions.img.nbis.model.records.BaseRecord
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

internal class RecordChecks(
    val record: BaseRecord,
) {
    val errors = mutableListOf<StandardNistValidatorError>()

    fun text(field: FieldType): String? = (record.fields[field.id] as? TextField)?.getData()

    fun check(
        error: StandardNistValidatorError,
        optional: Boolean = false,
        predicate: (String) -> Boolean,
    ) {
        val field = error.fieldTypeEnum
        if (optional && !record.fields.containsKey(field.id)) return
        val value = text(field)
        if (value == null || !predicate(value)) errors.add(error)
    }

    fun number(
        error: StandardNistValidatorError,
        range: IntRange,
        optional: Boolean = false,
    ) = check(error, optional) { it.matches(Regex("[0-9]+")) && it.toIntOrNull() in range }

    fun date(error: StandardNistValidatorError) = check(error) { validDate(it) }

    fun image(error: StandardNistValidatorError) {
        val data = record.fields[error.fieldTypeEnum.id] as? ImageField
        if (data == null || data.getData().isEmpty()) errors.add(error)
    }
}

internal fun validDate(value: String): Boolean {
    if (!value.matches(Regex("[0-9]{8}"))) return false
    return try {
        LocalDate.parse(value, DateTimeFormatter.BASIC_ISO_DATE)
        true
    } catch (_: DateTimeParseException) {
        false
    }
}
