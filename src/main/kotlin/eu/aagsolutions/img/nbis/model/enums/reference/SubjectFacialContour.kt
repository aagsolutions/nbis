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
 * Enumerates the different facial contour points of a subject as defined in specific NIST standards.
 *
 * Each enum constant represents a specific feature or contour of the face.
 * The enum provides metadata such as a unique code, description, and information
 * about when it was introduced or deprecated in the context of ANSI/NIST-ITL standards.
 *
 * This enum implements the `StandardReference` interface, enabling compatibility checks
 * with various NIST standards.
 *
 * @property code A unique string identifier for the facial contour point.
 * @property description A descriptive name or explanation of the facial contour point.
 * @property createdFromStandard The NIST standard in which this facial contour point was introduced.
 * @property deprecatedFromStandard The NIST standard in which this facial contour point was deprecated, or null if still valid.
 */
enum class SubjectFacialContour(
    override val code: String,
    val description: String,
    override val createdFromStandard: Standard,
    override val deprecatedFromStandard: Standard?,
) : StandardReference {
    EYETOP("eyetop", "Bottom of upper eye lid", Standard.ANSI_NIST_ITL_2011, null),
    EYEBOTTOM("eyebottom", "Top of lower eye lid", Standard.ANSI_NIST_ITL_2011, null),
    UPPERLIPTOP("upperliptop", "Top of upper lip", Standard.ANSI_NIST_ITL_2011, null),
    UPPERLIPBOTTOM("upperlipbottom", "Bottom of upper lip", Standard.ANSI_NIST_ITL_2011, null),
    LOWERLIPTOP("lowerliptop", "Top of lower lip", Standard.ANSI_NIST_ITL_2011, null),
    LOWERLIPBOTTOM("lowerlipbottom", "Bottom of lower lip", Standard.ANSI_NIST_ITL_2011, null),
    RIGHTNOSTRIL("rightnostril", "Subject's right nostril", Standard.ANSI_NIST_ITL_2011, null),
    LEFTNOSTRIL("leftnostril", "Subject's left nostril", Standard.ANSI_NIST_ITL_2011, null),
    LEFTEYEBROW(
        "lefteyebrow",
        "Curvature of top of subject's left eye socket",
        Standard.ANSI_NIST_ITL_2011,
        null,
    ),
    RIGHTEYEBROW(
        "righteyebrow",
        "Curvature of top of subject's right eye socket",
        Standard.ANSI_NIST_ITL_2011,
        null,
    ),
    CHIN("chin", "Chin", Standard.ANSI_NIST_ITL_2011, null),
    FACEOUTLINE(
        "faceoutline",
        "Face outline includes the entire head, all facial hair, and ears",
        Standard.ANSI_NIST_ITL_2011,
        null,
    ),
}
