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
 * Defines various modes of operation for device monitoring within the context of biometric
 * sample acquisition. Each mode specifies the level of operator or subject involvement
 * during the biometric capture process.
 *
 * Implements the `StandardReference` interface to provide compatibility with specific
 * ANSI/NIST standards, facilitating validation of allowable modes based on
 * standard revisions.
 *
 * @property code A unique code representing the mode.
 * @property description A textual description of the mode's characteristics.
 * @property createdFromStandard The specific ANSI/NIST standard in which this mode was introduced.
 * @property deprecatedFromStandard The specific ANSI/NIST standard in which this mode was deprecated, or null if still valid.
 */
enum class DeviceMonitoringMode(
    override val code: String,
    val description: String,
    override val createdFromStandard: Standard,
    override val deprecatedFromStandard: Standard?,
) : StandardReference {
    CONTROLLED(
        "CONTROLLED",
        "Operator physically controls the subject to acquire the biometric sample",
        Standard.ANSI_NIST_ITL_2007,
        null,
    ),
    ASSISTED(
        "ASSISTED",
        "Person available to provide assistance to subject submitting the biometric",
        Standard.ANSI_NIST_ITL_2007,
        null,
    ),
    OBSERVED(
        "OBSERVED",
        "Person present to observe operation of the device but provides no assistance",
        Standard.ANSI_NIST_ITL_2007,
        null,
    ),
    UNATTENDED(
        "UNATTENDED",
        "No one is present to observe or provide assistance",
        Standard.ANSI_NIST_ITL_2007,
        null,
    ),
    UNKNOWN(
        "UNKNOWN",
        "No information is known",
        Standard.ANSI_NIST_ITL_2007,
        null,
    ),
}
