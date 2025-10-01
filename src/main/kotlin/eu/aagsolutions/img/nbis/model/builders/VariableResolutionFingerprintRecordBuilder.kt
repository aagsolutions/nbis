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
import eu.aagsolutions.img.nbis.model.enums.records.VariableResolutionFingerprintImageFields
import eu.aagsolutions.img.nbis.model.fields.TextField
import eu.aagsolutions.img.nbis.model.records.VariableResolutionFingerprintRecord

/**
 * Builder class for creating and modifying variable-resolution fingerprint-based NIST records.
 */
@Suppress("TooManyFunctions")
class VariableResolutionFingerprintRecordBuilder :
    TextRecordWithImageBuilder<VariableResolutionFingerprintRecord, VariableResolutionFingerprintRecordBuilder>(
        RecordType.RT14.id,
        RecordType.RT14.label,
        TextRecordLengthCalculator(),
    ) {
    override fun build() = VariableResolutionFingerprintRecord(this.fields)

    /**
     * Sets IDC (Information Designation Character) – uniquely identifies the record within the transaction.
     *
     * @param designationChar The designation character value for the IDC field
     * @return The builder instance for method chaining
     */
    fun withInformationDesignationCharField(designationChar: String) =
        withField(
            VariableResolutionFingerprintImageFields.IDC.id,
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
            VariableResolutionFingerprintImageFields.IMP.id,
            TextField(impressionType),
        )

    /**
     * Sets SRC (Source Agency) – identifies the administration or organization that originally captured the fingerprint.
     *
     * @param sourceAgency The source agency identifier
     * @return The builder instance for method chaining
     */
    fun withSourceAgencyField(sourceAgency: String) =
        withField(
            VariableResolutionFingerprintImageFields.SRC.id,
            TextField(sourceAgency),
        )

    /**
     * Sets FCD (Fingerprint Capture Date) – date and time when the fingerprint was captured.
     *
     * @param fingerprintDate The capture date in the required format
     * @return The builder instance for method chaining
     */
    fun withLatentCaptureDateField(fingerprintDate: String) =
        withField(
            VariableResolutionFingerprintImageFields.FCD.id,
            TextField(fingerprintDate),
        )

    /**
     * Sets HLL (Horizontal Line Length) – the number of pixels contained on a single horizontal line.
     *
     * @param horizontalLineLength The number of pixels per horizontal line
     * @return The builder instance for method chaining
     */
    override fun withHorizontalLineLengthField(horizontalLineLength: String) =
        withField(
            VariableResolutionFingerprintImageFields.HLL.id,
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
            VariableResolutionFingerprintImageFields.VLL.id,
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
            VariableResolutionFingerprintImageFields.SLC.id,
            TextField(scaleUnits),
        )

    /**
     * Sets THPS (Transmitted Horizontal Pixel Scale) – the horizontal pixel density.
     *
     * @param horizontalPixelScale The horizontal pixel density value
     * @return The builder instance for method chaining
     */
    override fun withTransmittedHorizontalPixelScaleField(horizontalPixelScale: String) =
        withField(
            VariableResolutionFingerprintImageFields.THPS.id,
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
            VariableResolutionFingerprintImageFields.TVPS.id,
            TextField(verticalPixelScale),
        )

    /**
     * Sets CGA (Compression Algorithm) – specifies the algorithm used to compress the image data.
     *
     * @param compressionAlgorithm The compression algorithm identifier
     * @return The builder instance for method chaining
     */
    override fun withCompressionAlgorithmField(compressionAlgorithm: String) =
        withField(
            VariableResolutionFingerprintImageFields.CGA.id,
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
            VariableResolutionFingerprintImageFields.BPX.id,
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
            VariableResolutionFingerprintImageFields.FGP.id,
            TextField(fingerPalmPosition),
        )

    /**
     * Configures the field with the provided print position descriptors value.
     *
     * @param printPositionDescriptors the value to be set for the print position descriptors field
     * @return The builder instance for method chaining
     */
    fun withPrintPositionDescriptorsField(printPositionDescriptors: String) =
        withField(
            VariableResolutionFingerprintImageFields.PPD.id,
            TextField(printPositionDescriptors),
        )

    /**
     * Sets PPC (Print Position Coordinates) - specifies the location of the latent impression on the surface where it was found.
     *
     * @param printPositionCoordinates The print position coordinates value
     * @return The builder instance for method chaining
     */
    fun withPrintPositionCoordinatesField(printPositionCoordinates: String) =
        withField(
            VariableResolutionFingerprintImageFields.PPC.id,
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
            VariableResolutionFingerprintImageFields.SHPS.id,
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
            VariableResolutionFingerprintImageFields.SVPS.id,
            TextField(scannedVerticalPixelScale),
        )

    /**
     * Sets AMP (Amputation) - indicates whether the subject was amputated or bandaged during the acquisition.
     *
     * @param amputatedOrBandaged The amputation or bandage code
     * @return The builder instance for method chaining
     */
    fun withAmputatedOrBandagedField(amputatedOrBandaged: String) =
        withField(
            VariableResolutionFingerprintImageFields.AMP.id,
            TextField(amputatedOrBandaged),
        )

    /**
     * Sets COM (Comment) - additional information about the latent print image.
     *
     * @param comment The comment text
     * @return The builder instance for method chaining
     */
    fun withCommentField(comment: String) =
        withField(
            VariableResolutionFingerprintImageFields.COM.id,
            TextField(comment),
        )

    fun withSegmentationPositionFieldField(segmentationPosition: String) =
        withField(
            VariableResolutionFingerprintImageFields.SEG.id,
            TextField(segmentationPosition),
        )

    /**
     * Sets SUB (Image Subject Condition) - information about the condition of the subject when image was captured.
     *
     * @param imageSubjectCondition The subject condition code
     * @return The builder instance for method chaining
     */
    fun withImageSubjectConditionField(imageSubjectCondition: String) =
        withField(
            VariableResolutionFingerprintImageFields.SUB.id,
            TextField(imageSubjectCondition),
        )

    /**
     * Sets CON (Capture Organization Name) - the name of the organization that captured the fingerprint.
     *
     * @param captureOrganizationName The organization name
     * @return The builder instance for method chaining
     */
    fun withCaptureOrganizationNameField(captureOrganizationName: String) =
        withField(
            VariableResolutionFingerprintImageFields.CON.id,
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
            VariableResolutionFingerprintImageFields.FCT.id,
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
            VariableResolutionFingerprintImageFields.ANN.id,
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
            VariableResolutionFingerprintImageFields.DUI.id,
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
            VariableResolutionFingerprintImageFields.MMS.id,
            TextField(makeModelSerialNumber),
        )

    /**
     * Sets SAN (Source Agency Name) - the text name of the agency that provided the latent print.
     *
     * @param sourceAgencyName The agency name
     * @return The builder instance for method chaining
     */
    fun withSourceAgencyNameField(sourceAgencyName: String) =
        withField(
            VariableResolutionFingerprintImageFields.SAN.id,
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
            VariableResolutionFingerprintImageFields.EFR.id,
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
            VariableResolutionFingerprintImageFields.ASC.id,
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
            VariableResolutionFingerprintImageFields.HAS.id,
            TextField(hash),
        )

    /**
     * Sets SOR (Source Representation) - a reference to the source representation from which this record was derived.
     *
     * @param sourceRepresentation The source representation reference
     * @return The builder instance for method chaining
     */
    fun withSourceRepresentationField(sourceRepresentation: String) =
        withField(
            VariableResolutionFingerprintImageFields.SOR.id,
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
            VariableResolutionFingerprintImageFields.GEO.id,
            TextField(geographicLocation),
        )
}
