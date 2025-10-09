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

import eu.aagsolutions.img.nbis.calculators.TextRecordLengthCalculator
import eu.aagsolutions.img.nbis.model.enums.RecordType
import eu.aagsolutions.img.nbis.model.enums.records.IrisImageFields
import eu.aagsolutions.img.nbis.model.enums.records.LatentImageFields
import eu.aagsolutions.img.nbis.model.fields.TextField
import eu.aagsolutions.img.nbis.model.records.IrisImageRecord

class IrisImageRecordBuilder :
    TextRecordWithImageBuilder<IrisImageRecord, IrisImageRecordBuilder>(
        RecordType.RT17.id,
        RecordType.RT17.label,
        TextRecordLengthCalculator(),
    ) {
    override fun build() = IrisImageRecord(this.fields)

    /**
     * Sets IDC (Information Designation Character) – uniquely identifies the record within the transaction.
     *
     * @param designationChar The designation character value for the IDC field
     * @return The builder instance for method chaining
     */
    fun withInformationDesignationCharField(designationChar: String) =
        withField(
            LatentImageFields.IDC.id,
            TextField(designationChar),
        )

    /**
     * Sets HLL (Horizontal Line Length) – the number of pixels contained on a single horizontal line.
     *
     * @param horizontalLineLength The number of pixels per horizontal line
     * @return The builder instance for method chaining
     */
    override fun withHorizontalLineLengthField(horizontalLineLength: String) =
        withField(
            IrisImageFields.HLL.id,
            TextField(horizontalLineLength),
        )

    /**
     * Sets VLL (Vertical Line Length) – the number of horizontal lines contained in the image.
     *
     * @param verticalLineLength The number of horizontal lines
     * @return The builder instance for method chaining
     */
    override fun withVerticalLineLengthField(verticalLineLength: String) =
        withField(
            IrisImageFields.VLL.id,
            TextField(verticalLineLength),
        )

    /**
     * Sets THPS (Transmitted Horizontal Pixel Scale) – the horizontal pixel density.
     *
     * @param horizontalPixelScale The horizontal pixel density value
     * @return The builder instance for method chaining
     */
    override fun withTransmittedHorizontalPixelScaleField(horizontalPixelScale: String) =
        withField(
            IrisImageFields.THPS.id,
            TextField(horizontalPixelScale),
        )

    /**
     * Sets TVPS (Transmitted Vertical Pixel Scale) – the vertical pixel density.
     *
     * @param verticalPixelScale The vertical pixel density value
     * @return The builder instance for method chaining
     */
    override fun withTransmittedVerticalPixelScaleField(verticalPixelScale: String) =
        withField(
            IrisImageFields.TVPS.id,
            TextField(verticalPixelScale),
        )

    override fun withColorSpaceField(colorSpace: String) = withField(IrisImageFields.CSP.id, TextField(colorSpace))

    /**
     * Sets CGA (Compression Algorithm) – specifies the algorithm used to compress the image data.
     *
     * @param compressionAlgorithm The compression algorithm identifier
     * @return The builder instance for method chaining
     */
    override fun withCompressionAlgorithmField(compressionAlgorithm: String) =
        withField(
            IrisImageFields.GCA.id,
            TextField(compressionAlgorithm),
        )
}
