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

import eu.aagsolutions.img.nbis.calculators.BinaryRecordLengthCalculator
import eu.aagsolutions.img.nbis.model.enums.RecordType
import eu.aagsolutions.img.nbis.model.enums.records.UserDefinedImageFields
import eu.aagsolutions.img.nbis.model.fields.ImageField
import eu.aagsolutions.img.nbis.model.fields.TextField
import eu.aagsolutions.img.nbis.model.records.UserDefinedImageRecord

@Suppress("TooManyFunctions")
class UserDefinedImageRecordBuilder :
    NistRecordBuilder<UserDefinedImageRecord, UserDefinedImageRecordBuilder>(
        RecordType.RT7.id,
        RecordType.RT7.label,
        BinaryRecordLengthCalculator(USER_DEFINED_IMAGE_HEADER_SIZE, UserDefinedImageFields.DATA),
    ) {
    override fun build(): UserDefinedImageRecord {
        val lengthField = calculator.calculate(this.id, this.fields)
        this.fields[LENGTH_FIELD_ID] = lengthField
        return UserDefinedImageRecord(this.fields)
    }

    /**
     * Sets IDC (Information Designation Character) – the record sequence number within the transaction.
     *
     * @param designationChar value for the IDC field
     * @return The builder instance for method chaining
     */
    fun withInformationDesignationCharField(designationChar: String) = withField(UserDefinedImageFields.IDC.id, TextField(designationChar))

    fun withImageTypeField(imageType: String) = withField(UserDefinedImageFields.IMT.id, TextField(imageType))

    fun withImageDescriptionField(imageDescription: String) = withField(UserDefinedImageFields.IMD.id, TextField(imageDescription))

    fun withPatternClassificationField(patternClassification: String) =
        withField(UserDefinedImageFields.PCN.id, TextField(patternClassification))

    fun withSecondPatternClassificationField(patternClassification: String) =
        withField(UserDefinedImageFields.PCN2.id, TextField(patternClassification))

    fun withThirdPatternClassificationField(patternClassification: String) =
        withField(UserDefinedImageFields.PCN3.id, TextField(patternClassification))

    fun withFourthPatternClassificationField(patternClassification: String) =
        withField(UserDefinedImageFields.PCN4.id, TextField(patternClassification))

    fun withFifthPatternClassificationField(patternClassification: String) =
        withField(UserDefinedImageFields.PCN5.id, TextField(patternClassification))

    fun withImageCaptureResolutionField(imageCaptureResolution: String) =
        withField(UserDefinedImageFields.IMR.id, TextField(imageCaptureResolution))

    fun withImageCaptureResolutionField2(imageCaptureResolution: String) =
        withField(UserDefinedImageFields.IMR2.id, TextField(imageCaptureResolution))

    fun withImageCaptureResolutionField3(imageCaptureResolution: String) =
        withField(UserDefinedImageFields.IMR3.id, TextField(imageCaptureResolution))

    fun withImageCaptureResolutionField4(imageCaptureResolution: String) =
        withField(UserDefinedImageFields.IMR4.id, TextField(imageCaptureResolution))

    fun withImageCaptureResolutionField5(imageCaptureResolution: String) =
        withField(UserDefinedImageFields.IMR5.id, TextField(imageCaptureResolution))

    fun withImageCaptureResolutionField6(imageCaptureResolution: String) =
        withField(UserDefinedImageFields.IMR6.id, TextField(imageCaptureResolution))

    fun withImageCaptureResolutionField7(imageCaptureResolution: String) =
        withField(UserDefinedImageFields.IMR7.id, TextField(imageCaptureResolution))

    fun withImageCaptureResolutionField8(imageCaptureResolution: String) =
        withField(UserDefinedImageFields.IMR8.id, TextField(imageCaptureResolution))

    fun withImageCaptureResolutionField9(imageCaptureResolution: String) =
        withField(UserDefinedImageFields.IMR9.id, TextField(imageCaptureResolution))

    fun withImageCaptureResolutionField10(imageCaptureResolution: String) =
        withField(UserDefinedImageFields.IMR10.id, TextField(imageCaptureResolution))

    fun withImageCaptureResolutionField11(imageCaptureResolution: String) =
        withField(UserDefinedImageFields.IMR11.id, TextField(imageCaptureResolution))

    /**
     * Sets HLL (Horizontal Line Length) – the number of pixels on a horizontal line.
     *
     * @param horizontalLength value for the HLL field
     * @return The builder instance for method chaining
     */
    fun withHorizontalLineLengthField(horizontalLength: String) = withField(UserDefinedImageFields.HLL.id, TextField(horizontalLength))

    /**
     * Sets VLL (Vertical Line Length) – the number of horizontal lines in the image.
     *
     * @param verticalLength value for the VLL field
     * @return The builder instance for method chaining
     */
    fun withVerticalLineLengthField(verticalLength: String) = withField(UserDefinedImageFields.VLL.id, TextField(verticalLength))

    /**
     * Sets GCA (Greyscale Compression Algorithm) – indicates the algorithm used to compress the image.
     *
     * @param compressionAlgorithm value for the GCA field
     * @return The builder instance for method chaining
     */
    fun withGreyscaleCompressionAlgorithmField(compressionAlgorithm: String) =
        withField(UserDefinedImageFields.GCA.id, TextField(compressionAlgorithm))

    /**
     * Sets DATA (Image Data) – the binary image data.
     *
     * @param imageData the binary image data as ByteArray
     * @return The builder instance for method chaining
     */
    fun withImageDataField(imageData: ByteArray) = withField(UserDefinedImageFields.DATA.id, ImageField(imageData))

    companion object {
        private const val USER_DEFINED_IMAGE_HEADER_SIZE = 33
    }
}
