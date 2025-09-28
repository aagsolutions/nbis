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
import eu.aagsolutions.img.nbis.model.fields.ImageField
import eu.aagsolutions.img.nbis.model.fields.TextField
import eu.aagsolutions.img.nbis.model.records.FacialAndSMTImageRecord

@Suppress("TooManyFunctions")
class FacialAndSMTImageRecordBuilder :
    NistRecordBuilder<FacialAndSMTImageRecord, FacialAndSMTImageRecordBuilder>(
        RecordType.RT10.id,
        RecordType.RT10.label,
        TextRecordLengthCalculator(),
    ) {
    override fun build(): FacialAndSMTImageRecord {
        val lengthField = calculator.calculate(this.id, this.fields)
        this.fields[LENGTH_FIELD_ID] = lengthField
        return FacialAndSMTImageRecord(this.fields)
    }

    fun withInformationDesignationCharField(designationChar: String) =
        withField(
            FacialAndSMTImageFields.IDC.id,
            TextField(designationChar),
        )

    fun withImageTypeField(imageType: String) =
        withField(
            FacialAndSMTImageFields.IMT.id,
            TextField(imageType),
        )

    fun withSourceAgencyOriField(sourceAgencyOri: String) =
        withField(
            FacialAndSMTImageFields.SRC.id,
            TextField(sourceAgencyOri),
        )

    fun withPhotoDateField(photoDate: String) =
        withField(
            FacialAndSMTImageFields.PHD.id,
            TextField(photoDate),
        )

    fun withHorizontalLineLengthField(horizontalLineLength: String) =
        withField(
            FacialAndSMTImageFields.HLL.id,
            TextField(horizontalLineLength),
        )

    fun withVerticalLineLengthField(verticalLineLength: String) =
        withField(
            FacialAndSMTImageFields.VLL.id,
            TextField(verticalLineLength),
        )

    fun withScaleUnitsField(scaleUnits: String) =
        withField(
            FacialAndSMTImageFields.SLC.id,
            TextField(scaleUnits),
        )

    fun withHorizontalPixelScaleLegacyField(horizontalPixelScale: String) =
        withField(
            FacialAndSMTImageFields.HPS_LEGACY.id,
            TextField(horizontalPixelScale),
        )

    fun withTransmittedHorizontalPixelScaleField(transmittedHorizontalPixelScale: String) =
        withField(
            FacialAndSMTImageFields.THPS.id,
            TextField(transmittedHorizontalPixelScale),
        )

    fun withVerticalPixelScaleLegacyField(verticalPixelScale: String) =
        withField(
            FacialAndSMTImageFields.VPS_LEGACY.id,
            TextField(verticalPixelScale),
        )

    fun withTransmittedVerticalPixelScaleField(transmittedVerticalPixelScale: String) =
        withField(
            FacialAndSMTImageFields.TVPS.id,
            TextField(transmittedVerticalPixelScale),
        )

    fun withCompressionAlgorithmField(compressionAlgorithm: String) =
        withField(
            FacialAndSMTImageFields.CGA.id,
            TextField(compressionAlgorithm),
        )

    fun withColorSpaceField(colorSpace: String) =
        withField(
            FacialAndSMTImageFields.CSP.id,
            TextField(colorSpace),
        )

    fun withSubjectAcquisitionProfileField(subjectAcquisitionProfile: String) =
        withField(
            FacialAndSMTImageFields.SAP.id,
            TextField(subjectAcquisitionProfile),
        )

    fun withFacialImageBoundingBoxCoordinatesInFullImageField(boundingBoxCoordinates: String) =
        withField(
            FacialAndSMTImageFields.FIP.id,
            TextField(boundingBoxCoordinates),
        )

    fun withFaceImagePathCoordinatesInFullImageField(faceImagePathCoordinates: String) =
        withField(
            FacialAndSMTImageFields.FPFI.id,
            TextField(faceImagePathCoordinates),
        )

    fun withScanHorizontalPixelScaleField(scanHorizontalPixelScale: String) =
        withField(
            FacialAndSMTImageFields.SHPS.id,
            TextField(scanHorizontalPixelScale),
        )

    fun withScanVerticalPixelScaleField(scanVerticalPixelScale: String) =
        withField(
            FacialAndSMTImageFields.SVPS.id,
            TextField(scanVerticalPixelScale),
        )

    fun withDistortionField(distortion: String) =
        withField(
            FacialAndSMTImageFields.DIST.id,
            TextField(distortion),
        )

    fun withLightingArtifactsField(lightingArtifacts: String) =
        withField(
            FacialAndSMTImageFields.LAF.id,
            TextField(lightingArtifacts),
        )

    fun withSubjectPoseField(subjectPose: String) =
        withField(
            FacialAndSMTImageFields.POS.id,
            TextField(subjectPose),
        )

    fun withPoseOffsetAngleField(poseOffsetAngle: String) =
        withField(
            FacialAndSMTImageFields.POA.id,
            TextField(poseOffsetAngle),
        )

    fun withPhotoDesignationLegacyField(photoDesignation: String) =
        withField(
            FacialAndSMTImageFields.PXS_LEGACY.id,
            TextField(photoDesignation),
        )

    fun withPhotoAcquisitionSourceField(photoAcquisitionSource: String) =
        withField(
            FacialAndSMTImageFields.PAS.id,
            TextField(photoAcquisitionSource),
        )

    fun withSubjectQualityScoreField(subjectQualityScore: String) =
        withField(
            FacialAndSMTImageFields.SQS.id,
            TextField(subjectQualityScore),
        )

    fun withSubjectPoseAnglesField(subjectPoseAngles: String) =
        withField(
            FacialAndSMTImageFields.SPA.id,
            TextField(subjectPoseAngles),
        )

    fun withSubjectFacialDescriptionField(subjectFacialDescription: String) =
        withField(
            FacialAndSMTImageFields.SXS.id,
            TextField(subjectFacialDescription),
        )

    fun withSubjectEyeColorField(subjectEyeColor: String) =
        withField(
            FacialAndSMTImageFields.SEC.id,
            TextField(subjectEyeColor),
        )

    fun withSubjectHairColorField(subjectHairColor: String) =
        withField(
            FacialAndSMTImageFields.SHC.id,
            TextField(subjectHairColor),
        )

    fun withFacialFeaturePointsLegacyField(facialFeaturePointsLegacy: String) =
        withField(
            FacialAndSMTImageFields.SFP_LEGACY.id,
            TextField(facialFeaturePointsLegacy),
        )

    fun with2DFacialFeaturePointsField(facialFeaturePoints2D: String) =
        withField(
            FacialAndSMTImageFields.FFP.id,
            TextField(facialFeaturePoints2D),
        )

    fun withDeviceMonitoringModeField(deviceMonitoringMode: String) =
        withField(
            FacialAndSMTImageFields.DMM.id,
            TextField(deviceMonitoringMode),
        )

    fun withTieredMarkupCollectionField(tieredMarkupCollection: String) =
        withField(
            FacialAndSMTImageFields.TMC.id,
            TextField(tieredMarkupCollection),
        )

    fun with3DFacialFeaturePointsField(facialFeaturePoints3D: String) =
        withField(
            FacialAndSMTImageFields.THREEDF.id,
            TextField(facialFeaturePoints3D),
        )

    fun withFeatureContoursField(featureContours: String) =
        withField(
            FacialAndSMTImageFields.FEC.id,
            TextField(featureContours),
        )

    fun withImageCaptureDateRangeEstimateField(captureDateRangeEstimate: String) =
        withField(
            FacialAndSMTImageFields.ICDR.id,
            TextField(captureDateRangeEstimate),
        )

    fun withCommentField(comment: String) =
        withField(
            FacialAndSMTImageFields.COM.id,
            TextField(comment),
        )

    fun withType10ReferenceNumberField(type10ReferenceNumber: String) =
        withField(
            FacialAndSMTImageFields.T10.id,
            TextField(type10ReferenceNumber),
        )

    fun withNcicDesignationCodeField(ncicDesignationCode: String) =
        withField(
            FacialAndSMTImageFields.SMT.id,
            TextField(ncicDesignationCode),
        )

    fun withScarMarkTattooSizeField(scarMarkTattooSize: String) =
        withField(
            FacialAndSMTImageFields.SMS.id,
            TextField(scarMarkTattooSize),
        )

    fun withSmtDescriptorsField(smtDescriptors: String) =
        withField(
            FacialAndSMTImageFields.SMD.id,
            TextField(smtDescriptors),
        )

    fun withColorsPresentField(colorsPresent: String) =
        withField(
            FacialAndSMTImageFields.COL.id,
            TextField(colorsPresent),
        )

    fun withImageTransformationField(imageTransformation: String) =
        withField(
            FacialAndSMTImageFields.ITX.id,
            TextField(imageTransformation),
        )

    fun withOcclusionsField(occlusions: String) =
        withField(
            FacialAndSMTImageFields.OCC.id,
            TextField(occlusions),
        )

    fun withImageSubjectConditionField(imageSubjectCondition: String) =
        withField(
            FacialAndSMTImageFields.SUB.id,
            TextField(imageSubjectCondition),
        )

    fun withCaptureOrganizationNameField(captureOrganizationName: String) =
        withField(
            FacialAndSMTImageFields.CON.id,
            TextField(captureOrganizationName),
        )

    fun withSuspectedPatternedInjuryDetailField(suspectedPatternedInjuryDetail: String) =
        withField(
            FacialAndSMTImageFields.PID.id,
            TextField(suspectedPatternedInjuryDetail),
        )

    fun withCheiloscopicImageDescriptionField(cheiloscopicImageDescription: String) =
        withField(
            FacialAndSMTImageFields.CID.id,
            TextField(cheiloscopicImageDescription),
        )

    fun withDentalVisualImageDataInformationField(dentalVisualImageDataInformation: String) =
        withField(
            FacialAndSMTImageFields.VID.id,
            TextField(dentalVisualImageDataInformation),
        )

    fun withRulerOrScalePresenceField(rulerOrScalePresence: String) =
        withField(
            FacialAndSMTImageFields.RSP.id,
            TextField(rulerOrScalePresence),
        )

    fun withAnnotationInformationField(annotationInformation: String) =
        withField(
            FacialAndSMTImageFields.ANN.id,
            TextField(annotationInformation),
        )

    fun withDeviceUniqueIdentifierField(deviceUniqueIdentifier: String) =
        withField(
            FacialAndSMTImageFields.DUI.id,
            TextField(deviceUniqueIdentifier),
        )

    fun withMakeModelSerialNumberField(makeModelSerialNumber: String) =
        withField(
            FacialAndSMTImageFields.MMS.id,
            TextField(makeModelSerialNumber),
        )

    fun withType2RecordCrossReferenceField(type2RecordCrossReference: String) =
        withField(
            FacialAndSMTImageFields.T2C.id,
            TextField(type2RecordCrossReference),
        )

    fun withSourceAgencyNameField(sourceAgencyName: String) =
        withField(
            FacialAndSMTImageFields.SAN.id,
            TextField(sourceAgencyName),
        )

    fun withExternalFileReferenceField(externalFileReference: String) =
        withField(
            FacialAndSMTImageFields.EFR.id,
            TextField(externalFileReference),
        )

    fun withAssociatedContextField(associatedContext: String) =
        withField(
            FacialAndSMTImageFields.ASC.id,
            TextField(associatedContext),
        )

    fun withHashField(hash: String) =
        withField(
            FacialAndSMTImageFields.HAS.id,
            TextField(hash),
        )

    fun withSourceRepresentationField(sourceRepresentation: String) =
        withField(
            FacialAndSMTImageFields.SOR.id,
            TextField(sourceRepresentation),
        )

    fun withGeographicSampleAcquisitionLocationField(geographicSampleAcquisitionLocation: String) =
        withField(
            FacialAndSMTImageFields.GEO.id,
            TextField(geographicSampleAcquisitionLocation),
        )

    /**
     * Sets DATA (Image Data) – the binary image data.
     *
     * @param imageData the binary image data as ByteArray
     */
    fun withImageDataField(imageData: ByteArray) = withField(FacialAndSMTImageFields.DATA.id, ImageField(imageData))
}
