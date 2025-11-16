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

package eu.aagsolutions.img.nbis.validation.records

import eu.aagsolutions.img.nbis.calculators.LogicalRecordLengthCalculator
import eu.aagsolutions.img.nbis.model.enums.RecordType
import eu.aagsolutions.img.nbis.model.enums.records.DefaultFields
import eu.aagsolutions.img.nbis.model.fields.Field
import eu.aagsolutions.img.nbis.model.fields.TextField
import eu.aagsolutions.img.nbis.validation.ValidationError
import eu.aagsolutions.img.nbis.validation.ValidationErrors

/**
 * Base validator for NIST records.
 */
abstract class RecordValidator(
    open val errors: MutableList<ValidationError>,
    val fields: Map<Int, Field<*>>,
    val recordType: RecordType,
    val calculator: LogicalRecordLengthCalculator,
) {
    /**
     * Validates the LEN field of the record (1.001).
     *
     * @return `this` validator instance for method chaining
     */
    protected fun validateLengthField() {
        val len = calculator.calculate(recordType.id, fields) as TextField
        if (len.getData().toInt() != (fields[DefaultFields.LEN.id] as TextField).getData().toInt()) {
            errors.add(
                ValidationError(
                    recordType,
                    DefaultFields.LEN,
                    ValidationErrors.STD_ERR_INVALID_LEN_FIELD.message,
                    len.getData(),
                ),
            )
        }
    }

    /**
     * Validates the record.
     */
    abstract fun validate()
}
