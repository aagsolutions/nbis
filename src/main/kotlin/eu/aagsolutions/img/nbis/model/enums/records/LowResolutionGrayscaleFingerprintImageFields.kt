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
 * Enum class representing fields specific to low-resolution grayscale fingerprint images in the NIST format.
 *
 * This enumeration defines a set of standard fields for handling low-resolution grayscale fingerprint image data.
 * Each enum constant corresponds to a specific field type, inheriting its attributes from the `ImageType` enum.
 * The fields provide detailed metadata about the image, such as impression type, finger position, scanning resolution,
 * dimensions, and compression algorithm, along with the actual image data.
 *
 * Fields:
 * - `LEN`: Represents the length of the image.
 * - `IDC`: An identifier for the image record.
 * - `IMP`: The impression type of the fingerprint.
 * - `FGP`: The finger position on the hand.
 * - `ISR`: The image scanning resolution.
 * - `HLL`: The horizontal line length of the image.
 * - `VLL`: The vertical line length of the image.
 * - `GCA`: The grayscale compression algorithm used for the image.
 * - `DATA`: The raw image data.
 *
 * Each enum value implements the `FieldType` interface, providing necessary meta-information such as the record type,
 * field ID, field code, description, and data class representation.
 *
 * @constructor Creates a new instance of `LowResolutionGrayscaleFingerprintImage` using field properties inherited
 * from a parent `FieldType` instance.
 *
 * @property recordType The type of the record to which this field belongs, always set to "RT3" for these fields.
 * @property id A unique identifier for the field type.
 * @property code A standardized unique code representing the field type.
 * @property description A textual description of the field.
 * @property typeClass The data type or class associated with the field, which implements the `Field` interface.
 */
enum class LowResolutionGrayscaleFingerprintImageFields : FieldType {
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

    override val recordType: String = "Low-resolution grayscale fingerprint image"
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
