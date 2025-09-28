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
 * Enum representing field types for high-resolution binary fingerprint images.
 *
 * HighResolutionBinaryFingerprintImage is an enumeration of specific field types,
 * each corresponding to a standardized structure and metadata for representing
 * aspects of high-resolution fingerprint images. These field types include
 * information about image length, impression type, finger position, scanning
 * resolution, and associated binary data, among others.
 *
 * This class is marked as deprecated as per its `recordType` description, emphasizing
 * that newer implementations or usage recommendations may exist.
 *
 * @property recordType The type of the record indicating it pertains to high-resolution binary fingerprint images.
 * @property id A unique identifier for the field type, derived from the parent type.
 * @property code A standardized code representing the field type, inherited from the parent type.
 * @property description A textual representation providing details and purpose of the field.
 * @property typeClass The class representing the actual field's data structure, implementing the `Field` interface.
 */
enum class HighResolutionBinaryFingerprintImageFields : FieldType {
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

    override val recordType: String = "High-resolution binary fingerprint image (deprecated)"
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
