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
 * Enum representing descriptive attributes of a photo.
 *
 * Each attribute has a unique identifier, a textual description, and information about its
 * compatibility across different versions of the ANSI/NIST-ITL standards. This enum
 * implements the `StandardReference` interface, allowing retrieval of associated standards
 * and determining validity within specified standards.
 *
 * @property code The unique identifier for the photo attribute.
 * @property description A brief textual description of the photo attribute.
 * @property createdFromStandard The standard in which this attribute was introduced.
 * @property deprecatedFromStandard The standard in which this attribute became deprecated, or null if it is still valid.
 */
enum class PhotoDescriptor(
    override val code: String,
    val description: String,
    override val createdFromStandard: Standard,
    override val deprecatedFromStandard: Standard?,
) : StandardReference {
    GLASSES("GLASSES", "Subject Wearing Glasses", Standard.ANSI_NIST_ITL_2000, null),
    HAT("HAT", "Subject Wearing Hat", Standard.ANSI_NIST_ITL_2000, null),
    SCARF("SCARF", "Subject Wearing Scarf", Standard.ANSI_NIST_ITL_2000, null),
    PHYSICAL("PHYSICAL", "Physical Characteristics", Standard.ANSI_NIST_ITL_2000, null),
    OTHER("OTHER", "Other Characteristics", Standard.ANSI_NIST_ITL_2000, null),
}
