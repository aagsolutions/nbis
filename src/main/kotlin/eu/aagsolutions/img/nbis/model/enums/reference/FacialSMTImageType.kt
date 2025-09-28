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
 * Represents predefined types of facial images used in biometric systems.
 *
 * This enum defines categories such as facial images, scars, marks, and tattoos,
 * along with their corresponding unique codes and descriptions. Each type is associated
 * with the NIST standard in which it was introduced, as well as the standard in which it
 * was deprecated, if applicable.
 *
 * This enum implements the `StandardReference` interface, adopting mechanisms
 * to validate its compatibility with given NIST standards.
 *
 * @property code The unique identifier for the image type.
 * @property description A description of the image type.
 * @property createdFromStandard The NIST standard in which this image type was introduced.
 * @property deprecatedFromStandard The NIST standard in which this image type became deprecated, or null if still valid.
 */
enum class FacialSMTImageType(
    override val code: String,
    val description: String,
    override val createdFromStandard: Standard,
    override val deprecatedFromStandard: Standard?,
) : StandardReference {
    FACE("FACE", "Facial image", Standard.ANSI_NIST_ITL_2000, null),
    SCAR("SCAR", "Scar image", Standard.ANSI_NIST_ITL_2000, null),
    MARK("MARK", "Mark image", Standard.ANSI_NIST_ITL_2000, null),
    TATTOO("TATTOO", "Tattoo image", Standard.ANSI_NIST_ITL_2000, null),
}
