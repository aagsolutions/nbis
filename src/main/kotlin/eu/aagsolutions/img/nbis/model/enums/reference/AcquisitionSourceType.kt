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

package eu.aagsolutions.img.nbis.model.enums.reference

import eu.aagsolutions.img.nbis.model.enums.Standard

/**
 * Represents the different types of acquisition sources as defined by ANSI/NIST standards.
 *
 * Each acquisition source type is assigned a unique code and descriptive label, which provide
 * information regarding the origin of an image or frame (e.g., a digital camera, a scanner).
 *
 * @property code The unique string code for the acquisition source type.
 * @property description A descriptive label for the acquisition source type.
 * @property createdFromStandard The standard version in which this acquisition source type was introduced.
 * @property deprecatedFromStandard The standard version in which this acquisition source type was deprecated, or null if it has not been deprecated.
 */
enum class AcquisitionSourceType(
    override val code: String,
    val description: String,
    override val createdFromStandard: Standard,
    override val deprecatedFromStandard: Standard?,
) : StandardReference {
    UNSPECIFIED("UNSPECIFIED", "Unspecified or unknown", Standard.ANSI_NIST_ITL_2000, null),
    UNKNOWN_PHOTO(
        "UNKNOWN PHOTO",
        "Static photograph from an unknown source",
        Standard.ANSI_NIST_ITL_2000,
        null,
    ),
    DIGITAL_CAMERA(
        "DIGITAL CAMERA",
        "Static photograph from a digital still-image camera",
        Standard.ANSI_NIST_ITL_2000,
        null,
    ),
    SCANNER("SCANNER", "Static photograph from a scanner", Standard.ANSI_NIST_ITL_2000, null),
    UNKNOWN_VIDEO(
        "UNKNOWN VIDEO",
        "Single video frame from an unknown source",
        Standard.ANSI_NIST_ITL_2000,
        null,
    ),
    ANALOGUE_VIDEO(
        "ANALOGUE VIDEO",
        "Single video frame from an analogue video camera",
        Standard.ANSI_NIST_ITL_2000,
        null,
    ),
    DIGITAL_VIDEO(
        "DIGITAL VIDEO",
        "Single video frame from a digital video camera",
        Standard.ANSI_NIST_ITL_2000,
        null,
    ),
    VENDOR("VENDOR", "Vendor Specific source", Standard.ANSI_NIST_ITL_2000, null),
}
