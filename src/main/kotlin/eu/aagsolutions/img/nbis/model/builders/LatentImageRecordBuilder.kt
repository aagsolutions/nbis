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
import eu.aagsolutions.img.nbis.model.enums.records.ImageFields
import eu.aagsolutions.img.nbis.model.enums.records.LatentImageFields
import eu.aagsolutions.img.nbis.model.fields.ImageField
import eu.aagsolutions.img.nbis.model.fields.TextField
import eu.aagsolutions.img.nbis.model.records.LatentImageRecord
import java.io.ByteArrayInputStream
import javax.imageio.ImageIO

/**
 * Builder class for creating and modifying Type-13 Latent Image records in NIST files.
 *
 * This builder provides methods to set various fields specific to latent fingerprint images,
 * including image capture information, processing parameters, and quality metrics.
 * It supports the creation of records conforming to the ANSI/NIST-ITL 1-2011 standard.
 *
 * The Type-13 record is used for storing and exchanging variable-resolution latent friction ridge
 * image data, such as fingerprints, palm prints, and plantar prints.
 */
@Suppress("TooManyFunctions")
class LatentImageRecordBuilder :
    NistRecordBuilder<LatentImageRecord, LatentImageRecordBuilder>(
        RecordType.RT13.id,
        RecordType.RT13.label,
        TextRecordLengthCalculator(),
    ) {
    /**
     * Builds a new LatentImageRecord with the configured fields.
     *
     * @return A new LatentImageRecord instance containing all configured fields
     */
    override fun build() = LatentImageRecord(this.fields)

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
     * Sets IMP (Impression Type) – indicates the manner by which the latent impression was obtained.
     *
     * @param impressionType The impression type value for the IMP field
     * @return The builder instance for method chaining
     */
    fun withImpressionTypeField(impressionType: String) =
        withField(
            LatentImageFields.IMP.id,
            TextField(impressionType),
        )

    /**
     * Sets SRC (Source Agency) – identifies the administration or organization that originally captured the latent print.
     *
     * @param sourceAgency The source agency identifier
     * @return The builder instance for method chaining
     */
    fun withSourceAgencyField(sourceAgency: String) =
        withField(
            LatentImageFields.SRC.id,
            TextField(sourceAgency),
        )

    /**
     * Sets LCD (Latent Capture Date) – date and time when the latent print was captured.
     *
     * @param latentCaptureDate The capture date in the required format
     * @return The builder instance for method chaining
     */
    fun withLatentCaptureDateField(latentCaptureDate: String) =
        withField(
            LatentImageFields.LCD.id,
            TextField(latentCaptureDate),
        )

    /**
     * Sets HLL (Horizontal Line Length) – the number of pixels contained on a single horizontal line.
     *
     * @param horizontalLineLength The number of pixels per horizontal line
     * @return The builder instance for method chaining
     */
    fun withHorizontalLineLengthField(horizontalLineLength: String) =
        withField(
            LatentImageFields.HLL.id,
            TextField(horizontalLineLength),
        )

    /**
     * Sets VLL (Vertical Line Length) – the number of horizontal lines contained in the image.
     *
     * @param verticalLineLength The number of horizontal lines
     * @return The builder instance for method chaining
     */
    fun withVerticalLineLengthField(verticalLineLength: String) =
        withField(
            LatentImageFields.VLL.id,
            TextField(verticalLineLength),
        )

    /**
     * Sets SLC (Scale Units) – the units used to describe the image sampling frequency (pixel density).
     *
     * @param scaleUnits The scale units code
     * @return The builder instance for method chaining
     */
    fun withScaleUnitsField(scaleUnits: String) =
        withField(
            LatentImageFields.SLC.id,
            TextField(scaleUnits),
        )

    /**
     * Sets THPS (Transmitted Horizontal Pixel Scale) – the horizontal pixel density.
     *
     * @param horizontalPixelScale The horizontal pixel density value
     * @return The builder instance for method chaining
     */
    fun withTransmittedHorizontalPixelScaleField(horizontalPixelScale: String) =
        withField(
            LatentImageFields.THPS.id,
            TextField(horizontalPixelScale),
        )

    /**
     * Sets TVPS (Transmitted Vertical Pixel Scale) – the vertical pixel density.
     *
     * @param verticalPixelScale The vertical pixel density value
     * @return The builder instance for method chaining
     */
    fun withTransmittedVerticalPixelScaleField(verticalPixelScale: String) =
        withField(
            LatentImageFields.TVPS.id,
            TextField(verticalPixelScale),
        )

    /**
     * Sets CGA (Compression Algorithm) – specifies the algorithm used to compress the image data.
     *
     * @param compressionAlgorithm The compression algorithm identifier
     * @return The builder instance for method chaining
     */
    fun withCompressionAlgorithmField(compressionAlgorithm: String) =
        withField(
            LatentImageFields.CGA.id,
            TextField(compressionAlgorithm),
        )

    /**
     * Sets BPX (Bits Per Pixel) – the number of bits used to represent a pixel.
     *
     * @param bitsPerPixel The number of bits per pixel
     * @return The builder instance for method chaining
     */
    fun withBitsPerPixelField(bitsPerPixel: String) =
        withField(
            LatentImageFields.BPX.id,
            TextField(bitsPerPixel),
        )

    /**
     * Sets FGP (Finger/Palm Position) – indicates which finger or palm position is represented.
     *
     * @param fingerPalmPosition The finger or palm position code
     * @return The builder instance for method chaining
     */
    fun withFingerPalmPositionField(fingerPalmPosition: String) =
        withField(
            LatentImageFields.FGP.id,
            TextField(fingerPalmPosition),
        )

    /**
     * Sets SPD (Search Position Descriptors) - describes the set of finger or palm positions that should be searched.
     *
     * @param searchPositionDescriptors The search position descriptors value
     * @return The builder instance for method chaining
     */
    fun withSearchPositionDescriptorsField(searchPositionDescriptors: String) =
        withField(
            LatentImageFields.SPD.id,
            TextField(searchPositionDescriptors),
        )

    /**
     * Sets PPC (Print Position Coordinates) - specifies the location of the latent impression on the surface where it was found.
     *
     * @param printPositionCoordinates The print position coordinates value
     * @return The builder instance for method chaining
     */
    fun withPrintPositionCoordinatesField(printPositionCoordinates: String) =
        withField(
            LatentImageFields.PPC.id,
            TextField(printPositionCoordinates),
        )

    /**
     * Sets SHPS (Scanned Horizontal Pixel Scale) - the horizontal pixel density used during scanning.
     *
     * @param scannedHorizontalPixelScale The scanned horizontal pixel scale value
     * @return The builder instance for method chaining
     */
    fun withScannedHorizontalPixelScaleField(scannedHorizontalPixelScale: String) =
        withField(
            LatentImageFields.SHPS.id,
            TextField(scannedHorizontalPixelScale),
        )

    /**
     * Sets SVPS (Scanned Vertical Pixel Scale) - the vertical pixel density used during scanning.
     *
     * @param scannedVerticalPixelScale The scanned vertical pixel scale value
     * @return The builder instance for method chaining
     */
    fun withScannedVerticalPixelScaleField(scannedVerticalPixelScale: String) =
        withField(
            LatentImageFields.SVPS.id,
            TextField(scannedVerticalPixelScale),
        )

    /**
     * Sets RSP (Ruler or Scale Presence) - indicates whether a ruler or measurement scale was present in the image.
     *
     * @param rulerScalePresence The ruler or scale presence indicator
     * @return The builder instance for method chaining
     */
    fun withRulerScalePresenceField(rulerScalePresence: String) =
        withField(
            LatentImageFields.RSP.id,
            TextField(rulerScalePresence),
        )

    /**
     * Sets REM (Resolution Method) - indicates the method used to determine the resolution of the image.
     *
     * @param resolutionMethod The resolution method code
     * @return The builder instance for method chaining
     */
    fun withResolutionMethodField(resolutionMethod: String) =
        withField(
            LatentImageFields.REM.id,
            TextField(resolutionMethod),
        )

    /**
     * Sets COM (Comment) - additional information about the latent print image.
     *
     * @param comment The comment text
     * @return The builder instance for method chaining
     */
    fun withCommentField(comment: String) =
        withField(
            LatentImageFields.COM.id,
            TextField(comment),
        )

    /**
     * Sets LQM (Latent Quality Metric) - assessment of the overall quality of the latent print image.
     *
     * @param latentQualityMetric The quality metric value
     * @return The builder instance for method chaining
     */
    fun withLatentQualityMetricField(latentQualityMetric: String) =
        withField(
            LatentImageFields.LQM.id,
            TextField(latentQualityMetric),
        )

    /**
     * Sets SUB (Image Subject Condition) - information about the condition of the subject when image was captured.
     *
     * @param imageSubjectCondition The subject condition code
     * @return The builder instance for method chaining
     */
    fun withImageSubjectConditionField(imageSubjectCondition: String) =
        withField(
            LatentImageFields.SUB.id,
            TextField(imageSubjectCondition),
        )

    /**
     * Sets CON (Capture Organization Name) - name of the organization that captured the latent print.
     *
     * @param captureOrganizationName The organization name
     * @return The builder instance for method chaining
     */
    fun withCaptureOrganizationNameField(captureOrganizationName: String) =
        withField(
            LatentImageFields.CON.id,
            TextField(captureOrganizationName),
        )

    /**
     * Sets FCT (Friction Ridge Capture Technology) - method used to capture the original friction ridge image.
     *
     * @param frictionRidgeCapture The capture technology code
     * @return The builder instance for method chaining
     */
    fun withFrictionRidgeCaptureField(frictionRidgeCapture: String) =
        withField(
            LatentImageFields.FCT.id,
            TextField(frictionRidgeCapture),
        )

    /**
     * Sets ANN (Annotation Information) - information about any annotations applied to the image.
     *
     * @param annotationInformation The annotation information
     * @return The builder instance for method chaining
     */
    fun withAnnotationInformationField(annotationInformation: String) =
        withField(
            LatentImageFields.ANN.id,
            TextField(annotationInformation),
        )

    /**
     * Sets DUI (Device Unique Identifier) - identifier for the capture device used to acquire the image.
     *
     * @param deviceUniqueIdentifier The device identifier
     * @return The builder instance for method chaining
     */
    fun withDeviceUniqueIdentifierField(deviceUniqueIdentifier: String) =
        withField(
            LatentImageFields.DUI.id,
            TextField(deviceUniqueIdentifier),
        )

    /**
     * Sets MMS (Make/Model/Serial Number) - information about the capture device make, model and serial number.
     *
     * @param makeModelSerialNumber The device make/model/serial information
     * @return The builder instance for method chaining
     */
    fun withMakeModelSerialNumberField(makeModelSerialNumber: String) =
        withField(
            LatentImageFields.MMS.id,
            TextField(makeModelSerialNumber),
        )

    /**
     * Sets SAN (Source Agency Name) - text name of the agency that provided the latent print.
     *
     * @param sourceAgencyName The agency name
     * @return The builder instance for method chaining
     */
    fun withSourceAgencyNameField(sourceAgencyName: String) =
        withField(
            LatentImageFields.SAN.id,
            TextField(sourceAgencyName),
        )

    /**
     * Sets EFR (External File Reference) - reference to an external file containing additional information.
     *
     * @param externalFileReference The external file reference
     * @return The builder instance for method chaining
     */
    fun withExternalFileReferenceField(externalFileReference: String) =
        withField(
            LatentImageFields.EFR.id,
            TextField(externalFileReference),
        )

    /**
     * Sets ASC (Associated Context) - indicates whether this record is associated with other records.
     *
     * @param associatedContext The associated context information
     * @return The builder instance for method chaining
     */
    fun withAssociatedContextField(associatedContext: String) =
        withField(
            LatentImageFields.ASC.id,
            TextField(associatedContext),
        )

    /**
     * Sets HAS (Hash) - hash value of the image data.
     *
     * @param hash The hash value
     * @return The builder instance for method chaining
     */
    fun withHashField(hash: String) =
        withField(
            LatentImageFields.HAS.id,
            TextField(hash),
        )

    /**
     * Sets SOR (Source Representation) - reference to the source representation from which this record was derived.
     *
     * @param sourceRepresentation The source representation reference
     * @return The builder instance for method chaining
     */
    fun withSourceRepresentationField(sourceRepresentation: String) =
        withField(
            LatentImageFields.SOR.id,
            TextField(sourceRepresentation),
        )

    /**
     * Sets GEO (Geographic Sample Acquisition Location) - describes the location where the latent print was acquired.
     *
     * @param geographicLocation The geographic location information
     * @return The builder instance for method chaining
     */
    fun withGeographicSampleAcquisitionLocationField(geographicLocation: String) =
        withField(
            LatentImageFields.GEO.id,
            TextField(geographicLocation),
        )

    /**
     * Sets DATA (Image Data) - the actual image data in the specified format,
     * height (VLL) and width (HLL) are inferred from the image.
     *
     * @param imageData The binary image data
     * @return The builder instance for method chaining
     */
    fun withImageDataField(imageData: ByteArray): LatentImageRecordBuilder {
        ByteArrayInputStream(imageData).use { inputStream ->
            val bufferedImage = ImageIO.read(inputStream)
            withHorizontalLineLengthField(bufferedImage.width.toString())
            withVerticalLineLengthField(bufferedImage.height.toString())
        }
        return withField(ImageFields.DATA.id, ImageField(imageData))
    }
}
