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
 * Represents the type of signature image field within an NIST record.
 *
 * This enum class defines various types of signature image fields, each identified
 * by a unique identifier, code, description, and an associated data type. It
 * extends the `FieldType` interface to integrate with the standardized field
 * metadata structure in the NIST format.
 *
 * The available signature image field types include:
 * - LEN: Length of the data field.
 * - IDC: Image Designation Character.
 * - SIG: Signature type.
 * - SRT: Signature representation type.
 * - ISR: Image scanning resolution of the signature image.
 * - HLL: Horizontal line length of the signature image.
 * - VLL: Vertical line length of the signature image.
 * - DATA: Actual signature image data.
 *
 * @property recordType The type of record this field belongs to, always set to "Signature image".
 * @property id A unique identifier assigned to the field type.
 * @property code The standardized code associated with the field type.
 * @property description Provides a description of the field type.
 * @property typeClass The class that represents the data structure for this field type.
 */
@Suppress("MagicNumber")
enum class SignatureImageFields : FieldType {
    LEN(ImageFields.LEN),
    IDC(ImageFields.IDC),
    SIG(3, "SIG", "Signature type", TextField::class),
    SRT(4, "SRT", "Signature representation type", TextField::class),
    ISR(5, "ISR", "Image scanning resolution", TextField::class),
    HLL(6, "HLL", "Horizontal line length", TextField::class),
    VLL(7, "VLL", "Vertical line length", TextField::class),
    DATA(ImageFields.DATA),
    ;

    override val recordType: String = "Signature image"
    override val id: Int
    override val code: String
    override val description: String
    override val typeClass: KClass<out Field<*>>

    constructor(parentEnum: FieldType) {
        this.id = parentEnum.id
        this.code = parentEnum.code
        this.description = parentEnum.description
        this.typeClass = parentEnum.typeClass
    }

    constructor(
        id: Int,
        code: String,
        description: String,
        typeClass: KClass<out Field<*>>,
    ) {
        this.id = id
        this.code = code
        this.description = description
        this.typeClass = typeClass
    }
}
