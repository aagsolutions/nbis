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
 * Enum class representing low-resolution binary fingerprint image fields in a NIST record.
 *
 * This enum is used to define field types specific to low-resolution binary fingerprint images.
 * Each field corresponds to an image-related property, such as image length, impression type,
 * or data encoding. This class inherits common field metadata from the `FieldType` interface.
 *
 * This enumeration is considered deprecated due to its association with low-resolution fingerprint
 * image formats, which may no longer be supported or recommended for use in modern systems.
 *
 * @property recordType Specifies the type of record this field applies to.
 * @property id Unique identifier for this specific field type.
 * @property code Standardized code representing this field.
 * @property description Brief textual information about the field's purpose.
 * @property typeClass Defines the class of the field, complying with the `Field` interface.
 */
enum class LowResolutionBinaryFingerprintImageFields : FieldType {
    LEN(ImageFields.LEN),
    IDC(ImageFields.IDC),
    IMP(ImageFields.IMP),
    FGP(ImageFields.FGP),
    ISR(ImageFields.ISR),
    HLL(ImageFields.HLL),
    VLL(ImageFields.VLL),
    GCA(ImageFields.GCA),
    DATA(ImageFields.DATA),
    ;

    override val recordType: String = "Low-resolution binary fingerprint image (deprecated)"
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
}
