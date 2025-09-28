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

package eu.aagsolutions.img.nbis.model.enums.records

import eu.aagsolutions.img.nbis.model.fields.Field
import kotlin.reflect.KClass

/**
 * Represents the type of field in the NIST format.
 *
 * The `FieldType` interface defines a blueprint for representing metadata
 * and structural information related to a specific type of field in a
 * NIST format record. Each field type is uniquely identifiable by its
 * associated `recordType`, `id`, `name`, and `code`.
 *
 * @property recordType The type of the record to which the field belongs, represented as a string.
 * @property id A unique identifier associated with the field type.
 * @property name A human-readable name representing the field type.
 * @property code A code indicating the standardized value associated with the field type.
 * @property description A description of the field type, providing additional context or usage information.
 * @property typeClass The actual enum class associated with this field type. This must implement the `Field` interface.
 */
interface FieldType {
    val recordType: String
    val id: Int
    val name: String
    val code: String
    val description: String
    val typeClass: KClass<out Field<*>>
}
