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
 * Represents various field types within a variable-resolution fingerprint image record.
 *
 * The `VariableResolutionFingerprintImage` enum categorizes specific data fields and their
 * associated metadata within a variable-resolution fingerprint image context. Each enum
 * constant describes a unique field type, including its identifier, standardized code,
 * descriptive name, and associated class. This allows for systematic representation and
 * handling of crucial fingerprint metadata as part of a NIST-based format.
 *
 * This enum also provides constructors for creating linked or standalone field definitions.
 * It leverages the `FieldType` interface to define the expected behavior and properties
 * of field types.
 *
 * @property recordType Specifies the record to which the field belongs, set as "Variable-resolution fingerprint image".
 * @property id A unique identifier representing the specific field type.
 * @property code A standardized code associated with the field type for interoperability.
 * @property description A brief description detailing the purpose or nature of the field.
 * @property typeClass The class associated with the field, representing its expected data structure.
 */
@Suppress("MagicNumber")
enum class VariableResolutionFingerprintImageFields : FieldType {
    LEN(ImageFields.LEN),
    IDC(ImageFields.IDC),
    IMP(3, "IMP", "Impression Type", TextField::class),
    SRC(4, "SRC", "Source Agency", TextField::class),
    FCD(5, "FCD", "Capture Date", TextField::class),
    HLL(6, "HLL", "Horizontal line length", TextField::class),
    VLL(7, "VLL", "Vertical line length", TextField::class),
    SLC(8, "SLC", "Scale Units", TextField::class),
    THPS(9, "THPS", "Transmitted Horizontal Pixel Scale", TextField::class),
    TVPS(10, "TVPS", "Transmitted Vertical Pixel Scale", TextField::class),
    CGA(11, "CGA", "Compression Algorithm", TextField::class),
    BPX(12, "BPX", "Bits Per Pixel", TextField::class),
    FGP(13, "FGP", "Finger Position", TextField::class),
    PPD(14, "PPD", "Print Position Descriptors", TextField::class),
    PPC(15, "PPC", "Print Position Coordinates", TextField::class),
    SHPS(16, "SHPS", "Scanned Horizontal Pixel Scale", TextField::class),
    SVPS(17, "SVPS", "Scanned Vertical Pixel Scale", TextField::class),
    AMP(18, "AMP", "Amputated Or Bandaged", TextField::class),
    COM(20, "COM", "Comment", TextField::class),
    SEG(21, "SEG", "Fingerprint Segmentation Position", TextField::class),
    NQM(22, "NQM", "NIST Quality Metric", TextField::class),
    SQM(23, "SQM", "Segmentation Quality Metric", TextField::class),
    FQM(24, "FQM", "Fingerprint Quality Metric", TextField::class),
    ASEG(25, "ASEG", "Alternate Finger Segment Position", TextField::class),
    SCF(26, "SCF", "Simultaneous Capture", TextField::class),
    SIF(27, "SIF", "Stitched Image Flag", TextField::class),
    DMM(30, "DMM", "Device Monitoring Mode", TextField::class),
    FAP(31, "FAP", "Friction Ridge Segment Position", TextField::class),
    SUB(46, "SUB", "Image Subject Condition", TextField::class),
    CON(47, "CON", "Capture Organization Name", TextField::class),
    FCT(901, "FCT", "Friction Ridge Capture Technology", TextField::class),
    ANN(902, "ANN", "Annotation Information", TextField::class),
    DUI(903, "DUI", "Device Unique Identifier", TextField::class),
    MMS(904, "MMS", "Make Model Serial Number", TextField::class),
    SAN(993, "SAN", "Source Agency Name", TextField::class),
    EFR(994, "EFR", "External File Reference", TextField::class),
    ASC(995, "ASC", "Associated Context", TextField::class),
    HAS(996, "HAS", "hash", TextField::class),
    SOR(997, "SOR", "Source Representation", TextField::class),
    GEO(998, "GEO", "Geographic Sample Acquisition Location", TextField::class),
    DATA(ImageFields.DATA),
    ;

    override val recordType: String = "Variable-resolution fingerprint image"
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
