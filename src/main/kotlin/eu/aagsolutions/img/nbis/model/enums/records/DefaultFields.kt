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
import eu.aagsolutions.img.nbis.model.fields.TextField
import kotlin.reflect.KClass

/**
 * Represents a set of default fields that are part of the NIST standard record structure.
 *
 * Each constant in this enum maps to a specific field type, uniquely identified by its ID, code,
 * description, and the associated class implementing the `Field` interface. These fields are used
 * to standardize the representation of logical records within a NIST file.
 *
 * @property id A unique integer identifier associated with the field.
 * @property code A string code representing the standardized name of the field.
 * @property description A human-readable description providing additional context about the field.
 * @property typeClass The class type of the field, which implements the `Field` interface.
 */
enum class DefaultFields(
    override val id: Int,
    override val code: String,
    override val description: String,
    override val typeClass: KClass<out Field<*>>,
) : FieldType {
    LEN(1, "LEN", "Logical record length", TextField::class),
    IDC(2, "IDC", "Image designation character", TextField::class),
    ;

    override val recordType: String = "RTx"
}
