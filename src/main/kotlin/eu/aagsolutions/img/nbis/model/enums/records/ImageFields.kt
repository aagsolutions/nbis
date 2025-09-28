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
import eu.aagsolutions.img.nbis.model.fields.ImageField
import eu.aagsolutions.img.nbis.model.fields.TextField
import kotlin.reflect.KClass

/**
 * Enum class representing various types of image-related fields in a NIST format record.
 *
 * The `ImageType` enum provides a standardized representation for specific image metadata
 * and properties, including their identifiers, codes, descriptions, and associated field classes.
 * Each enum constant corresponds to a unique image field type, describing its structure and
 * purpose within the record. Additionally, it implements the `FieldType` interface to align
 * with the expected behavior and properties of field types.
 *
 * @property recordType Indicates the type of record this field belongs to, defaulting to "RTx".
 * @property id The unique identifier for the field type.
 * @property code A standardized code associated with the field type.
 * @property description Brief textual information explaining the field type.
 * @property typeClass The class implementing the `Field` interface that represents the field's data structure.
 */
@Suppress("MagicNumber")
enum class ImageFields : FieldType {
    LEN(DefaultFields.LEN),
    IDC(DefaultFields.IDC),
    IMP(3, "IMP", "Impression type", TextField::class),
    FGP(4, "FGP", "Finger position", TextField::class),
    ISR(5, "ISR", "Image scanning resolution", TextField::class),
    HLL(6, "HLL", "Horizontal line length", TextField::class),
    VLL(7, "VLL", "Vertical line length", TextField::class),
    GCA(8, "GCA", "Greyscale Compression Algorithm", TextField::class),
    DATA(999, "DATA", "Image data", ImageField::class),
    ;

    override val recordType: String = "RTx"
    override val id: Int
    override val code: String
    override val description: String
    override val typeClass: KClass<out Field<*>>

    constructor(parentEnum: FieldType) : this(
        parentEnum.id,
        parentEnum.code,
        parentEnum.description,
        parentEnum.typeClass,
    )

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
