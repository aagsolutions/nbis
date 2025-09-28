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
 * NIST reference enum for fingerprint acquisition profile codes.
 *
 * Defines the various acquisition profile levels used for fingerprint
 * capture according to NIST standards.
 */
enum class FingerprintAcquisitionProfile(
    override val code: String,
    override val createdFromStandard: Standard,
    override val deprecatedFromStandard: Standard?,
) : StandardReference {
    FP_10("10", Standard.ANSI_NIST_ITL_2011, null),
    FP_20("20", Standard.ANSI_NIST_ITL_2011, null),
    FP_30("30", Standard.ANSI_NIST_ITL_2011, null),
    FP_40("40", Standard.ANSI_NIST_ITL_2011, null),
    FP_45("45", Standard.ANSI_NIST_ITL_2011, null),
    FP_50("50", Standard.ANSI_NIST_ITL_2011, null),
    FP_60("60", Standard.ANSI_NIST_ITL_2011, null),
    FP_145("145", Standard.ANSI_NIST_ITL_2015, null),
    FP_150("150", Standard.ANSI_NIST_ITL_2015, null),
    FP_160("160", Standard.ANSI_NIST_ITL_2015, null),
}
