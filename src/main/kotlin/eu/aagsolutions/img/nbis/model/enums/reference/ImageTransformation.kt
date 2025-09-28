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
 * Represents various image transformation types, each associated with a unique code,
 * a description, and compatibility information regarding their creation and deprecation
 * across specific standards.
 *
 * This enum implements the `StandardReference` interface to provide compatibility
 * validation with ANSI/NIST standards.
 *
 * @property code The unique identifier for the image transformation.
 * @property description A descriptive name for the image transformation.
 * @property createdFromStandard The NIST standard in which this transformation was introduced.
 * @property deprecatedFromStandard The NIST standard in which this transformation became deprecated, or null if still valid.
 */
enum class ImageTransformation(
    override val code: String,
    val description: String,
    override val createdFromStandard: Standard,
    override val deprecatedFromStandard: Standard?,
) : StandardReference {
    AGE("AGE", "Age progressed", Standard.ANSI_NIST_ITL_2011, null),
    AXIS("AXIS", "Off-axis image rectification / Angle correction", Standard.ANSI_NIST_ITL_2011, null),
    COLORSHIFT("COLORSHIFT", "Color shifted", Standard.ANSI_NIST_ITL_2011, null),
    CONTRAST("CONTRAST", "Contrast stretched", Standard.ANSI_NIST_ITL_2011, null),
    CROP("CROP", "Cropped", Standard.ANSI_NIST_ITL_2011, null),
    DIST("DIST", "Distortion corrected (e.g. fisheye correction)", Standard.ANSI_NIST_ITL_2011, null),
    DOWNSAMPLE("DOWNSAMPLE", "Down-sampled", Standard.ANSI_NIST_ITL_2011, null),
    GRAY("GRAY", "Grayscale from color", Standard.ANSI_NIST_ITL_2011, null),
    ILLUM("ILLUM", "Illumination transform", Standard.ANSI_NIST_ITL_2011, null),
    IMGFUSE("IMGFUSE", "Image-level fusion of two or more images", Standard.ANSI_NIST_ITL_2011, null),
    INTERPOLATE("INTERPOLATE", "Up-sampled", Standard.ANSI_NIST_ITL_2011, null),
    MULTCOMP("MULTCOMP", "Multiply compressed", Standard.ANSI_NIST_ITL_2011, null),
    MULTIVIEW("MULTIVIEW", "Multi-view image", Standard.ANSI_NIST_ITL_2011, null),
    POSE("POSE", "Face-specific pose correction", Standard.ANSI_NIST_ITL_2011, null),
    ROTATE("ROTATE", "Rotated (in-plane)", Standard.ANSI_NIST_ITL_2011, null),
    SNIR("SNIR", "Simulated Near IR", Standard.ANSI_NIST_ITL_2011, null),
    SUPERRES("SUPERRES", "Super-resolution image, derived from multiple lower resolution images", Standard.ANSI_NIST_ITL_2011, null),
    WHITE("WHITE", "White balance adjusted", Standard.ANSI_NIST_ITL_2011, null),
}
