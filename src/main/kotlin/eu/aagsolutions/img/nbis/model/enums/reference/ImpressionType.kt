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
 * Represents the different types of impressions for biometric data as defined by various standards.
 * Each type includes an identifying code, descriptive name, the standard in which it was introduced,
 * and optionally the standard in which it was deprecated.
 *
 * @property code The unique code representing the impression type.
 * @property description A description of the impression type.
 * @property createdFromStandard The standard in which this impression type was first introduced.
 * @property deprecatedFromStandard The standard in which this impression type was deprecated, if applicable.
 */
enum class ImpressionType(
    override val code: String,
    val description: String,
    override val createdFromStandard: Standard,
    override val deprecatedFromStandard: Standard?,
) : StandardReference {
    PLAIN_CONTACT_FINGERPRINT(
        "0",
        "Plain fingerprint with contact - Former \"Livescan Plain fingerprint\"",
        Standard.ANSI_NIST_ITL_2000,
        null,
    ),
    ROLLED_CONTACT_FINGERPRINT(
        "1",
        "Rolled fingerprint with contact - Former \"Livescan Rolled fingerprint\"",
        Standard.ANSI_NIST_ITL_2000,
        null,
    ),
    NON_LIVESCAN_OF_PLAIN_FINGERPRINT(
        "2",
        "Nonlive-scan plain",
        Standard.ANSI_NIST_ITL_2000,
        Standard.ANSI_NIST_ITL_2015,
    ),
    NON_LIVESCAN_OF_ROLLED_FINGERPRINT(
        "3",
        "Nonlive-scan roll",
        Standard.ANSI_NIST_ITL_2000,
        Standard.ANSI_NIST_ITL_2015,
    ),
    LATENT_IMAGE(
        "4",
        "Latent image or impression - Former \"Latent impression\"",
        Standard.ANSI_NIST_ITL_2000,
        null,
    ),
    LATENT_TRACING("5", "Latent tracing", Standard.ANSI_NIST_ITL_2000, Standard.ANSI_NIST_ITL_2015),
    LATENT_PHOTO("6", "Latent photo", Standard.ANSI_NIST_ITL_2000, Standard.ANSI_NIST_ITL_2015),
    LATENT_LIFT("7", "Latent lift", Standard.ANSI_NIST_ITL_2000, Standard.ANSI_NIST_ITL_2015),
    LIVESCAN_SWIPE_FP(
        "8",
        "Finger swiped on platen - Former \"Livescan Vertical Swipe fingerprint\"",
        Standard.ANSI_NIST_ITL_2007,
        null,
    ),
    LIVESCAN_PALM("10", "Livescan Palm", Standard.ANSI_NIST_ITL_2007, Standard.ANSI_NIST_ITL_2015),
    NON_LIVESCAN_PALM("11", "Non Livescan Palm", Standard.ANSI_NIST_ITL_2007, Standard.ANSI_NIST_ITL_2015),
    LATENT_PALM_IMPRESSION("12", "Latent Palm Impression", Standard.ANSI_NIST_ITL_2007, Standard.ANSI_NIST_ITL_2015),
    LATENT_PALM_TRACING("13", "Latent Palm Tracing", Standard.ANSI_NIST_ITL_2007, Standard.ANSI_NIST_ITL_2015),
    LATENT_PALM_PHOTO("14", "Latent Palm Photo", Standard.ANSI_NIST_ITL_2007, Standard.ANSI_NIST_ITL_2015),
    LATENT_PALM_LIFT("15", "Latent Palm Lift", Standard.ANSI_NIST_ITL_2007, Standard.ANSI_NIST_ITL_2015),
    LIVESCAN_OPTICAL_CONTACT_PLAIN(
        "20",
        "Live-scan optical contact plain",
        Standard.ANSI_NIST_ITL_2007,
        Standard.ANSI_NIST_ITL_2015,
    ),
    LIVESCAN_OPTICAL_CONTACT_ROLLED(
        "21",
        "Live-scan optical contact rolled",
        Standard.ANSI_NIST_ITL_2007,
        Standard.ANSI_NIST_ITL_2015,
    ),
    LIVESCAN_NON_OPTICAL_CONTACT_PLAIN(
        "22",
        "Live-scan non optical contact plain",
        Standard.ANSI_NIST_ITL_2007,
        Standard.ANSI_NIST_ITL_2015,
    ),
    LIVESCAN_NON_OPTICAL_CONTACT_ROLLED(
        "23",
        "Live-scan non optical contact rolled",
        Standard.ANSI_NIST_ITL_2007,
        Standard.ANSI_NIST_ITL_2015,
    ),
    PLAIN_CONTACTLESS_STATIONARY_SUBJECT(
        "24",
        "Plain contactless – stationary subject - Former \"Livescan Optical contacless plain fingerprint\"",
        Standard.ANSI_NIST_ITL_2007,
        null,
    ),
    ROLLED_CONTACTLESS_STATIONARY_SUBJECT(
        "25",
        "Rolled contactless – stationary subject - Former \"Livescan Optical contacless rolled fingerprint\"",
        Standard.ANSI_NIST_ITL_2007,
        null,
    ),
    LIVESCAN_NON_OPTICAL_CONTACTLESS_PLAIN(
        "26",
        "Live-scan non optical contactless plain",
        Standard.ANSI_NIST_ITL_2007,
        Standard.ANSI_NIST_ITL_2015,
    ),
    LIVESCAN_NON_OPTICAL_CONTACTLESS_ROLLED(
        "27",
        "Live-scan non optical contactless rolled",
        Standard.ANSI_NIST_ITL_2007,
        Standard.ANSI_NIST_ITL_2015,
    ),
    OTHER("28", "Other", Standard.ANSI_NIST_ITL_2007, null),
    UNKNOWN("29", "Unknown", Standard.ANSI_NIST_ITL_2007, null),
    LIVESCAN_PLANTAR(
        "30",
        "Livescan (type unknown or unspecified) plantar",
        Standard.ANSI_NIST_ITL_2011,
        Standard.ANSI_NIST_ITL_2015,
    ),
    NON_LIVESCAN_PLANTAR("31", "Non livescan plantar", Standard.ANSI_NIST_ITL_2011, Standard.ANSI_NIST_ITL_2015),
    LATENT_PLANTAR_IMPRESSION(
        "32",
        "Latent Plantar Impression",
        Standard.ANSI_NIST_ITL_2011,
        Standard.ANSI_NIST_ITL_2015,
    ),
    LATENT_PLANTAR_TRACING("33", "Latent Plantar Tracing", Standard.ANSI_NIST_ITL_2011, Standard.ANSI_NIST_ITL_2015),
    LATENT_PLANTAR_PHOTO("34", "Latent Plantar Photo", Standard.ANSI_NIST_ITL_2011, Standard.ANSI_NIST_ITL_2015),
    LATENT_PLANTAR_LIFT("35", "Latent Plantar Lift", Standard.ANSI_NIST_ITL_2011, Standard.ANSI_NIST_ITL_2015),
    LATENT_UNKNOWN_FRICTION_IMPRESSION(
        "36",
        "Latent Unknown friction ridge Impression",
        Standard.ANSI_NIST_ITL_2011,
        Standard.ANSI_NIST_ITL_2015,
    ),
    LATENT_UNKNOWN_FRICTION_TRACING(
        "37",
        "Latent Unknown friction ridge Tracing",
        Standard.ANSI_NIST_ITL_2011,
        Standard.ANSI_NIST_ITL_2015,
    ),
    LATENT_UNKNOWN_FRICTION_PHOTO(
        "38",
        "Latent Unknown friction ridge Photo",
        Standard.ANSI_NIST_ITL_2011,
        Standard.ANSI_NIST_ITL_2015,
    ),
    LATENT_UNKNOWN_FRICTION_LIFT(
        "39",
        "Latent Unknown friction ridge Lift",
        Standard.ANSI_NIST_ITL_2011,
        Standard.ANSI_NIST_ITL_2015,
    ),
    PLAIN_CONTACTLESS_MOVING_SUBJECT(
        "41",
        "Rolled contactless – moving subject",
        Standard.ANSI_NIST_ITL_2015,
        null,
    ),
    ROLLED_CONTACTLESS_MOVING_SUBJECT(
        "42",
        "Plain contactless – moving subject",
        Standard.ANSI_NIST_ITL_2015,
        null,
    ),
}
