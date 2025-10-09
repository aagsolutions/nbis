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
import eu.aagsolutions.img.nbis.model.enums.records.FacialAndSMTImageFields
import eu.aagsolutions.img.nbis.model.fields.TextField
import eu.aagsolutions.img.nbis.model.records.FacialAndSMTImageRecord

/**
 * Builder class for creating FacialAndSMTImageRecord instances.
 * Handles construction of Type-10 records containing facial and SMT (Scars, Marks, Tattoos) image data.
 */
@Suppress("TooManyFunctions")
class FacialAndSMTImageRecordBuilder :
    TextRecordWithImageBuilder<FacialAndSMTImageRecord, FacialAndSMTImageRecordBuilder>(
        RecordType.RT10.id,
        RecordType.RT10.label,
        TextRecordLengthCalculator(),
    ) {
    /**
     * Builds and returns a FacialAndSMTImageRecord instance with all configured fields.
     *
     * @return FacialAndSMTImageRecord The constructed facial/SMT image record
     */
    override fun build() = FacialAndSMTImageRecord(this.fields)

    /**
     * Sets IDC (Information Designation Character) field.
     *
     * @param designationChar The information designation character value
     * @return FacialAndSMTImageRecordBuilder for method chaining
     */
    fun withInformationDesignationCharField(designationChar: String) =
        withField(
            FacialAndSMTImageFields.IDC.id,
            TextField(designationChar),
        )

    /**
     * Sets IMT (Image Type) field.
     *
     * @param imageType The type of image being transmitted
     * @return FacialAndSMTImageRecordBuilder for method chaining
     */
    fun withImageTypeField(imageType: String) =
        withField(
            FacialAndSMTImageFields.IMT.id,
            TextField(imageType),
        )

    /**
     * Sets SRC (Source Agency ORI) field.
     *
     * @param sourceAgencyOri The identifier of the originating agency
     * @return FacialAndSMTImageRecordBuilder for method chaining
     */
    fun withSourceAgencyOriField(sourceAgencyOri: String) =
        withField(
            FacialAndSMTImageFields.SRC.id,
            TextField(sourceAgencyOri),
        )

    /**
     * Sets PHD (Photo Date) field.
     *
     * @param photoDate The date when the photo was taken
     * @return FacialAndSMTImageRecordBuilder for method chaining
     */
    fun withPhotoDateField(photoDate: String) =
        withField(
            FacialAndSMTImageFields.PHD.id,
            TextField(photoDate),
        )

    /**
     * Sets HLL (Horizontal Line Length) field.
     *
     * @param horizontalLineLength The number of pixels on each horizontal line
     * @return FacialAndSMTImageRecordBuilder for method chaining
     */
    override fun withHorizontalLineLengthField(horizontalLineLength: String) =
        withField(
            FacialAndSMTImageFields.HLL.id,
            TextField(horizontalLineLength),
        )

    /**
     * Sets VLL (Vertical Line Length) field.
     *
     * @param verticalLineLength The number of horizontal pixel rows
     * @return FacialAndSMTImageRecordBuilder for method chaining
     */
    override fun withVerticalLineLengthField(verticalLineLength: String) =
        withField(
            FacialAndSMTImageFields.VLL.id,
            TextField(verticalLineLength),
        )

    /**
     * Sets SLC (Scale Units) field.
     *
     * @param scaleUnits The units used to describe the image sampling frequency
     * @return FacialAndSMTImageRecordBuilder for method chaining
     */
    fun withScaleUnitsField(scaleUnits: String) =
        withField(
            FacialAndSMTImageFields.SLC.id,
            TextField(scaleUnits),
        )

    /**
     * Sets HPS (Horizontal Pixel Scale) legacy field.
     *
     * @param horizontalPixelScale The horizontal pixel density
     * @return FacialAndSMTImageRecordBuilder for method chaining
     */
    fun withHorizontalPixelScaleLegacyField(horizontalPixelScale: String) =
        withField(
            FacialAndSMTImageFields.HPS_LEGACY.id,
            TextField(horizontalPixelScale),
        )

    /**
     * Sets THPS (Transmitted Horizontal Pixel Scale) field.
     *
     * @param horizontalPixelScale The transmitted horizontal pixel density
     * @return FacialAndSMTImageRecordBuilder for method chaining
     */
    override fun withTransmittedHorizontalPixelScaleField(horizontalPixelScale: String) =
        withField(
            FacialAndSMTImageFields.THPS.id,
            TextField(horizontalPixelScale),
        )

    /**
     * Sets VPS (Vertical Pixel Scale) legacy field.
     *
     * @param verticalPixelScale The vertical pixel density
     * @return FacialAndSMTImageRecordBuilder for method chaining
     */
    fun withVerticalPixelScaleLegacyField(verticalPixelScale: String) =
        withField(
            FacialAndSMTImageFields.VPS_LEGACY.id,
            TextField(verticalPixelScale),
        )

    /**
     * Sets TVPS (Transmitted Vertical Pixel Scale) field.
     *
     * @param verticalPixelScale The transmitted vertical pixel density
     * @return FacialAndSMTImageRecordBuilder for method chaining
     */
    override fun withTransmittedVerticalPixelScaleField(verticalPixelScale: String) =
        withField(
            FacialAndSMTImageFields.TVPS.id,
            TextField(verticalPixelScale),
        )

    /**
     * Sets CGA (Compression Algorithm) field.
     *
     * @param compressionAlgorithm The algorithm used to compress the image data
     * @return FacialAndSMTImageRecordBuilder for method chaining
     */
    override fun withCompressionAlgorithmField(compressionAlgorithm: String) =
        withField(
            FacialAndSMTImageFields.CGA.id,
            TextField(compressionAlgorithm),
        )

    /**
     * Sets CSP (Color Space) field.
     *
     * @param colorSpace The color space of the image
     * @return FacialAndSMTImageRecordBuilder for method chaining
     */
    override fun withColorSpaceField(colorSpace: String) =
        withField(
            FacialAndSMTImageFields.CSP.id,
            TextField(colorSpace),
        )

    /**
     * Sets SAP (Subject Acquisition Profile) field.
     *
     * @param subjectAcquisitionProfile The SAP level of the facial image
     * @return FacialAndSMTImageRecordBuilder for method chaining
     */
    fun withSubjectAcquisitionProfileField(subjectAcquisitionProfile: String) =
        withField(
            FacialAndSMTImageFields.SAP.id,
            TextField(subjectAcquisitionProfile),
        )

    /**
     * Sets FIP (Facial Image Bounding Box Coordinates) field.
     *
     * @param boundingBoxCoordinates The coordinates of facial image bounding box in full image
     * @return FacialAndSMTImageRecordBuilder for method chaining
     */
    fun withFacialImageBoundingBoxCoordinatesInFullImageField(boundingBoxCoordinates: String) =
        withField(
            FacialAndSMTImageFields.FIP.id,
            TextField(boundingBoxCoordinates),
        )

    /**
     * Sets FPFI (Face Image Path Coordinates) field.
     *
     * @param faceImagePathCoordinates The coordinates of face image path in full image
     * @return FacialAndSMTImageRecordBuilder for method chaining
     */
    fun withFaceImagePathCoordinatesInFullImageField(faceImagePathCoordinates: String) =
        withField(
            FacialAndSMTImageFields.FPFI.id,
            TextField(faceImagePathCoordinates),
        )

    /**
     * Sets SHPS (Scan Horizontal Pixel Scale) field.
     *
     * @param scanHorizontalPixelScale The horizontal pixel density from scanning
     * @return FacialAndSMTImageRecordBuilder for method chaining
     */
    fun withScanHorizontalPixelScaleField(scanHorizontalPixelScale: String) =
        withField(
            FacialAndSMTImageFields.SHPS.id,
            TextField(scanHorizontalPixelScale),
        )

    /**
     * Sets SVPS (Scan Vertical Pixel Scale) field.
     *
     * @param scanVerticalPixelScale The vertical pixel density from scanning
     * @return FacialAndSMTImageRecordBuilder for method chaining
     */
    fun withScanVerticalPixelScaleField(scanVerticalPixelScale: String) =
        withField(
            FacialAndSMTImageFields.SVPS.id,
            TextField(scanVerticalPixelScale),
        )

    /**
     * Sets DIST (Distortion) field.
     *
     * @param distortion The distortion value present in image
     * @return FacialAndSMTImageRecordBuilder for method chaining
     */
    fun withDistortionField(distortion: String) =
        withField(
            FacialAndSMTImageFields.DIST.id,
            TextField(distortion),
        )

    /**
     * Sets LAF (Lighting Artifacts) field.
     *
     * @param lightingArtifacts The lighting artifacts present in image
     * @return FacialAndSMTImageRecordBuilder for method chaining
     */
    fun withLightingArtifactsField(lightingArtifacts: String) =
        withField(
            FacialAndSMTImageFields.LAF.id,
            TextField(lightingArtifacts),
        )

    /**
     * Sets POS (Subject Pose) field.
     *
     * @param subjectPose The pose angle of the subject
     * @return FacialAndSMTImageRecordBuilder for method chaining
     */
    fun withSubjectPoseField(subjectPose: String) =
        withField(
            FacialAndSMTImageFields.POS.id,
            TextField(subjectPose),
        )

    /**
     * Sets POA (Pose Offset Angle) field.
     *
     * @param poseOffsetAngle The offset angle of the subject's pose
     * @return FacialAndSMTImageRecordBuilder for method chaining
     */
    fun withPoseOffsetAngleField(poseOffsetAngle: String) =
        withField(
            FacialAndSMTImageFields.POA.id,
            TextField(poseOffsetAngle),
        )

    /**
     * Sets PXS (Photo Designation) legacy field.
     *
     * @param photoDesignation The legacy photo designation code
     * @return FacialAndSMTImageRecordBuilder for method chaining
     */
    fun withPhotoDesignationLegacyField(photoDesignation: String) =
        withField(
            FacialAndSMTImageFields.PXS_LEGACY.id,
            TextField(photoDesignation),
        )

    /**
     * Sets PAS (Photo Acquisition Source) field.
     *
     * @param photoAcquisitionSource The source/method used to acquire the photo
     * @return FacialAndSMTImageRecordBuilder for method chaining
     */
    fun withPhotoAcquisitionSourceField(photoAcquisitionSource: String) =
        withField(
            FacialAndSMTImageFields.PAS.id,
            TextField(photoAcquisitionSource),
        )

    /**
     * Sets SQS (Subject Quality Score) field.
     *
     * @param subjectQualityScore The quality score of the subject image
     * @return FacialAndSMTImageRecordBuilder for method chaining
     */
    fun withSubjectQualityScoreField(subjectQualityScore: String) =
        withField(
            FacialAndSMTImageFields.SQS.id,
            TextField(subjectQualityScore),
        )

    /**
     * Sets SPA (Subject Pose Angles) field.
     *
     * @param subjectPoseAngles The angles of the subject's pose
     * @return FacialAndSMTImageRecordBuilder for method chaining
     */
    fun withSubjectPoseAnglesField(subjectPoseAngles: String) =
        withField(
            FacialAndSMTImageFields.SPA.id,
            TextField(subjectPoseAngles),
        )

    /**
     * Sets SXS (Subject Facial Description) field.
     *
     * @param subjectFacialDescription The description of subject's facial features
     * @return FacialAndSMTImageRecordBuilder for method chaining
     */
    fun withSubjectFacialDescriptionField(subjectFacialDescription: String) =
        withField(
            FacialAndSMTImageFields.SXS.id,
            TextField(subjectFacialDescription),
        )

    /**
     * Sets SEC (Subject Eye Color) field.
     *
     * @param subjectEyeColor The color of subject's eyes
     * @return FacialAndSMTImageRecordBuilder for method chaining
     */
    fun withSubjectEyeColorField(subjectEyeColor: String) =
        withField(
            FacialAndSMTImageFields.SEC.id,
            TextField(subjectEyeColor),
        )

    /**
     * Sets SHC (Subject Hair Color) field.
     *
     * @param subjectHairColor The color of subject's hair
     * @return FacialAndSMTImageRecordBuilder for method chaining
     */
    fun withSubjectHairColorField(subjectHairColor: String) =
        withField(
            FacialAndSMTImageFields.SHC.id,
            TextField(subjectHairColor),
        )

    /**
     * Sets SFP (Subject Feature Points) legacy field.
     *
     * @param facialFeaturePointsLegacy The legacy facial feature points
     * @return FacialAndSMTImageRecordBuilder for method chaining
     */
    fun withFacialFeaturePointsLegacyField(facialFeaturePointsLegacy: String) =
        withField(
            FacialAndSMTImageFields.SFP_LEGACY.id,
            TextField(facialFeaturePointsLegacy),
        )

    /**
     * Sets FFP (2D Facial Feature Points) field.
     *
     * @param facialFeaturePoints2D The 2D facial feature points
     * @return FacialAndSMTImageRecordBuilder for method chaining
     */
    fun with2DFacialFeaturePointsField(facialFeaturePoints2D: String) =
        withField(
            FacialAndSMTImageFields.FFP.id,
            TextField(facialFeaturePoints2D),
        )

    /**
     * Sets DMM (Device Monitoring Mode) field.
     *
     * @param deviceMonitoringMode The monitoring mode of the capture device
     * @return FacialAndSMTImageRecordBuilder for method chaining
     */
    fun withDeviceMonitoringModeField(deviceMonitoringMode: String) =
        withField(
            FacialAndSMTImageFields.DMM.id,
            TextField(deviceMonitoringMode),
        )

    /**
     * Sets TMC (Tiered Markup Collection) field.
     *
     * @param tieredMarkupCollection The tiered markup collection data
     * @return FacialAndSMTImageRecordBuilder for method chaining
     */
    fun withTieredMarkupCollectionField(tieredMarkupCollection: String) =
        withField(
            FacialAndSMTImageFields.TMC.id,
            TextField(tieredMarkupCollection),
        )

    /**
     * Sets 3DF (3D Facial Feature Points) field.
     *
     * @param facialFeaturePoints3D The 3D facial feature points
     * @return FacialAndSMTImageRecordBuilder for method chaining
     */
    fun with3DFacialFeaturePointsField(facialFeaturePoints3D: String) =
        withField(
            FacialAndSMTImageFields.THREEDF.id,
            TextField(facialFeaturePoints3D),
        )

    /**
     * Sets FEC (Feature Contours) field.
     *
     * @param featureContours The facial feature contours data
     * @return FacialAndSMTImageRecordBuilder for method chaining
     */
    fun withFeatureContoursField(featureContours: String) =
        withField(
            FacialAndSMTImageFields.FEC.id,
            TextField(featureContours),
        )

    /**
     * Sets ICDR (Image Capture Date Range Estimate) field.
     *
     * @param captureDateRangeEstimate The estimated date range of image capture
     * @return FacialAndSMTImageRecordBuilder for method chaining
     */
    fun withImageCaptureDateRangeEstimateField(captureDateRangeEstimate: String) =
        withField(
            FacialAndSMTImageFields.ICDR.id,
            TextField(captureDateRangeEstimate),
        )

    /**
     * Sets COM (Comment) field.
     *
     * @param comment The comment text
     * @return FacialAndSMTImageRecordBuilder for method chaining
     */
    fun withCommentField(comment: String) =
        withField(
            FacialAndSMTImageFields.COM.id,
            TextField(comment),
        )

    /**
     * Sets T10 (Type-10 Reference Number) field.
     *
     * @param type10ReferenceNumber The reference number for Type-10 record
     * @return FacialAndSMTImageRecordBuilder for method chaining
     */
    fun withType10ReferenceNumberField(type10ReferenceNumber: String) =
        withField(
            FacialAndSMTImageFields.T10.id,
            TextField(type10ReferenceNumber),
        )

    /**
     * Sets SMT (NCIC Designation Code) field.
     *
     * @param ncicDesignationCode The NCIC SMT code
     * @return FacialAndSMTImageRecordBuilder for method chaining
     */
    fun withNcicDesignationCodeField(ncicDesignationCode: String) =
        withField(
            FacialAndSMTImageFields.SMT.id,
            TextField(ncicDesignationCode),
        )

    /**
     * Sets SMS (Scar/Mark/Tattoo Size) field.
     *
     * @param scarMarkTattooSize The size of scar, mark or tattoo
     * @return FacialAndSMTImageRecordBuilder for method chaining
     */
    fun withScarMarkTattooSizeField(scarMarkTattooSize: String) =
        withField(
            FacialAndSMTImageFields.SMS.id,
            TextField(scarMarkTattooSize),
        )

    /**
     * Sets SMD (SMT Descriptors) field.
     *
     * @param smtDescriptors The descriptors for scars, marks and tattoos
     * @return FacialAndSMTImageRecordBuilder for method chaining
     */
    fun withSmtDescriptorsField(smtDescriptors: String) =
        withField(
            FacialAndSMTImageFields.SMD.id,
            TextField(smtDescriptors),
        )

    /**
     * Sets COL (Colors Present) field.
     *
     * @param colorsPresent The colors present in the image
     * @return FacialAndSMTImageRecordBuilder for method chaining
     */
    fun withColorsPresentField(colorsPresent: String) =
        withField(
            FacialAndSMTImageFields.COL.id,
            TextField(colorsPresent),
        )

    /**
     * Sets ITX (Image Transformation) field.
     *
     * @param imageTransformation The transformation applied to the image
     * @return FacialAndSMTImageRecordBuilder for method chaining
     */
    fun withImageTransformationField(imageTransformation: String) =
        withField(
            FacialAndSMTImageFields.ITX.id,
            TextField(imageTransformation),
        )

    /**
     * Sets OCC (Occlusions) field.
     *
     * @param occlusions The occlusions present in the image
     * @return FacialAndSMTImageRecordBuilder for method chaining
     */
    fun withOcclusionsField(occlusions: String) =
        withField(
            FacialAndSMTImageFields.OCC.id,
            TextField(occlusions),
        )

    /**
     * Sets SUB (Image Subject Condition) field.
     *
     * @param imageSubjectCondition The condition of the subject in the image
     * @return FacialAndSMTImageRecordBuilder for method chaining
     */
    fun withImageSubjectConditionField(imageSubjectCondition: String) =
        withField(
            FacialAndSMTImageFields.SUB.id,
            TextField(imageSubjectCondition),
        )

    /**
     * Sets CON (Capture Organization Name) field.
     *
     * @param captureOrganizationName The name of organization that captured the image
     * @return FacialAndSMTImageRecordBuilder for method chaining
     */
    fun withCaptureOrganizationNameField(captureOrganizationName: String) =
        withField(
            FacialAndSMTImageFields.CON.id,
            TextField(captureOrganizationName),
        )

    /**
     * Sets PID (Suspected Patterned Injury Detail) field.
     *
     * @param suspectedPatternedInjuryDetail The details of suspected patterned injury
     * @return FacialAndSMTImageRecordBuilder for method chaining
     */
    fun withSuspectedPatternedInjuryDetailField(suspectedPatternedInjuryDetail: String) =
        withField(
            FacialAndSMTImageFields.PID.id,
            TextField(suspectedPatternedInjuryDetail),
        )

    /**
     * Sets CID (Cheiloscopic Image Description) field.
     *
     * @param cheiloscopicImageDescription The description of lip print characteristics
     * @return FacialAndSMTImageRecordBuilder for method chaining
     */
    fun withCheiloscopicImageDescriptionField(cheiloscopicImageDescription: String) =
        withField(
            FacialAndSMTImageFields.CID.id,
            TextField(cheiloscopicImageDescription),
        )

    /**
     * Sets VID (Dental Visual Image Data Information) field.
     *
     * @param dentalVisualImageDataInformation The dental visual image information
     * @return FacialAndSMTImageRecordBuilder for method chaining
     */
    fun withDentalVisualImageDataInformationField(dentalVisualImageDataInformation: String) =
        withField(
            FacialAndSMTImageFields.VID.id,
            TextField(dentalVisualImageDataInformation),
        )

    /**
     * Sets RSP (Ruler or Scale Presence) field.
     *
     * @param rulerOrScalePresence Indicates if ruler/scale is present in image
     * @return FacialAndSMTImageRecordBuilder for method chaining
     */
    fun withRulerOrScalePresenceField(rulerOrScalePresence: String) =
        withField(
            FacialAndSMTImageFields.RSP.id,
            TextField(rulerOrScalePresence),
        )

    /**
     * Sets ANN (Annotation Information) field.
     *
     * @param annotationInformation The annotation information for the image
     * @return FacialAndSMTImageRecordBuilder for method chaining
     */
    fun withAnnotationInformationField(annotationInformation: String) =
        withField(
            FacialAndSMTImageFields.ANN.id,
            TextField(annotationInformation),
        )

    /**
     * Sets DUI (Device Unique Identifier) field.
     *
     * @param deviceUniqueIdentifier The unique identifier of capture device
     * @return FacialAndSMTImageRecordBuilder for method chaining
     */
    fun withDeviceUniqueIdentifierField(deviceUniqueIdentifier: String) =
        withField(
            FacialAndSMTImageFields.DUI.id,
            TextField(deviceUniqueIdentifier),
        )

    /**
     * Sets MMS (Make Model Serial Number) field.
     *
     * @param makeModelSerialNumber The make/model/serial number of capture device
     * @return FacialAndSMTImageRecordBuilder for method chaining
     */
    fun withMakeModelSerialNumberField(makeModelSerialNumber: String) =
        withField(
            FacialAndSMTImageFields.MMS.id,
            TextField(makeModelSerialNumber),
        )

    /**
     * Sets T2C (Type-2 Record Cross Reference) field.
     *
     * @param type2RecordCrossReference The cross reference to Type-2 record
     * @return FacialAndSMTImageRecordBuilder for method chaining
     */
    fun withType2RecordCrossReferenceField(type2RecordCrossReference: String) =
        withField(
            FacialAndSMTImageFields.T2C.id,
            TextField(type2RecordCrossReference),
        )

    /**
     * Sets SAN (Source Agency Name) field.
     *
     * @param sourceAgencyName The name of the source agency
     * @return FacialAndSMTImageRecordBuilder for method chaining
     */
    fun withSourceAgencyNameField(sourceAgencyName: String) =
        withField(
            FacialAndSMTImageFields.SAN.id,
            TextField(sourceAgencyName),
        )

    /**
     * Sets EFR (External File Reference) field.
     *
     * @param externalFileReference The reference to external file
     * @return FacialAndSMTImageRecordBuilder for method chaining
     */
    fun withExternalFileReferenceField(externalFileReference: String) =
        withField(
            FacialAndSMTImageFields.EFR.id,
            TextField(externalFileReference),
        )

    /**
     * Sets ASC (Associated Context) field.
     *
     * @param associatedContext The context associated with the image
     * @return FacialAndSMTImageRecordBuilder for method chaining
     */
    fun withAssociatedContextField(associatedContext: String) =
        withField(
            FacialAndSMTImageFields.ASC.id,
            TextField(associatedContext),
        )

    /**
     * Sets HAS (Hash) field.
     *
     * @param hash The hash value of the image data
     * @return FacialAndSMTImageRecordBuilder for method chaining
     */
    fun withHashField(hash: String) =
        withField(
            FacialAndSMTImageFields.HAS.id,
            TextField(hash),
        )

    /**
     * Sets SOR (Source Representation) field.
     *
     * @param sourceRepresentation The source representation information
     * @return FacialAndSMTImageRecordBuilder for method chaining
     */
    fun withSourceRepresentationField(sourceRepresentation: String) =
        withField(
            FacialAndSMTImageFields.SOR.id,
            TextField(sourceRepresentation),
        )

    /**
     * Sets GEO (Geographic Sample Acquisition Location) field.
     *
     * @param geographicSampleAcquisitionLocation The geographic location where image was acquired
     * @return FacialAndSMTImageRecordBuilder for method chaining
     */
    fun withGeographicSampleAcquisitionLocationField(geographicSampleAcquisitionLocation: String) =
        withField(
            FacialAndSMTImageFields.GEO.id,
            TextField(geographicSampleAcquisitionLocation),
        )
}
