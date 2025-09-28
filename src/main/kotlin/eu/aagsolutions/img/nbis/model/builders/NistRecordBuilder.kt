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

import eu.aagsolutions.img.nbis.calculators.LogicalRecordLengthCalculator
import eu.aagsolutions.img.nbis.model.fields.Field
import eu.aagsolutions.img.nbis.model.records.BaseRecord

/**
 * Abstract builder class for creating and modifying NIST records.
 *
 * This builder provides a base implementation for constructing different types of NIST records,
 * managing their fields and ensuring proper record construction.
 *
 * @param T The type of record being built must extend BaseRecord
 * @param B The concrete builder type for method chaining must extend NistRecordBuilder
 * @param id Identifier of the record type
 * @param label A descriptive label for the record type
 * @param calculator Calculator used to determine the logical record length
 */
@Suppress("UNCHECKED_CAST")
abstract class NistRecordBuilder<T : BaseRecord, B : NistRecordBuilder<T, B>>(
    val id: Int,
    val label: String,
    val calculator: LogicalRecordLengthCalculator,
) {
    val fields: MutableMap<Int, Field<*>> = mutableMapOf()

    /**
     * Creates a new builder instance initialized with fields from an existing record.
     *
     * @param record The existing record to copy fields from
     * @return Builder instance with copied fields
     */
    fun fromRecord(record: T): B {
        copyFieldsFrom(record)
        return this as B
    }

    /**
     * Adds or updates a field in the record being built.
     *
     * @param fieldId The identifier for the field
     * @param field The field value to set
     * @return Builder instance for method chaining
     */
    fun withField(
        fieldId: Int,
        field: Field<*>,
    ): B {
        this.fields[fieldId] = field
        return this as B
    }

    /**
     * Removes a field from the record being built.
     *
     * @param fieldId The identifier of the field to remove
     * @return Builder instance for method chaining
     */
    fun removeField(fieldId: Int): B {
        this.fields.remove(fieldId)
        return this as B
    }

    private fun copyFieldsFrom(record: T) {
        this.fields.clear()
        record.fields.forEach { (fieldId, field) ->
            this.fields[fieldId] = field.deepCopy()
        }
    }

    abstract fun build(): T

    companion object {
        /** Field identifier for the record length field, always set to 1 */
        const val LENGTH_FIELD_ID = 1
    }
}
