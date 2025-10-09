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

package eu.aagsolutions.img.nbis.model.builders

import eu.aagsolutions.img.nbis.calculators.LogicalRecordLengthCalculator
import eu.aagsolutions.img.nbis.io.ImageParser
import eu.aagsolutions.img.nbis.model.enums.records.ImageFields
import eu.aagsolutions.img.nbis.model.fields.ImageField
import eu.aagsolutions.img.nbis.model.records.BaseRecord

abstract class TextRecordWithImageBuilder<T : BaseRecord, B : TextRecordWithImageBuilder<T, B>>(
    id: Int,
    label: String,
    calculator: LogicalRecordLengthCalculator,
) : NistRecordBuilder<T, B>(id, label, calculator) {
    /**
     * Sets HLL (Horizontal Line Length) – the number of pixels contained on a single horizontal line.
     *
     * @param horizontalLineLength The number of pixels per horizontal line
     * @return The builder instance for method chaining
     */
    abstract fun withHorizontalLineLengthField(horizontalLineLength: String): B

    /**
     * Sets VLL (Vertical Line Length) – the number of horizontal lines contained in the image.
     *
     * @param verticalLineLength The number of horizontal lines
     * @return The builder instance for method chaining
     */
    abstract fun withVerticalLineLengthField(verticalLineLength: String): B

    /**
     * Sets CGA (Compression Algorithm) – specifies the algorithm used to compress the image data.
     *
     * @param compressionAlgorithm The compression algorithm identifier
     * @return The builder instance for method chaining
     */
    abstract fun withCompressionAlgorithmField(compressionAlgorithm: String): B

    /**
     * Sets THPS (Transmitted Horizontal Pixel Scale) – the horizontal pixel density.
     *
     * @param horizontalPixelScale The horizontal pixel density value
     * @return The builder instance for method chaining
     */
    abstract fun withTransmittedHorizontalPixelScaleField(horizontalPixelScale: String): B

    /**
     * Sets TVPS (Transmitted Vertical Pixel Scale) – the vertical pixel density.
     *
     * @param verticalPixelScale The vertical pixel density value
     * @return The builder instance for method chaining
     */
    abstract fun withTransmittedVerticalPixelScaleField(verticalPixelScale: String): B

    /**
     * Sets the color space for the image being built. The color space specifies the format
     * in which colors are represented (e.g., RGB, CMYK, grayscale).
     *
     * @param colorSpace The color space identifier as a string
     * @return The builder instance for method chaining
     */
    abstract fun withColorSpaceField(colorSpace: String): B

    /**
     * Sets DATA (Image Data) - the actual image data in the specified format,
     * height (VLL) and width (HLL) are inferred from the image.
     *
     * @param imageData The binary image data
     * @return The builder instance for method chaining
     */
    fun withImageDataField(imageData: ByteArray): B {
        val imageInfo = ImageParser.readImageInfo(imageData)
        return this
            .withHorizontalLineLengthField(imageInfo.width.toString())
            .withVerticalLineLengthField(imageInfo.height.toString())
            .withCompressionAlgorithmField(imageInfo.compressionAlgorithm.code)
            .withTransmittedHorizontalPixelScaleField(imageInfo.pixelsPerInchX.toString())
            .withTransmittedVerticalPixelScaleField(imageInfo.pixelsPerInchY.toString())
            .withColorSpaceField(imageInfo.colorSpace)
            .withField(ImageFields.DATA.id, ImageField(imageData))
    }
}
