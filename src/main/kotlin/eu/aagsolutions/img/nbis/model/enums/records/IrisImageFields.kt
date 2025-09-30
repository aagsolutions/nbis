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

@Suppress("MagicNumber")
enum class IrisImageFields : FieldType {
    LEN(ImageFields.LEN),
    IDC(ImageFields.IDC),
    ELR(3, "ELR", "Eye label", TextField::class),
    SRC(4, "SRC", "Source agency", TextField::class),
    ICD(5, "ICD", "Iris capture date", TextField::class),
    HLL(6, "HLL", "Horizontal line length", TextField::class),
    VLL(7, "VLL", "Vertical line length", TextField::class),
    SLC(8, "SLC", "Scale units", TextField::class),
    THPS(9, "THPS", "Transmitted horizontal pixel scale", TextField::class),
    TVPS(10, "TVPS", "Transmitted vertical pixel scale", TextField::class),
    GCA(11, "GCA", "Grayscale compression algorithm", TextField::class),
    BPX(12, "BPX", "Bits per pixel", TextField::class),
    CSP(13, "CSP", "Color space", TextField::class),
    RAE(14, "RAE", "Rotation angle of eye", TextField::class),
    RAU(15, "RAU", "Rotation uncertainty", TextField::class),
    IPC(16, "IPC", "Image processing code", TextField::class),
    DUI(17, "DUI", "Device unique identifier", TextField::class),
    GUI(18, "GUI", "Global unique identifier (deprecated)", TextField::class),
    MMS(19, "MMS", "Make / Model / Serial number", TextField::class),
    ECL(20, "ECL", "Eye color", TextField::class),
    COM(21, "COM", "Comment", TextField::class),
    SHPS(22, "SHPS", "Scan horizontal pixel scale", TextField::class),
    SVPS(23, "SVPS", "Scan vertical pixel scale", TextField::class),
    IQS(24, "IQS", "Image quality score", TextField::class),
    EAS(25, "EAS", "Effective acquisition spectrum", TextField::class),
    IRD(26, "IRD", "Iris diameter", TextField::class),
    SSV(27, "SSV", "Specified spectrum value", TextField::class),
    DME(28, "DME", "Damaged or missing eye", TextField::class),
    DMM(30, "DMM", "Device monitoring mode", TextField::class),
    IAP(31, "IAP", "Subject acquisition profile - iris", TextField::class),
    ISF(32, "ISF", "Iris storage format", TextField::class),
    IPB(33, "IBP", "Iris pupil boundary", TextField::class),
    ISB(34, "ISB", "Iris sclera boundary", TextField::class),
    UEB(35, "UEB", "Upper eyelid boundary", TextField::class),
    LEB(36, "LEB", "Lower eyelid boundary", TextField::class),
    NEO(37, "NEO", "Non-eyelid occlusions", TextField::class),
    RAN(40, "RAN", "Range", TextField::class),
    GAZ(41, "GAZ", "Frontal gaze", TextField::class),
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
