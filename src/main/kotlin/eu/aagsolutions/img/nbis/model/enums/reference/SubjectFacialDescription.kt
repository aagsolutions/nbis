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
 * Represents various facial descriptions for a subject, conforming to specific
 * ANSI/NIST-ITL standards. Each description provides details about the subject's
 * facial expression, pose, or accessories that may affect their appearance during
 * biometric data collection.
 *
 * @property code A unique string representation for the facial description.
 * @property description A descriptive label providing more details about the facial description.
 * @property createdFromStandard The ANSI/NIST-ITL standard version where this description was first introduced.
 * @property deprecatedFromStandard The ANSI/NIST-ITL standard version where this description was deprecated, if applicable.
 */
enum class SubjectFacialDescription(
    override val code: String,
    val description: String,
    override val createdFromStandard: Standard,
    override val deprecatedFromStandard: Standard?,
) : StandardReference {
    UNKNOWN("UNKNOWN", "Expression unspecified", Standard.ANSI_NIST_ITL_2000, null),
    NEUTRAL(
        "NEUTRAL",
        "Neutral (non-smiling) with both eyes open and mouth closed)",
        Standard.ANSI_NIST_ITL_2000,
        null,
    ),
    SMILE(
        "SMILE",
        "Smiling where the inside of the mouth and/or teeth is not exposed (closed jaw)",
        Standard.ANSI_NIST_ITL_2000,
        null,
    ),
    MOUTH_OPEN("MOUTH OPEN", "Subject Having Mouth open", Standard.ANSI_NIST_ITL_2000, null),
    TEETH_VISIBLE("TEETH VISIBLE", "Having Teeth visible", Standard.ANSI_NIST_ITL_2000, null),
    RAISED_BROWS("RAISED BROWS", "Raising eyebrows", Standard.ANSI_NIST_ITL_2000, null),
    FROWNING("FROWNING", "Frowning", Standard.ANSI_NIST_ITL_2000, null),
    EYES_AWAY("EYES AWAY", "Looking away from the camera", Standard.ANSI_NIST_ITL_2000, null),
    SQUINTING("SQUINTING", "Squinting", Standard.ANSI_NIST_ITL_2000, null),
    LEFT_EYE_PATCH("LEFT EYE PATCH", "Subject Wearing Left Eye Patch", Standard.ANSI_NIST_ITL_2000, null),
    RIGHT_EYE_PATCH("RIGHT EYE PATCH", "Subject Wearing Right Eye Patch", Standard.ANSI_NIST_ITL_2000, null),
    CLEAR_GLASSES("CLEAR GLASSES ", "Subject Wearing Clear Glasses", Standard.ANSI_NIST_ITL_2000, null),
    DARK_GLASSE(
        "DARK GLASSE",
        "Subject Wearing Dark or Visible Colored Glasses (medical)",
        Standard.ANSI_NIST_ITL_2000,
        null,
    ),
    HAT("HAT", "Head covering/hat", Standard.ANSI_NIST_ITL_2000, null),
    SCARF("SCARF", "Wearing Scarf", Standard.ANSI_NIST_ITL_2000, null),
    MOUSTACHE("MOUSTACHE", "Having Moustache ", Standard.ANSI_NIST_ITL_2000, null),
    BEARD("BEARD", "Having Beard", Standard.ANSI_NIST_ITL_2000, null),
    NO_EAR("NO EAR", "Ear(s) obscured by hair", Standard.ANSI_NIST_ITL_2000, null),
    BLINK("BLINK", "Blinking (either or both eyes closed)", Standard.ANSI_NIST_ITL_2000, null),
    DISTORTING_CONDITION(
        "DISTORTING CONDITION",
        "Having Distorting Medical Condition impacting, Feature Point detection",
        Standard.ANSI_NIST_ITL_2000,
        null,
    ),
}
