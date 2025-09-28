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
 * Enum class representing the various fields in a Variable-Resolution Latent Friction Ridge Image record (latent prints).
 *
 * This enumeration extends the `FieldType` interface and represents fields related to latent image data
 * within an NIST format record. Each field type in the `LatentImageData` enum is uniquely identifiable
 * by its ID, code, description, and associated type class.
 *
 * Fields in this enum may either directly map to those in the `ImageType` enum or be uniquely defined within
 * this specific context of latent image data. The fields include predefined fields, reserved fields for future
 * definitions, and user-defined fields extending the existing schema.
 *
 * The `LatentImageData` enum contains constructors to initialize fields based on their parent enum field type
 * or with explicit ID, code, description, and class information.
 *
 * @property recordType Specifies the record type for all fields in this enum is "Variable-resolution latent friction ridge image (latent prints)".
 * @property id A unique identifier for the specific field type.
 * @property code Standardized code representing the field type.
 * @property description Description providing context or purpose of the field.
 * @property typeClass Class representing the data structure of the field, expected to implement the `Field` interface.
 */
@Suppress("MagicNumber")
enum class LatentImageFields : FieldType {
    LEN(ImageFields.LEN),
    IDC(ImageFields.IDC),
    IMP(ImageFields.IMP),
    SRC(4, "SRC", "SOURCE AGENCY / ORI", TextField::class),
    LCD(5, "LCD", "LATENT CAPTURE DATE", TextField::class),
    HLL(6, "HLL", "HORIZONTAL LINE LENGTH", TextField::class),
    VLL(7, "VLL", "VERTICAL LINE LENGTH", TextField::class),
    SLC(8, "SLC", "SCALE UNITS", TextField::class),
    THPS(9, "THPS", "TRANSMITTED HORIZONTAL PIXEL SCALE", TextField::class),
    TVPS(10, "TVPS", "TRANSMITTED VERTICAL PIXEL SCALE", TextField::class),
    CGA(11, "CGA", "COMPRESSION ALGORITHM", TextField::class),
    BPX(12, "BPX", "BITS PER PIXEL", TextField::class),
    FGP(13, "FGP", "FINGER / PALM POSITION", TextField::class),
    SPD(14, "SPD", "SEARCH POSITION DESCRIPTORS", TextField::class),
    PPC(15, "PPC", "PRINT POSITION COORDINATES", TextField::class),
    SHPS(16, "SHPS", "SCANNED HORIZONTAL PIXEL SCALE", TextField::class),
    SVPS(17, "SVPS", "SCANNED VERTICAL PIXEL SCALE", TextField::class),
    RSP(18, "RSP", "RULER OR SCALE PRESENCE", TextField::class),
    REM(19, "REM", "RESOLUTION METHOD", TextField::class),
    COM(20, "COM", "COMMENT", TextField::class),
    LQM(24, "LQM", "LATENT QUALITY METRIC", TextField::class),
    SUB(46, "SUB", "IMAGE SUBJECT CONDITION", TextField::class),
    CON(47, "CON", "CAPTURE ORGANIZATION NAME", TextField::class),
    FCT(901, "FCT", "FRICTION RIDGE CAPTURE TECHNOLOGY", TextField::class),
    ANN(902, "ANN", "ANNOTATION INFORMATION", TextField::class),
    DUI(903, "DUI", "DEVICE UNIQUE IDENTIFIER", TextField::class),
    MMS(904, "MMS", "MAKE/MODEL/SERIAL NUMBER", TextField::class),
    SAN(993, "SAN", "SOURCE AGENCY NAME", TextField::class),
    EFR(994, "EFR", "EXTERNAL FILE REFERENCE", TextField::class),
    ASC(995, "ASC", "ASSOCIATED CONTEXT", TextField::class),
    HAS(996, "HAS", "HASH", TextField::class),
    SOR(997, "SOR", "SOURCE REPRESENTATION", TextField::class),
    GEO(998, "GEO", "GEOGRAPHIC SAMPLE ACQUISITION LOCATION", TextField::class),
    DATA(ImageFields.DATA),
    ;

    override val recordType: String = "Variable-resolution latent friction ridge image (latent prints)"
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
