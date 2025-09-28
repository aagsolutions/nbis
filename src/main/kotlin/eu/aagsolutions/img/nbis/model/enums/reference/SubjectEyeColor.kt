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
 * Enum representing subject eye color categories defined by specific codes, descriptions,
 * and their creation and deprecation based on NIST standards.
 *
 * Each eye color has an associated standard code, a description, and metadata indicating
 * the NIST standard in which it was introduced and the standard in which it became deprecated, if applicable.
 *
 * Implements the `StandardReference` interface to provide standard compatibility validation.
 *
 * @property code The unique code representing the eye color.
 * @property description The descriptive name of the eye color.
 * @property createdFromStandard The NIST standard in which this eye color was introduced.
 * @property deprecatedFromStandard The NIST standard in which this eye color became deprecated, or null if it is still valid.
 */
enum class SubjectEyeColor(
    override val code: String,
    val description: String,
    override val createdFromStandard: Standard,
    override val deprecatedFromStandard: Standard?,
) : StandardReference {
    BLACK("BLK", "Black", Standard.ANSI_NIST_ITL_2000, null),
    BLUE("BLU", "Blue", Standard.ANSI_NIST_ITL_2000, null),
    BROWN("BRO", "Brown", Standard.ANSI_NIST_ITL_2000, null),
    GRAY("GRY", "Gray", Standard.ANSI_NIST_ITL_2000, null),
    GREEN("GRN", "Green", Standard.ANSI_NIST_ITL_2000, null),
    HAZEL("HAZ", "Hazel", Standard.ANSI_NIST_ITL_2000, null),
    MAROON("MAR", "Maroon", Standard.ANSI_NIST_ITL_2000, null),
    MULTICOLORED("MUL", "Multicolored", Standard.ANSI_NIST_ITL_2000, null),
    PINK("PNK", "Pink", Standard.ANSI_NIST_ITL_2000, null),
    UNKNOWN("XXX", "Unknown", Standard.ANSI_NIST_ITL_2000, null),
}
