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
 * Enum class representing the hair color of a subject as defined by specific NIST standards.
 *
 * Each hair color is associated with a unique code, description, and metadata about its
 * creation and optional deprecation based on ANSI/NIST-ITL standards. This enum implements
 * the `StandardReference` interface to provide functionality for validating compatibility
 * with specified NIST standards.
 *
 * @property code The unique identifier for the hair color.
 * @property description A descriptive name of the hair color.
 * @property createdFromStandard The NIST standard in which this hair color was introduced.
 * @property deprecatedFromStandard The NIST standard in which this hair color was deprecated, or null if still valid.
 */
enum class SubjectHairColor(
    override val code: String,
    val description: String,
    override val createdFromStandard: Standard,
    override val deprecatedFromStandard: Standard?,
) : StandardReference {
    UNKNOWN("XXX", "Unspecified or unknown", Standard.ANSI_NIST_ITL_2000, null),
    BALD("BAL", "Bald", Standard.ANSI_NIST_ITL_2000, null),
    BLACK("BLK", "Black", Standard.ANSI_NIST_ITL_2000, null),
    BLONDE("BLN", "Blonde or Strawberry", Standard.ANSI_NIST_ITL_2000, null),
    BROWN("BRO", "Brown", Standard.ANSI_NIST_ITL_2000, null),
    GRAY("GRY", "Gray or Partially Gray", Standard.ANSI_NIST_ITL_2000, null),
    RED("RED", "Red or Auburn", Standard.ANSI_NIST_ITL_2000, null),
    SANDY("SDY", "Sandy", Standard.ANSI_NIST_ITL_2000, null),
    WHITE("WHI", "White", Standard.ANSI_NIST_ITL_2000, null),
    BLUE("BLU", "Blue", Standard.ANSI_NIST_ITL_2000, null),
    GREEN("GRN", "Green", Standard.ANSI_NIST_ITL_2000, null),
    ORANGE("ONG", "Orange", Standard.ANSI_NIST_ITL_2000, null),
    PINK("PNK", "Pink", Standard.ANSI_NIST_ITL_2000, null),
    PURPLE("PLE", "Purple", Standard.ANSI_NIST_ITL_2000, null),
}
