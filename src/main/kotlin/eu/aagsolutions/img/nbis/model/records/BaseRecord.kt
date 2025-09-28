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

package eu.aagsolutions.img.nbis.model.records

import eu.aagsolutions.img.nbis.exceptions.NistException
import eu.aagsolutions.img.nbis.model.enums.records.FieldType
import eu.aagsolutions.img.nbis.model.fields.Field
import eu.aagsolutions.img.nbis.model.fields.ImageField
import eu.aagsolutions.img.nbis.model.fields.TextField

/**
 * Represents an abstract base class for NIST records with fields.
 *
 * This class provides a structure for managing records that contain a variety
 * of fields, each associated with a unique identifier and a specific type. It
 * includes mechanisms to retrieve information about the fields, perform type-safe
 * operations based on field types, and ensure immutability by maintaining internal
 * copies of data.
 *
 * @constructor Protected constructor to initialize a record with a unique identifier,
 *              a name, and a map of fields.
 * @param recordId An integer representing the unique identifier of the record.
 * @param recordName A string representing the name of the record.
 * @param fields A map mapping field identifiers to field objects.
 */
abstract class BaseRecord protected constructor(
    val recordId: Int,
    val recordName: String,
    fields: Map<Int, Field<*>>,
) {
    val fields: Map<Int, Field<*>> = unmodifiableMapOfCopies(fields)

    private fun unmodifiableMapOfCopies(fields: Map<Int, Field<*>>): Map<Int, Field<*>> =
        fields.entries
            .associate { (key, value) ->
                key to value.deepCopy()
            }.toSortedMap()

    protected abstract fun getFieldTypeValues(): Set<FieldType>

    override fun toString(): String = "${this::class.java.name}(id=$recordId, name=$recordName, fields=$fields)"

    fun getFieldText(field: FieldType): String? {
        val fieldData = fields[field.id] ?: return null
        if (fieldData is TextField) {
            return fieldData.getData()
        }
        throw NistException("Field ${field.id} isn't in text format")
    }

    fun getFieldImage(field: FieldType): ByteArray? {
        val fieldData = fields[field.id] ?: return null
        if (fieldData is ImageField) {
            return fieldData.getData()
        }
        throw NistException("Field ${field.id} isn't in image format")
    }

    fun getFieldData(id: Int): Field<*>? = fields[id]

    fun getFieldAsInt(field: FieldType): Int? {
        val fieldData = getFieldData(field.id) ?: return null
        if (fieldData is TextField) {
            return fieldData.getData().toIntOrNull()
        }
        throw NistException("Field ${field.id} isn't in text format")
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is BaseRecord) return false

        if (recordId != other.recordId) return false
        if (recordName != other.recordName) return false
        if (fields != other.fields) return false

        return true
    }

    override fun hashCode(): Int {
        var result = recordId
        result = 31 * result + recordName.hashCode()
        result = 31 * result + fields.hashCode()
        return result
    }
}
