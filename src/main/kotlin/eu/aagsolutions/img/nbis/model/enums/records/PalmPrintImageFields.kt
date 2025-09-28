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
 * Enum class representing various types of palm print image metadata and attributes.
 *
 * The `PalmPrintImage` enum is specifically tailored to handle the unique information
 * related to palm print images within a NIST record. Each entry in the enum corresponds
 * to a distinct field, defining its attributes such as unique identifier, standardized code,
 * description, and associated data type. This enum implements the `FieldType` interface,
 * ensuring alignment with the expected behavior and metadata structure for field types.
 *
 * Constructors:
 * - One constructor allows the initialization of a `PalmPrintImage` instance
 *   based on another `FieldType` instance, inheriting its attributes.
 * - Another constructor provides explicit declarations for field attributes
 *   such as `id`, `code`, `description`, and `typeClass`.
 *
 * @property recordType The type of the record this field is associated with, always "RT15".
 * @property id A unique identifier for the field type.
 * @property code A standardized code for the field type.
 * @property description A brief description of the field's purpose within the NIST format.
 * @property typeClass The class representing the field's data structure, typically implementing the `Field` interface.
 */
@Suppress("MagicNumber")
enum class PalmPrintImageFields : FieldType {
    LEN(ImageFields.LEN),
    IDC(ImageFields.IDC),
    IMP(3, "IMP", "impressionType", TextField::class),
    SRC(4, "SRC", "sourceAgency", TextField::class),
    PCD(5, "PCD", "captureDate", TextField::class),
    HLL(6, "HLL", "horizontalLineLength", TextField::class),
    VLL(7, "VLL", "verticalLineLength", TextField::class),
    SLC(8, "SLC", "scaleUnits", TextField::class),
    THPS(9, "THPS", "transmittedHorizontalPixelScale", TextField::class),
    TVPS(10, "TVPS", "transmittedVerticalPixelScale", TextField::class),
    CGA(11, "CGA", "compressionAlgorithm", TextField::class),
    BPX(12, "BPX", "bitsPerPixel", TextField::class),
    FGP(13, "FGP", "palmPosition", TextField::class),
    SHPS(16, "SHPS", "scannedHorizontalPixelScale", TextField::class),
    SVPS(17, "SVPS", "scannedVerticalPixelScale", TextField::class),
    AMP(18, "AMP", "amputatedOrBandaged", TextField::class),
    COM(20, "COM", "comment", TextField::class),
    SEG(21, "SEG", "palmSegmentationPosition", TextField::class),
    PQM(24, "PQM", "palmQualityMetric", TextField::class),
    SUB(46, "SUB", "imageSubjectCondition", TextField::class),
    CON(47, "CON", "captureOrganizationName", TextField::class),
    FCT(901, "FCT", "frictionRidgeCaptureTechnology", TextField::class),
    ANN(902, "ANN", "annotationInformation", TextField::class),
    DUI(903, "DUI", "deviceUniqueIdentifier", TextField::class),
    MMS(904, "MMS", "makeModelSerialNumber", TextField::class),
    SAN(993, "SAN", "sourceAgencyName", TextField::class),
    EFR(994, "EFR", "externalFileReference", TextField::class),
    ASC(995, "ASC", "associatedContext", TextField::class),
    HAS(996, "HAS", "hash", TextField::class),
    SOR(997, "SOR", "sourceRepresentation", TextField::class),
    GEO(998, "GEO", "geographicSampleAcquisitionLocation", TextField::class),
    DATA(ImageFields.DATA),
    ;

    override val recordType: String = "RT15"
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
