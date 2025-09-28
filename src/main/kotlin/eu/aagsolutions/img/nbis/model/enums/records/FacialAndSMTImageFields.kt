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
 * Enum class representing fields associated with Facial and SMT (Scar, Mark and Tattoo) images.
 *
 * This enumeration provides a comprehensive list of standardized field definitions
 * for encoding and managing data in Type-10 records of the NIST format. Every enum
 * constant corresponds to a specific metadata or attribute, identified through unique
 * identifiers, codes and descriptions. These fields enable robust representation of
 * facial and SMT image data for biometric, forensics or identification purposes.
 *
 * Each enum constant encapsulates the following:
 * - A unique field identifier.
 * - A standardized alphanumeric code.
 * - A description indicating the type or purpose of the field.
 * - The associated data type for the field.
 *
 * Constructors enable initialization either through inheritance of attributes
 * from another `FieldType` instance or explicit specification of attributes.
 */
@Suppress("MagicNumber")
enum class FacialAndSMTImageFields : FieldType {
    LEN(ImageFields.LEN),
    IDC(ImageFields.IDC),
    IMT(3, "IMT", "Image Type", TextField::class),
    SRC(4, "SRC", "Source Agency / ORI", TextField::class),
    PHD(5, "PHD", "Photo Date", TextField::class),
    HLL(6, "HLL", "Horizontal line length", TextField::class),
    VLL(7, "VLL", "Vertical line length", TextField::class),
    SLC(8, "SLC", "Scale units", TextField::class),
    HPS_LEGACY(9, "HPS", "Horizontal pixel scale", TextField::class),
    THPS(9, "THPS", "Transmitted horizontal pixel scale", TextField::class),
    VPS_LEGACY(10, "VPS", "Vertical pixel scale", TextField::class),
    TVPS(10, "TVPS", "Transmitted vertical pixel scale", TextField::class),
    CGA(11, "CGA", "Compression algorithm", TextField::class),
    CSP(12, "CSP", "Color space", TextField::class),
    SAP(13, "SAP", "Subject acquisition profile", TextField::class),
    FIP(14, "FIP", "Facial image bounding box coordinates in full image", TextField::class), // Not in 2007
    FPFI(15, "FPFI", "Face image path coordinates in full image", TextField::class), // Not in 2007
    SHPS(16, "SHPS", "Scan horizontal pixel scale", TextField::class),
    SVPS(17, "SVPS", "Scan vertical pixel scale", TextField::class),
    DIST(18, "DIST", "Distortion", TextField::class), // Not in 2007
    LAF(19, "LAF", "Lighting artifacts", TextField::class), // Not in 2007
    POS(20, "POS", "Subject pose", TextField::class),
    POA(21, "POA", "Pose offset angle", TextField::class),
    PXS_LEGACY(22, "PXS", "Photo designation", TextField::class), // Only in 2007
    PAS(23, "PAS", "Photo acquisition source", TextField::class),
    SQS(24, "SQS", "Subject quality score", TextField::class),
    SPA(25, "SPA", "Subject pose angles", TextField::class),
    SXS(26, "SXS", "Subject facial description", TextField::class),
    SEC(27, "SEC", "Subject eye color", TextField::class),
    SHC(28, "SHC", "Subject air color", TextField::class),
    SFP_LEGACY(29, "SFP", "Facial feature points", TextField::class),
    FFP(29, "FFP", "2D facial feature points", TextField::class), // not in 2007
    DMM(30, "DMM", "Device monitoring mode", TextField::class), // not in 2007
    TMC(31, "TMC", "Tiered markup collection", TextField::class), // not in 2007
    THREEDF(32, "3DF", "3D facial feature points", TextField::class), // not in 2007
    FEC(33, "FEC", "Feature contours", TextField::class), // not in 2007
    ICDR(34, "ICDR", "Image capture date range estimate", TextField::class), // not in 2007
    COM(38, "COM", "Comment", TextField::class),
    T10(39, "T10", "Type-10 reference number", TextField::class),
    SMT(40, "SMT", "NCIC Designation Code", TextField::class),
    SMS(41, "SMS", "Scar / Mark / Tattoo size", TextField::class),
    SMD(42, "SMD", "SMT Descriptors", TextField::class),
    COL(43, "COL", "Colors Present", TextField::class),
    ITX(44, "ITX", "Image transformation", TextField::class), // Not in 2007
    OCC(45, "OCC", "Occlusions", TextField::class), // Not in 2007
    SUB(46, "SUB", "Image Subject Condition", TextField::class), // Not in 2007
    CON(47, "CON", "Capture organization name", TextField::class), // Not in 2007
    PID(48, "PID", "Suspected patterned injury detail", TextField::class), // Not in 2007
    CID(49, "CID", "Cheiloscopic Image Description", TextField::class), // Not in 2007
    VID(50, "VID", "Dental visual image Data Information", TextField::class), // Not in 2007
    RSP(51, "RSP", "Ruler or Scale présence", TextField::class), // Not in 2007
    ANN(902, "ANN", "Annotation information", TextField::class), // Not in 2007
    DUI(903, "DUI", "Device Unique identifier", TextField::class), // Not in 2007
    MMS(904, "MMS", "Make / Model / Serial number", TextField::class), // Not in 2007
    T2C(992, "T2C", "Type-2 record cross reference", TextField::class), // Not in 2007
    SAN(993, "SAN", "Source Agence name", TextField::class), // Not in 2007
    EFR(994, "EFR", "External file reference", TextField::class), // Not in 2007
    ASC(995, "ASC", "Associated context", TextField::class), // Not in 2007
    HAS(996, "HAS", "HASH", TextField::class), // Not in 2007
    SOR(997, "SOR", "Source Representation", TextField::class), // Not in 2007
    GEO(998, "GEO", "Geographic Sample Acquisition Location", TextField::class), // Not in 2007
    DATA(ImageFields.DATA),
    ;

    override val recordType: String = "Facial and SMT (Scar, Mark, and Tattoo) image"
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
