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
 * Enumerates a set of subject acquisition profiles, each defined with a unique code, description, and associated
 * standards. These profiles are used to specify the context and standards-compliance of facial images or
 * mugshots being captured.
 *
 * Implements the `StandardReference` interface to allow compatibility checks against specific NIST standards.
 *
 * @property code A unique identifier for the subject acquisition profile.
 * @property description A human-readable description of the subject acquisition profile.
 * @property createdFromStandard The standard in which this profile was introduced.
 * @property deprecatedFromStandard The standard in which this profile was deprecated, if applicable.
 */
enum class SubjectAcquisitionProfile(
    override val code: String,
    val description: String,
    override val createdFromStandard: Standard,
    override val deprecatedFromStandard: Standard?,
) : StandardReference {
    UNKNOWN_PROFILE("0", "Unknown profile", Standard.ANSI_NIST_ITL_2000, null),
    SURVEILLANCE_FACIAL("1", "Surveillance facial image", Standard.ANSI_NIST_ITL_2000, null),
    DRIVER_S_LICENSE("10", "Driver's license image (AAMVA)", Standard.ANSI_NIST_ITL_2000, null),
    ANSI_FULL_FRONTAL_FACIAL(
        "11",
        "ANSI Full Frontal facial image (ANSI 385)",
        Standard.ANSI_NIST_ITL_2000,
        null,
    ),
    ANSI_TOKEN_FACIAL("12", "ANSI Token facial image (ANSI 385)", Standard.ANSI_NIST_ITL_2000, null),
    ISO_FULL_FRONTAL_FACIAL(
        "13",
        "ISO Full Frontal facial image (ISO/IEC 19794-5)",
        Standard.ANSI_NIST_ITL_2000,
        null,
    ),
    ISO_TOKEN_FACIAL("14", "ISO Token facial image (ISO/IEC 19794-5)", Standard.ANSI_NIST_ITL_2000, null),
    PIV_FACIAL("15", "PIV facial image (NIST SP 800-76)", Standard.ANSI_NIST_ITL_2000, null),
    LEGACY_MUGSHOT("20", "Legacy Mugshot", Standard.ANSI_NIST_ITL_2000, null),
    BEST_PRACTICE_APP_LVL_30("30", "Best Practice Application - Level 30", Standard.ANSI_NIST_ITL_2000, null),
    MOBILE_BEST_PRACTICE_LVL_32("32", "Mobile ID Best Practice - Level 32", Standard.ANSI_NIST_ITL_2011, null),
    BEST_PRACTICE_APP_LVL_40("40", "Best Practice Application - Level 40", Standard.ANSI_NIST_ITL_2000, null),
    MOBILE_BEST_PRACTICE_LVL_42("42", "Mobile ID Best Practice - Level 42", Standard.ANSI_NIST_ITL_2011, null),
    BEST_PRACTICE_APP_LVL_50("50", "Best Practice Application - Level 50", Standard.ANSI_NIST_ITL_2000, null),
    BEST_PRACTICE_APP_LVL_51("51", "Best Practice Application - Level 51", Standard.ANSI_NIST_ITL_2000, null),
    MOBILE_BEST_PRACTICE_LVL_52("52", "Mobile ID Best Practice - Level 52", Standard.ANSI_NIST_ITL_2011, null),
}
