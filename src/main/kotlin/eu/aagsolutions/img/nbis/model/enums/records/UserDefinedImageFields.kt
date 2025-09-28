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
 * Represents a set of user-defined image fields in the context of a NIST format record.
 *
 * The `UserDefinedImage` enum supports custom definitions of image-related metadata.
 * Each enumerator provides a unique set of attributes such as identifiers, codes,
 * descriptions, and associated field classes, ensuring compatibility with NIST standards.
 * Additionally, the `UserDefinedImage` enum implements the `FieldType` interface, offering
 * metadata and structural definitions for use in specific contexts.
 *
 * The following types of fields are defined in the `UserDefinedImage` enum:
 * - Predefined image fields inherited from `ImageType`.
 * - Custom-defined fields, such as "Image Type", "Image Description", "Pattern Classification",
 *   and resolution-related fields.
 *
 * Properties:
 * - recordType: The type of record this field belongs to, set to "RT7".
 * - id: A unique numerical identifier for each field.
 * - code: A standardized code used for each field.
 * - description: A textual description providing context for the field.
 * - typeClass: The associated class representing the type of data the field supports.
 *
 * Constructors:
 * - Constructor accepting a parent `FieldType`. Inherits metadata properties
 *   from the parent enumeration.
 * - Constructor accepting explicit field properties such as `id`, `code`,
 *   `description`, and `typeClass`.
 */
@Suppress("MagicNumber")
enum class UserDefinedImageFields : FieldType {
    LEN(ImageFields.LEN),
    IDC(ImageFields.IDC),
    IMT(3, "IMT", "Image Type", TextField::class),
    IMD(4, "IMD", "Image Description", TextField::class),
    PCN(5, "PCN", "Pattern Classification", TextField::class),
    PCN2(6, "PCN2", "Second Pattern Classification", TextField::class),
    PCN3(7, "PCN3", "Third Pattern Classification", TextField::class),
    PCN4(8, "PCN4", "Fourth Pattern Classification", TextField::class),
    PCN5(9, "PCN5", "Fifth Pattern Classification", TextField::class),
    IMR(10, "IMR", "Image Capture Resolution", TextField::class),
    IMR2(102, "IMR2", "Image Capture Resolution", TextField::class),
    IMR3(103, "IMR3", "Image Capture Resolution", TextField::class),
    IMR4(104, "IMR4", "Image Capture Resolution", TextField::class),
    IMR5(105, "IMR5", "Image Capture Resolution", TextField::class),
    IMR6(106, "IMR6", "Image Capture Resolution", TextField::class),
    IMR7(107, "IMR7", "Image Capture Resolution", TextField::class),
    IMR8(108, "IMR8", "Image Capture Resolution", TextField::class),
    IMR9(109, "IMR9", "Image Capture Resolution", TextField::class),
    IMR10(110, "IMR10", "Image Capture Resolution", TextField::class),
    IMR11(111, "IMR11", "Image Capture Resolution", TextField::class),
    HLL(11, "HLL", "horizontal Line Length", TextField::class),
    VLL(12, "VLL", "vertical Line Length", TextField::class),
    GCA(13, "GCA", "Grayscale Compression Algorithm", TextField::class),
    DATA(ImageFields.DATA),
    ;

    override val recordType: String = "User-defined image"
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
