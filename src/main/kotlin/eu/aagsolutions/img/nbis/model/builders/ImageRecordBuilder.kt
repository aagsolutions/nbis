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
import eu.aagsolutions.img.nbis.model.enums.records.ImageFields
import eu.aagsolutions.img.nbis.model.fields.ImageField
import eu.aagsolutions.img.nbis.model.fields.TextField
import eu.aagsolutions.img.nbis.model.records.BaseRecord
import java.io.ByteArrayInputStream
import javax.imageio.ImageIO

/**
 * Abstract builder class for creating and modifying image-based NIST records.
 *
 * This builder provides methods to set various fields specific to image records,
 * ensuring proper field assignments and data encapsulation for the record type.
 *
 * @param T The type of the resulting record, extending BaseRecord.
 * @param B The builder type, extending ImageRecordBuilder for method chaining.
 * @param id Identifier of the record type.
 * @param label A descriptive label for the record type.
 * @param calculator Responsible for calculating the logical length of the record.
 */
abstract class ImageRecordBuilder<T : BaseRecord, B : ImageRecordBuilder<T, B>>(
    id: Int,
    label: String,
    calculator: LogicalRecordLengthCalculator,
) : NistRecordBuilder<T, B>(id, label, calculator) {
    /**
     * Sets IDC (Information Designation Character) – the record sequence number within the transaction.
     *
     * @param designationChar value for the IDC field
     * @return The builder instance for method chaining
     */
    fun withInformationDesignationCharField(designationChar: String) = withField(ImageFields.IDC.id, TextField(designationChar))

    /**
     * Sets IMP (Impression Type) – indicates the manner by which the fingerprint was obtained.
     *
     * @param impressionType value for the IMP field
     * @return The builder instance for method chaining
     */
    fun withImpressionTypeField(impressionType: String) = withField(ImageFields.IMP.id, TextField(impressionType))

    /**
     * Sets FGP (Finger Position) – indicates the finger position code.
     *
     * @param fingerPosition value for the FGP field
     * @return The builder instance for method chaining
     */
    fun withFingerPositionField(fingerPosition: String) = withField(ImageFields.FGP.id, TextField(fingerPosition))

    /**
     * Sets ISR (Image Scanning Resolution) – the pixel density used to digitize the image.
     *
     * @param scanningResolution value for the ISR field in pixels per inch (ppi)
     * @return The builder instance for method chaining
     */
    fun withImageScanningResolutionField(scanningResolution: String) = withField(ImageFields.ISR.id, TextField(scanningResolution))

    /**
     * Sets HLL (Horizontal Line Length) – the number of pixels on a horizontal line.
     *
     * @param horizontalLength value for the HLL field
     * @return The builder instance for method chaining
     */
    fun withHorizontalLineLengthField(horizontalLength: String) = withField(ImageFields.HLL.id, TextField(horizontalLength))

    /**
     * Sets VLL (Vertical Line Length) – the number of horizontal lines in the image.
     *
     * @param verticalLength value for the VLL field
     * @return The builder instance for method chaining
     */
    fun withVerticalLineLengthField(verticalLength: String) = withField(ImageFields.VLL.id, TextField(verticalLength))

    /**
     * Sets GCA (Greyscale Compression Algorithm) – indicates the algorithm used to compress the image.
     *
     * @param compressionAlgorithm value for the GCA field
     * @return The builder instance for method chaining
     */
    fun withGreyscaleCompressionAlgorithmField(compressionAlgorithm: String) =
        withField(ImageFields.GCA.id, TextField(compressionAlgorithm))

    /**
     * Sets DATA (Image Data) – the binary image data, height (VLL) and width (HLL) are inferred from the image.
     *
     * @param imageData the binary image data as ByteArray
     * @return The builder instance for method chaining
     */
    fun withImageDataField(imageData: ByteArray): B {
        ByteArrayInputStream(imageData).use { inputStream ->
            val bufferedImage = ImageIO.read(inputStream)
            withHorizontalLineLengthField(bufferedImage.width.toString())
            withVerticalLineLengthField(bufferedImage.height.toString())
        }
        return withField(ImageFields.DATA.id, ImageField(imageData))
    }
}
