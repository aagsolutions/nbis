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
 * Represents an enumeration of various compression algorithms that are compliant with ANSI/NIST standards.
 *
 * Each compression algorithm is associated with a unique code, binary code, and description.
 * The enumeration also tracks the standard from which each algorithm was introduced and,
 * if applicable, the standard in which it became deprecated.
 *
 * @property code A string representation of the code associated with the compression algorithm.
 * @property codeBinary A binary representation of the code for the compression algorithm.
 * @property description A short description of the compression algorithm.
 * @property createdFromStandard The standard where the compression algorithm was first introduced.
 * @property deprecatedFromStandard The standard where the compression algorithm became deprecated, or null if it is not deprecated.
 */
enum class CompressionAlgorithm(
    override val code: String,
    val codeBinary: String,
    val description: String,
    override val createdFromStandard: Standard,
    override val deprecatedFromStandard: Standard?,
) : StandardReference {
    NONE("NONE", "0", "Uncompressed", Standard.ANSI_NIST_ITL_2007, null),
    WSQ20("WSQ20", "1", "WSQ", Standard.ANSI_NIST_ITL_2007, null),
    JPEGB("JPEGB", "2", "JPEG ISO/IEC 10918 (Lossy)", Standard.ANSI_NIST_ITL_2007, null),
    JPEGL("JPEGL", "3", "JPEG ISO/IEC 10918 (Lossless)", Standard.ANSI_NIST_ITL_2007, null),
    JP2("JP2", "4", "JPEG 2000 ISO/IEC 15444-1 (Lossy)", Standard.ANSI_NIST_ITL_2007, null),
    JP2L("JP2L", "5", "JPEG 2000 ISO/IEC 15444-1 (Lossless)", Standard.ANSI_NIST_ITL_2007, null),
    PNG("PNG", "6", "Portable Network Graphics", Standard.ANSI_NIST_ITL_2007, null),
}
