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
 * Represents a set of predefined subject poses, each associated with a unique code,
 * a description, and compatibility information based on their creation and deprecation
 * across specific NIST standards.
 *
 * This enum implements the `StandardReference` interface, enabling
 * compatibility validation for various NIST standards.
 *
 * @property code The unique identifier for the subject pose.
 * @property description A descriptive label for the subject pose.
 * @property createdFromStandard The NIST standard in which this subject pose was introduced.
 * @property deprecatedFromStandard The NIST standard in which this subject pose became deprecated, or null if still valid.
 */
enum class SubjectPose(
    override val code: String,
    val description: String,
    override val createdFromStandard: Standard,
    override val deprecatedFromStandard: Standard?,
) : StandardReference {
    FULL_FACE_FRONTAL("F", "Full Face Frontal", Standard.ANSI_NIST_ITL_2000, null),
    RIGHT_PROFILE("R", "Right Profile (90 degree)", Standard.ANSI_NIST_ITL_2000, null),
    LEFT_PROFILE("L", "Left Profile (90 degree)", Standard.ANSI_NIST_ITL_2000, null),
    ANGLED_POSE("A", "Angled Pose", Standard.ANSI_NIST_ITL_2000, null),
    DETERMINED_3D_POSE("D", "Determined 3D Pose", Standard.ANSI_NIST_ITL_2000, null),
}
