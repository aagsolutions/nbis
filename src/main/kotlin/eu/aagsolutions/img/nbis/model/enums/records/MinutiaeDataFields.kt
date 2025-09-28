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
 * Represents an enum class containing minutiae data types categorized by specific groups.
 * This enum defines various fields used in different feature sets associated with biometric data,
 * particularly in sectors like fingerprint identification and analysis systems.
 *
 * Each enum constant represents a specific field type used in minutiae or fingerprint-related data
 * processing. These constants can belong to legacy fields, feature sets from various organizations
 * (e.g., FBI IAFIS, Cogent, Motorola, etc.), or other advanced specifications such as EFS (Extended Features Sets).
 *
 * These fields aid in structuring, identifying, and processing biometric data according to
 * international standards and practices related to fingerprint analysis or machine-encoded data.
 *
 * Enum values define:
 * - Unique ID (optional)
 * - Field short code
 * - Field description
 * - Associated data type
 */
@Suppress("MagicNumber")
enum class MinutiaeDataFields : FieldType {
    LEN(ImageFields.LEN),
    IDC(ImageFields.IDC),
    IMP(3, "IMP", "Impression Type", TextField::class),
    FMT(4, "FMT", "Minutiae format", TextField::class),
    OFR_LEGACY(5, "OFR", "Originating fingerprint reading system", TextField::class),
    FGP_LEGACY(6, "FGP", "Finger position", TextField::class),
    FPC_LEGACY(7, "FPC", "Finger pattern classification", TextField::class),
    CRP_LEGACY(8, "CRP", "Core Position", TextField::class),
    DLT_LEGACY(9, "DLT", "Delta position", TextField::class),
    MIN_LEGACY(10, "MIN", "Number of Minutiae", TextField::class),
    RDG_LEGACY(11, "RDG", "Minutiae ridge count indicator", TextField::class),
    MRC_LEGACY(12, "MRC", "Minutiae and ridge count data", TextField::class),
    CBI(126, "CBI", "M1 CBEFF INFORMATION", TextField::class),
    CEI(127, "CEI", "M1 CAPTURE EQUIPMENT ID", TextField::class),
    HLL(128, "HLL", "M1 HORIZONTAL LINE LENGTH", TextField::class),
    VLL(129, "VLL", "M1 VERTICAL LINE LENGTH", TextField::class),
    SLC(130, "SLC", "M1 SCALE UNITS", TextField::class),
    THPS(131, "THPS", "M1 TRANSMITTED HORIZONTAL PIXEL SCALE", TextField::class),
    TVPS(132, "TVPS", "M1 TRANSMITTED VERTICAL PIXEL SCALE", TextField::class),
    FVW(133, "FVW", "M1 FINGER VIEW", TextField::class),
    FGP(134, "FGP", "M1 FRICTION RIDGE GENERALIZED POSITION", TextField::class),
    FQD(135, "FQD", "M1 FRICTION RIDGE QUALITY DATA", TextField::class),
    NOM(136, "NOM", "M1 NUMBER OF MINUTIAE", TextField::class),
    FMD(137, "FMD", "M1 FINGER MINUTIAE DATA", TextField::class),
    RCI(138, "RCI", "M1 RIDGE COUNT INFORMATION", TextField::class),
    CIN(139, "CIN", "M1 CORE INFORMATION", TextField::class),
    DIN(140, "DIN", "M1 DELTA INFORMATION", TextField::class),
    ADA(141, "ADA", "M1 ADDITIONAL DELTA ANGLES", TextField::class),
    OOD(176, "OOD", "OTHER FEATURE SETS - OWNER OR DEVELOPER", TextField::class),
    PAG(177, "PAG", "OTHER FEATURE SETS – PROCESSING ALGORITHM", TextField::class),
    SOD(178, "SOD", "OTHER FEATURE SETS - SYSTEM OR DEVICE", TextField::class),
    DTX(179, "DTX", "OTHER FEATURE SETS – CONTACT INFORMATION", TextField::class),
    ROI(300, "ROI", "EFS REGION OF INTEREST", TextField::class),
    ORT(301, "ORT", "EFS ORIENTATION", TextField::class),
    FPP(302, "FPP", "EFS FINGER, PALM, PLANTAR POSITION", TextField::class),
    FSP(303, "FSP", "EFS FEATURE SET PROFILE", TextField::class),
    PAT(307, "PAT", "EFS PATTERN CLASSIFICATION", TextField::class),
    RQM(308, "RQM", "EFS RIDGE QUALITY MAP", TextField::class),
    RQF(309, "RQF", "EFS RIDGE QUALITY FORMAT", TextField::class),
    RFM(310, "RFM", "EFS RIDGE FLOW MAP", TextField::class),
    RFF(311, "RFF", "EFS RIDGE FLOW MAP FORMAT", TextField::class),
    RWM(312, "RWM", "EFS RIDGE WAVELENGTH MAP", TextField::class),
    RWF(313, "RWF", "EFS RIDGE WAVELENGTH MAP FORMAT", TextField::class),
    TRV(314, "TRV", "EFS TONAL REVERSAL", TextField::class),
    PLR(315, "PLR", "EFS POSSIBLE LATERAL REVERSAL", TextField::class),
    FQM(316, "FQM", "EFS FRICTION RIDGE QUALITY METRIC", TextField::class),
    PGS(317, "PGS", "EFS POSSIBLE GROWTH OR SHRINKAGE", TextField::class),
    COR(320, "COR", "EFS CORES", TextField::class),
    DEL(321, "DEL", "EFS DELTAS", TextField::class),
    CDR(322, "CDR", "EFS CORE-DELTA RIDGE COUNTS", TextField::class),
    CPR(323, "CPR", "EFS CENTER POINT OF REFERENCE", TextField::class),
    DIS(324, "DIS", "EFS DISTINCTIVE FEATURES", TextField::class),
    NCOR(325, "NCOR", "EFS NO CORES PRESENT", TextField::class),
    NDEL(326, "NDEL", "EFS NO DELTAS PRESENT", TextField::class),
    NDIS(327, "NDIS", "EFS NO DISTINCTIVE FEATURES PRESENT", TextField::class),
    MIN(331, "MIN", "EFS MINUTIAE", TextField::class),
    MRA(332, "MRA", "EFS MINUTIAE RIDGE COUNT ALGORITHM", TextField::class),
    MRC(333, "MRC", "EFS MINUTIAE RIDGE COUNTS", TextField::class),
    NMIN(334, "NMIN", "EFS NO MINUTIA PRESENT", TextField::class),
    RCC(335, "RCC", "EFS RIDGE COUNT CONFIDENCE", TextField::class),
    DOT(340, "DOT", "EFS RIDGE COUNT CONFIDENCE", TextField::class),
    INR(341, "INR", "EFS INCIPIENT RIDGES", TextField::class),
    CLD(342, "CLD", "EFS CREASES AND LINEAR DISCONTINUITIES", TextField::class),
    REF(343, "REF", "EFS RIDGE EDGE FEATURES", TextField::class),
    NPOR(344, "NPOR", "EFS NO PORES PRESENT", TextField::class),
    POR(345, "POR", "EFS PORES", TextField::class),
    NDOT(346, "NDOT", "EFS NO DOTS PRESENT", TextField::class),
    NINR(347, "NINR", "EFS NO INCIPIENT RIDGES PRESENT", TextField::class),
    NCLD(348, "NCLD", "EFS NO CREASES PRESENT", TextField::class),
    NREF(349, "NREF", "EFS NO RIDGE EDG FEATURES PRESENT", TextField::class),
    MFD(350, "MFD", "EFS METHOD OF FEATURE DETECTION", TextField::class),
    COM(351, "COM", "EFS COMMENT", TextField::class),
    LPM(352, "LPM", "EFS LATENT PROCESSING METHOD", TextField::class),
    EAA(353, "EAA", "EFS EXAMINER ANALYSIS ASSESSMENT", TextField::class),
    EOF(354, "EOF", "EFS EVIDENCE OF FRAUD", TextField::class),
    LSB(355, "LSB", "EFS LATENT SUBSTRATE", TextField::class),
    LMT(356, "LMT", "EFS LATENT MATRIX", TextField::class),
    LQI(357, "LQI", "EFS LOCAL QUALITY ISSUES", TextField::class),
    AOC(360, "AOC", "EFS AREA OF CORRESPONDENCE", TextField::class),
    CPF(361, "CPF", "EFS CORRESPONDING POINTS OR FEATURES", TextField::class),
    ECD(362, "ECD", "EFS EXAMINER COMPARISON DETERMINATION", TextField::class),
    RRC(363, "RRC", "EFS RELATIVE ROTATION OF CORRESPONDING PRINT", TextField::class),
    SIM(372, "SIM", "EFS SKELETONIZED IMAGE", TextField::class),
    RPS(373, "RPS", "EFS RIDGE PATH SEGMENTS", TextField::class),
    TPL(380, "TPL", "EFS TEMPORARY LINES", TextField::class),
    FCC(381, "FCC", "EFS FEATURE COLOR [2015a>] AND COMMENT", TextField::class),
    ULA(901, "ULA", "UNIVERSAL LATENT ANNOTATION", TextField::class),
    ANN(902, "ANN", "ANNOTATION INFORMATION", TextField::class),
    DUI(903, "DUI", "DEVICE UNIQUE IDENTIFIER", TextField::class),
    MMS(904, "MMS", "MAKE/MODEL/SERIAL NUMBER", TextField::class),
    ;

    override val recordType: String = "Minutiae data (for fingerprints)"
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
