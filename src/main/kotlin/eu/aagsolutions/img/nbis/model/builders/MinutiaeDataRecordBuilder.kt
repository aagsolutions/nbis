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
import eu.aagsolutions.img.nbis.model.enums.records.MinutiaeDataFields
import eu.aagsolutions.img.nbis.model.fields.TextField
import eu.aagsolutions.img.nbis.model.records.MinutiaeDataRecord

/**
 * Builder class for creating and modifying Type-9 Minutiae Data records in NIST transactions.
 *
 * This builder exposes fluent helpers for setting the fields defined in MinutiaeDataFields
 * (ANSI/NIST-ITL 1-2011 and later). It automatically computes and injects the required LEN
 * field during build().
 *
 * Type-9 records contain minutiae, ridge counts and associated feature metadata for friction
 * ridge impressions (finger, palm, plantar), including legacy and EFS elements.
 */
@Suppress("TooManyFunctions")
class MinutiaeDataRecordBuilder :
    NistRecordBuilder<MinutiaeDataRecord, MinutiaeDataRecordBuilder>(
        RecordType.RT9.id,
        RecordType.RT9.label,
        TextRecordLengthCalculator(),
    ) {
    /**
     * Builds a new MinutiaeDataRecord with the configured fields.
     *
     * Calculates the logical record length (LEN) using the configured calculator,
     * inserts it into the field map and returns an immutable MinutiaeDataRecord
     * instance containing all configured fields.
     *
     * @return A MinutiaeDataRecord instance containing all configured fields
     */
    override fun build(): MinutiaeDataRecord {
        val lengthField = calculator.calculate(this.id, this.fields)
        this.fields[LENGTH_FIELD_ID] = lengthField
        return MinutiaeDataRecord(this.fields)
    }

    /**
     * Sets IDC (Information Designation Character) – uniquely identifies this Type-9 record within the transaction.
     *
     * @param designationChar The IDC value
     * @return The builder instance for method chaining
     */
    fun withInformationDesignationCharField(designationChar: String) = withField(MinutiaeDataFields.IDC.id, TextField(designationChar))

    /**
     * Sets IMP (Impression Type) – indicates how the friction ridge impression was obtained.
     *
     * @param impressionType The impression type value for the IMP field
     * @return The builder instance for method chaining
     */
    fun withImpressionTypeField(impressionType: String) = withField(MinutiaeDataFields.IMP.id, TextField(impressionType))

    /**
     * Sets FMT (Minutiae Format) – identifies the minutiae data format used in this record.
     *
     * @param format The minutiae format identifier
     * @return The builder instance for method chaining
     */
    fun withMinutiaeFormatField(format: String) = withField(MinutiaeDataFields.FMT.id, TextField(format))

    /**
     * Sets OFR (Originating Fingerprint Reading System) – legacy field identifying the source system.
     *
     * @param system The originating system identifier
     * @return The builder instance for method chaining
     */
    fun withOriginatingFingerprintReadingSystemField(system: String) = withField(MinutiaeDataFields.OFR_LEGACY.id, TextField(system))

    /**
     * Sets FGP (Finger Position) – legacy field specifying the finger position.
     *
     * @param position The finger position value
     * @return The builder instance for method chaining
     */
    fun withFingerPositionLegacyField(position: String) = withField(MinutiaeDataFields.FGP_LEGACY.id, TextField(position))

    /**
     * Sets FPC (Finger Pattern Classification) – legacy field describing the fingerprint pattern class.
     *
     * @param classification The pattern classification value
     * @return The builder instance for method chaining
     */
    fun withFingerPatternClassificationLegacyField(classification: String) =
        withField(MinutiaeDataFields.FPC_LEGACY.id, TextField(classification))

    /**
     * Sets CRP (Core Position) – legacy field indicating the position of the core.
     *
     * @param corePosition The core position value
     * @return The builder instance for method chaining
     */
    fun withCorePositionLegacyField(corePosition: String) = withField(MinutiaeDataFields.CRP_LEGACY.id, TextField(corePosition))

    /**
     * Sets DLT (Delta Position) – legacy field indicating the position of the delta.
     *
     * @param deltaPosition The delta position value
     * @return The builder instance for method chaining
     */
    fun withDeltaPositionLegacyField(deltaPosition: String) = withField(MinutiaeDataFields.DLT_LEGACY.id, TextField(deltaPosition))

    /**
     * Sets MIN (Number of Minutiae) – legacy field specifying how many minutiae are present.
     *
     * @param numberOfMinutiae The number of minutiae
     * @return The builder instance for method chaining
     */
    fun withNumberOfMinutiaeLegacyField(numberOfMinutiae: String) = withField(MinutiaeDataFields.MIN_LEGACY.id, TextField(numberOfMinutiae))

    /**
     * Sets RDG (Minutiae Ridge Count Indicator) – legacy field indicating presence/use of ridge counts.
     *
     * @param indicator The ridge count indicator value
     * @return The builder instance for method chaining
     */
    fun withMinutiaeRidgeCountIndicatorLegacyField(indicator: String) = withField(MinutiaeDataFields.RDG_LEGACY.id, TextField(indicator))

    /**
     * Sets MRC (Minutiae and Ridge Count Data) – legacy field containing minutiae and ridge count details.
     *
     * @param data The minutiae and ridge count data
     * @return The builder instance for method chaining
     */
    fun withMinutiaeAndRidgeCountDataLegacyField(data: String) = withField(MinutiaeDataFields.MRC_LEGACY.id, TextField(data))

    /**
     * Sets CBI (CBEFF Information) – M1 CBEFF metadata about the capture equipment/biometric data.
     *
     * @param cbeffInformation The CBEFF information value
     * @return The builder instance for method chaining
     */
    fun withCaptureEquipmentInformationField(cbeffInformation: String) = withField(MinutiaeDataFields.CBI.id, TextField(cbeffInformation))

    /**
     * Sets CEI (Capture Equipment ID) – M1 identifier of the capture device.
     *
     * @param captureEquipmentId The capture equipment identifier
     * @return The builder instance for method chaining
     */
    fun withCaptureEquipmentIdField(captureEquipmentId: String) = withField(MinutiaeDataFields.CEI.id, TextField(captureEquipmentId))

    /**
     * Sets HLL (Horizontal Line Length) – number of pixels per horizontal line of the image.
     *
     * @param horizontalLineLength The number of pixels per line
     * @return The builder instance for method chaining
     */
    fun withHorizontalLineLengthField(horizontalLineLength: String) = withField(MinutiaeDataFields.HLL.id, TextField(horizontalLineLength))

    /**
     * Sets VLL (Vertical Line Length) – number of horizontal lines comprising the image height.
     *
     * @param verticalLineLength The number of horizontal lines in the image
     * @return The builder instance for method chaining
     */
    fun withVerticalLineLengthField(verticalLineLength: String) = withField(MinutiaeDataFields.VLL.id, TextField(verticalLineLength))

    /**
     * Sets SLC (Scale Units) – measurement units used for pixel scale values (e.g., PPI).
     *
     * @param scaleUnits The scale units value
     * @return The builder instance for method chaining
     */
    fun withScaleUnitsField(scaleUnits: String) = withField(MinutiaeDataFields.SLC.id, TextField(scaleUnits))

    /**
     * Sets THPS (Transmitted Horizontal Pixel Scale) – horizontal pixel density of the transmitted image.
     *
     * @param horizontalPixelScale The horizontal pixel scale value
     * @return The builder instance for method chaining
     */
    fun withTransmittedHorizontalPixelScaleField(horizontalPixelScale: String) =
        withField(MinutiaeDataFields.THPS.id, TextField(horizontalPixelScale))

    /**
     * Sets TVPS (Transmitted Vertical Pixel Scale) – vertical pixel density of the transmitted image.
     *
     * @param verticalPixelScale The vertical pixel scale value
     * @return The builder instance for method chaining
     */
    fun withTransmittedVerticalPixelScaleField(verticalPixelScale: String) =
        withField(MinutiaeDataFields.TVPS.id, TextField(verticalPixelScale))

    /**
     * Sets FVW (Finger View) – indicates the view number for the finger impression.
     *
     * @param fingerView The finger view value
     * @return The builder instance for method chaining
     */
    fun withFingerViewField(fingerView: String) = withField(MinutiaeDataFields.FVW.id, TextField(fingerView))

    /**
     * Sets FGP (Friction Ridge Generalized Position) – generalized anatomic position of the ridge detail.
     *
     * @param position The generalized position value
     * @return The builder instance for method chaining
     */
    fun withFrictionRidgeGeneralizedPositionField(position: String) = withField(MinutiaeDataFields.FGP.id, TextField(position))

    /**
     * Sets FQD (Friction Ridge Quality Data) – quality information for the friction ridge data.
     *
     * @param qualityData The quality data value
     * @return The builder instance for method chaining
     */
    fun withFrictionRidgeQualityDataField(qualityData: String) = withField(MinutiaeDataFields.FQD.id, TextField(qualityData))

    /**
     * Sets NOM (Number of Minutiae) – count of minutiae encoded in the record (EFS).
     *
     * @param numberOfMinutiae The number of minutiae
     * @return The builder instance for method chaining
     */
    fun withNumberOfMinutiaeField(numberOfMinutiae: String) = withField(MinutiaeDataFields.NOM.id, TextField(numberOfMinutiae))

    /**
     * Sets FMD (Finger Minutiae Data) – the serialized minutiae data per the selected format profile.
     *
     * @param minutiaeData The minutiae data string
     * @return The builder instance for method chaining
     */
    fun withFingerMinutiaeDataField(minutiaeData: String) = withField(MinutiaeDataFields.FMD.id, TextField(minutiaeData))

    /**
     * Sets RCI (Ridge Count Information) – ridge count data between feature points.
     *
     * @param ridgeCountInfo The ridge count information
     * @return The builder instance for method chaining
     */
    fun withRidgeCountInformationField(ridgeCountInfo: String) = withField(MinutiaeDataFields.RCI.id, TextField(ridgeCountInfo))

    /**
     * Sets CIN (Core Information) – data describing detected core(s).
     *
     * @param coreInfo The core information value
     * @return The builder instance for method chaining
     */
    fun withCoreInformationField(coreInfo: String) = withField(MinutiaeDataFields.CIN.id, TextField(coreInfo))

    /**
     * Sets DIN (Delta Information) – data describing detected delta(s).
     *
     * @param deltaInfo The delta information value
     * @return The builder instance for method chaining
     */
    fun withDeltaInformationField(deltaInfo: String) = withField(MinutiaeDataFields.DIN.id, TextField(deltaInfo))

    /**
     * Sets ADA (Additional Delta Angles) – supplementary angular measurements at delta(s).
     *
     * @param additionalAngles The additional delta angles value
     * @return The builder instance for method chaining
     */
    fun withAdditionalDeltaAnglesField(additionalAngles: String) = withField(MinutiaeDataFields.ADA.id, TextField(additionalAngles))

    /**
     * Sets OOD (Owner or Developer) – identifies the owner/developer of an alternate feature set.
     *
     * @param ownerOrDeveloper The owner or developer value
     * @return The builder instance for method chaining
     */
    fun withOwnerOrDeveloperField(ownerOrDeveloper: String) = withField(MinutiaeDataFields.OOD.id, TextField(ownerOrDeveloper))

    /**
     * Sets PAG (Processing Algorithm) – identifies the algorithm used to generate features.
     *
     * @param processingAlgorithm The processing algorithm identifier
     * @return The builder instance for method chaining
     */
    fun withProcessingAlgorithmField(processingAlgorithm: String) = withField(MinutiaeDataFields.PAG.id, TextField(processingAlgorithm))

    /**
     * Sets SOD (System or Device) – identifies the system or device producing the feature set.
     *
     * @param systemOrDevice The system/device identifier
     * @return The builder instance for method chaining
     */
    fun withSystemOrDeviceField(systemOrDevice: String) = withField(MinutiaeDataFields.SOD.id, TextField(systemOrDevice))

    /**
     * Sets DTX (Contact Information) – contact details for the owner/developer of the feature set.
     *
     * @param contactInformation The contact information value
     * @return The builder instance for method chaining
     */
    fun withContactInformationField(contactInformation: String) = withField(MinutiaeDataFields.DTX.id, TextField(contactInformation))

    /**
     * Sets ROI (Region of Interest) – EFS region(s) within the image considered for features.
     *
     * @param roi The region of interest specification
     * @return The builder instance for method chaining
     */
    fun withRegionOfInterestField(roi: String) = withField(MinutiaeDataFields.ROI.id, TextField(roi))

    /**
     * Sets ORT (Orientation) – EFS overall ridge orientation information.
     *
     * @param orientation The orientation value
     * @return The builder instance for method chaining
     */
    fun withOrientationField(orientation: String) = withField(MinutiaeDataFields.ORT.id, TextField(orientation))

    /**
     * Sets FPP (Finger, Palm, Plantar Position) – EFS anatomic position classification.
     *
     * @param position The FPP position value
     * @return The builder instance for method chaining
     */
    fun withFingerPalmPlantarPositionField(position: String) = withField(MinutiaeDataFields.FPP.id, TextField(position))

    /**
     * Sets FSP (Feature Set Profile) – identifies the EFS profile used.
     *
     * @param profile The feature set profile identifier
     * @return The builder instance for method chaining
     */
    fun withFeatureSetProfileField(profile: String) = withField(MinutiaeDataFields.FSP.id, TextField(profile))

    /**
     * Sets PAT (Pattern Classification) – EFS classification of the ridge pattern.
     *
     * @param pattern The pattern classification value
     * @return The builder instance for method chaining
     */
    fun withPatternClassificationField(pattern: String) = withField(MinutiaeDataFields.PAT.id, TextField(pattern))

    /**
     * Sets RQM (Ridge Quality Map) – EFS map describing local ridge quality.
     *
     * @param map The ridge quality map value
     * @return The builder instance for method chaining
     */
    fun withRidgeQualityMapField(map: String) = withField(MinutiaeDataFields.RQM.id, TextField(map))

    /**
     * Sets RQF (Ridge Quality Format) – encoding/format of the ridge quality map.
     *
     * @param format The ridge quality format identifier or version
     * @return The builder instance for method chaining
     */
    fun withRidgeQualityFormatField(format: String) = withField(MinutiaeDataFields.RQF.id, TextField(format))

    /**
     * Sets RFM (Ridge Flow Map) – EFS map describing local ridge flow directions.
     *
     * @param map The ridge flow map data
     * @return The builder instance for method chaining
     */
    fun withRidgeFlowMapField(map: String) = withField(MinutiaeDataFields.RFM.id, TextField(map))

    /**
     * Sets RFF (Ridge Flow Map Format) – encoding/format for the ridge flow map.
     *
     * @param format The ridge flow map format identifier or version
     * @return The builder instance for method chaining
     */
    fun withRidgeFlowMapFormatField(format: String) = withField(MinutiaeDataFields.RFF.id, TextField(format))

    /**
     * Sets RWM (Ridge Wavelength Map) – EFS map describing local ridge wavelengths/frequencies.
     *
     * @param map The ridge wavelength map data
     * @return The builder instance for method chaining
     */
    fun withRidgeWavelengthMapField(map: String) = withField(MinutiaeDataFields.RWM.id, TextField(map))

    /**
     * Sets RWF (Ridge Wavelength Map Format) – encoding/format for the ridge wavelength map.
     *
     * @param format The ridge wavelength map format identifier or version
     * @return The builder instance for method chaining
     */
    fun withRidgeWavelengthMapFormatField(format: String) = withField(MinutiaeDataFields.RWF.id, TextField(format))

    /**
     * Sets TRV (Tonal Reversal) – indicates whether the image exhibits tonal reversal.
     *
     * @param tonalReversal The tonal reversal indicator/value
     * @return The builder instance for method chaining
     */
    fun withTonalReversalField(tonalReversal: String) = withField(MinutiaeDataFields.TRV.id, TextField(tonalReversal))

    /**
     * Sets PLR (Possible Lateral Reversal) – indicates potential left-right/lateral reversal.
     *
     * @param possibleLateralReversal The lateral reversal indicator/value
     * @return The builder instance for method chaining
     */
    fun withPossibleLateralReversalField(possibleLateralReversal: String) =
        withField(MinutiaeDataFields.PLR.id, TextField(possibleLateralReversal))

    /**
     * Sets FQM (Friction Ridge Quality Metric) – numeric/encoded measure of overall ridge quality.
     *
     * @param metric The quality metric value
     * @return The builder instance for method chaining
     */
    fun withFrictionRidgeQualityMetricField(metric: String) = withField(MinutiaeDataFields.FQM.id, TextField(metric))

    /**
     * Sets PGS (Possible Growth or Shrinkage) – indicates potential size changes in the impression.
     *
     * @param value The growth/shrinkage indicator or magnitude
     * @return The builder instance for method chaining
     */
    fun withPossibleGrowthOrShrinkageField(value: String) = withField(MinutiaeDataFields.PGS.id, TextField(value))

    /**
     * Sets COR (Cores) – EFS description of core point(s).
     *
     * @param cores The encoded cores information
     * @return The builder instance for method chaining
     */
    fun withCoresField(cores: String) = withField(MinutiaeDataFields.COR.id, TextField(cores))

    /**
     * Sets DEL (Deltas) – EFS description of delta point(s).
     *
     * @param deltas The encoded deltas information
     * @return The builder instance for method chaining
     */
    fun withDeltasField(deltas: String) = withField(MinutiaeDataFields.DEL.id, TextField(deltas))

    /**
     * Sets CDR (Core-Delta Ridge Counts) – ridge counts measured between core and delta points.
     *
     * @param counts The core-delta ridge counts data
     * @return The builder instance for method chaining
     */
    fun withCoreDeltaRidgeCountsField(counts: String) = withField(MinutiaeDataFields.CDR.id, TextField(counts))

    /**
     * Sets CPR (Center Point of Reference) – reference point used for alignment/annotation.
     *
     * @param centerPoint The center point of reference data
     * @return The builder instance for method chaining
     */
    fun withCenterPointOfReferenceField(centerPoint: String) = withField(MinutiaeDataFields.CPR.id, TextField(centerPoint))

    /**
     * Sets DIS (Distinctive Features) – EFS description of distinctive ridge features.
     *
     * @param features The encoded distinctive features
     * @return The builder instance for method chaining
     */
    fun withDistinctiveFeaturesField(features: String) = withField(MinutiaeDataFields.DIS.id, TextField(features))

    /**
     * Sets NCOR (No Cores Present) – flag indicating that no cores are present.
     *
     * @param flag The boolean/flag value
     * @return The builder instance for method chaining
     */
    fun withNoCoresPresentField(flag: String) = withField(MinutiaeDataFields.NCOR.id, TextField(flag))

    /**
     * Sets NDEL (No Deltas Present) – flag indicating that no deltas are present.
     *
     * @param flag The boolean/flag value
     * @return The builder instance for method chaining
     */
    fun withNoDeltasPresentField(flag: String) = withField(MinutiaeDataFields.NDEL.id, TextField(flag))

    /**
     * Sets NDIS (No Distinctive Features Present) – flag indicating that no distinctive features are present.
     *
     * @param flag The boolean/flag value
     * @return The builder instance for method chaining
     */
    fun withNoDistinctiveFeaturesPresentField(flag: String) = withField(MinutiaeDataFields.NDIS.id, TextField(flag))

    /**
     * Sets MIN (EFS Minutiae) – list/encoding of minutiae according to the EFS specification.
     *
     * @param minutiae The encoded minutiae data
     * @return The builder instance for method chaining
     */
    fun withEfsMinutiaeField(minutiae: String) = withField(MinutiaeDataFields.MIN.id, TextField(minutiae))

    /**
     * Sets MRA (Minutiae Ridge Count Algorithm) – identifies the algorithm used to compute ridge counts.
     *
     * @param algorithm The algorithm identifier or description
     * @return The builder instance for method chaining
     */
    fun withMinutiaeRidgeCountAlgorithmField(algorithm: String) = withField(MinutiaeDataFields.MRA.id, TextField(algorithm))

    /**
     * Sets MRC (Minutiae Ridge Counts) – ridge counts associated with the encoded minutiae.
     *
     * @param counts The minutiae ridge counts data
     * @return The builder instance for method chaining
     */
    fun withMinutiaeRidgeCountsField(counts: String) = withField(MinutiaeDataFields.MRC.id, TextField(counts))

    /**
     * Sets NMIN (No Minutiae Present) – flag indicating that no minutiae are present.
     *
     * @param flag The boolean/flag value
     * @return The builder instance for method chaining
     */
    fun withNoMinutiaePresentField(flag: String) = withField(MinutiaeDataFields.NMIN.id, TextField(flag))

    /**
     * Sets RCC (Ridge Count Confidence) – confidence metric for computed ridge counts.
     *
     * @param confidence The confidence value/metric
     * @return The builder instance for method chaining
     */
    fun withRidgeCountConfidenceField(confidence: String) = withField(MinutiaeDataFields.RCC.id, TextField(confidence))

    /**
     * Sets DOT (Dots) – EFS dot features within the friction ridge detail.
     *
     * @param dots The encoded dots features
     * @return The builder instance for method chaining
     */
    fun withDotsField(dots: String) = withField(MinutiaeDataFields.DOT.id, TextField(dots))

    /**
     * Sets INR (Incipient Ridges) – EFS incipient/partial ridge features.
     *
     * @param incipientRidges The encoded incipient ridge features
     * @return The builder instance for method chaining
     */
    fun withIncipientRidgesField(incipientRidges: String) = withField(MinutiaeDataFields.INR.id, TextField(incipientRidges))

    /**
     * Sets CLD (Creases and Linear Discontinuities) – EFS creases/line interruptions in ridge detail.
     *
     * @param creases The encoded creases/linear discontinuities
     * @return The builder instance for method chaining
     */
    fun withCreasesAndLinearDiscontinuitiesField(creases: String) = withField(MinutiaeDataFields.CLD.id, TextField(creases))

    /**
     * Sets REF (Ridge Edge Features) – EFS ridge edge characteristics (e.g., breaks, notches).
     *
     * @param features The encoded ridge edge features
     * @return The builder instance for method chaining
     */
    fun withRidgeEdgeFeaturesField(features: String) = withField(MinutiaeDataFields.REF.id, TextField(features))

    /**
     * Sets NPOR (No Pores Present) – flag indicating that no pore features are present.
     *
     * @param flag The boolean/flag value
     * @return The builder instance for method chaining
     */
    fun withNoPoresPresentField(flag: String) = withField(MinutiaeDataFields.NPOR.id, TextField(flag))

    /**
     * Sets POR (Pores) – EFS pore features within the friction ridge detail.
     *
     * @param pores The encoded pore features
     * @return The builder instance for method chaining
     */
    fun withPoresField(pores: String) = withField(MinutiaeDataFields.POR.id, TextField(pores))

    /**
     * Sets NDOT (No Dots Present) – flag indicating that no dot features are present.
     *
     * @param flag The boolean/flag value
     * @return The builder instance for method chaining
     */
    fun withNoDotsPresentField(flag: String) = withField(MinutiaeDataFields.NDOT.id, TextField(flag))

    /**
     * Sets NINR (No Incipient Ridges Present) – flag indicating that no incipient ridges are present.
     *
     * @param flag The boolean/flag value
     * @return The builder instance for method chaining
     */
    fun withNoIncipientRidgesPresentField(flag: String) = withField(MinutiaeDataFields.NINR.id, TextField(flag))

    /**
     * Sets NCLD (No Creases Present) – flag indicating that no creases are present.
     *
     * @param flag The boolean/flag value
     * @return The builder instance for method chaining
     */
    fun withNoCreasesPresentField(flag: String) = withField(MinutiaeDataFields.NCLD.id, TextField(flag))

    /**
     * Sets NREF (No Ridge Edge Features Present) – flag indicating that no ridge edge features are present.
     *
     * @param flag The boolean/flag value
     * @return The builder instance for method chaining
     */
    fun withNoRidgeEdgeFeaturesPresentField(flag: String) = withField(MinutiaeDataFields.NREF.id, TextField(flag))

    /**
     * Sets MFD (Method of Feature Detection) – method used to detect/annotate features.
     *
     * @param method The method/approach identifier or description
     * @return The builder instance for method chaining
     */
    fun withMethodOfFeatureDetectionField(method: String) = withField(MinutiaeDataFields.MFD.id, TextField(method))

    /**
     * Sets COM (Comment) – free-form comment related to the minutiae/feature set.
     *
     * @param comment The comment text
     * @return The builder instance for method chaining
     */
    fun withCommentField(comment: String) = withField(MinutiaeDataFields.COM.id, TextField(comment))

    /**
     * Sets LPM (Latent Processing Method) – method used to process/develop the latent print.
     *
     * @param method The processing method identifier or description
     * @return The builder instance for method chaining
     */
    fun withLatentProcessingMethodField(method: String) = withField(MinutiaeDataFields.LPM.id, TextField(method))

    /**
     * Sets EAA (Examiner Analysis Assessment) – assessment outcome of examiner analysis.
     *
     * @param assessment The assessment value/notes
     * @return The builder instance for method chaining
     */
    fun withExaminerAnalysisAssessmentField(assessment: String) = withField(MinutiaeDataFields.EAA.id, TextField(assessment))

    /**
     * Sets EOF (Evidence of Fraud) – records any indicators of alteration or fraud.
     *
     * @param evidence The evidence/notes of fraud
     * @return The builder instance for method chaining
     */
    fun withEvidenceOfFraudField(evidence: String) = withField(MinutiaeDataFields.EOF.id, TextField(evidence))

    /**
     * Sets LSB (Latent Substrate) – describes the surface on which the latent impression was deposited.
     *
     * @param substrate The latent substrate description
     * @return The builder instance for method chaining
     */
    fun withLatentSubstrateField(substrate: String) = withField(MinutiaeDataFields.LSB.id, TextField(substrate))

    /**
     * Sets LMT (Latent Matrix) – describes the matrix/medium of the latent impression (e.g., residue/contaminant).
     *
     * @param matrix The latent matrix description
     * @return The builder instance for method chaining
     */
    fun withLatentMatrixField(matrix: String) = withField(MinutiaeDataFields.LMT.id, TextField(matrix))

    /**
     * Sets LQI (Local Quality Issues) – documents localized quality deficiencies affecting interpretation.
     *
     * @param issues The local quality issues description/encoding
     * @return The builder instance for method chaining
     */
    fun withLocalQualityIssuesField(issues: String) = withField(MinutiaeDataFields.LQI.id, TextField(issues))

    /**
     * Sets AOC (Area of Correspondence) – region(s) used for comparison/correspondence assessment.
     *
     * @param aoc The area of correspondence specification/encoding
     * @return The builder instance for method chaining
     */
    fun withAreaOfCorrespondenceField(aoc: String) = withField(MinutiaeDataFields.AOC.id, TextField(aoc))

    /**
     * Sets CPF (Corresponding Points or Features) – list/encoding of feature correspondences.
     *
     * @param cpf The corresponding points/features encoding
     * @return The builder instance for method chaining
     */
    fun withCorrespondingPointsOrFeaturesField(cpf: String) = withField(MinutiaeDataFields.CPF.id, TextField(cpf))

    /**
     * Sets ECD (Examiner Comparison Determination) – conclusion of the examiner's comparison.
     *
     * @param ecd The comparison determination value/notes
     * @return The builder instance for method chaining
     */
    fun withExaminerComparisonDeterminationField(ecd: String) = withField(MinutiaeDataFields.ECD.id, TextField(ecd))

    /**
     * Sets RRC (Relative Rotation of Corresponding Print) – rotation between compared impressions.
     *
     * @param rrc The relative rotation value/encoding
     * @return The builder instance for method chaining
     */
    fun withRelativeRotationOfCorrespondingPrintField(rrc: String) = withField(MinutiaeDataFields.RRC.id, TextField(rrc))

    /**
     * Sets SIM (Skeletonized Image) – reference or encoding of a skeletonized ridge image.
     *
     * @param sim The skeletonized image reference/encoding
     * @return The builder instance for method chaining
     */
    fun withSkeletonizedImageField(sim: String) = withField(MinutiaeDataFields.SIM.id, TextField(sim))

    /**
     * Sets RPS (Ridge Path Segments) – encoded ridge path segment information.
     *
     * @param rps The ridge path segments encoding
     * @return The builder instance for method chaining
     */
    fun withRidgePathSegmentsField(rps: String) = withField(MinutiaeDataFields.RPS.id, TextField(rps))

    /**
     * Sets TPL (Temporary Lines) – temporary lines used for analysis/annotation purposes.
     *
     * @param tpl The temporary lines encoding/notes
     * @return The builder instance for method chaining
     */
    fun withTemporaryLinesField(tpl: String) = withField(MinutiaeDataFields.TPL.id, TextField(tpl))

    /**
     * Sets FCC (Feature Color and Comment) – color coding and comments for annotated features.
     *
     * @param fcc The feature color and comment encoding
     * @return The builder instance for method chaining
     */
    fun withFeatureColorAndCommentField(fcc: String) = withField(MinutiaeDataFields.FCC.id, TextField(fcc))

    /**
     * Sets ULA (Universal Latent Annotation) – standardized latent annotation content.
     *
     * @param ula The universal latent annotation encoding
     * @return The builder instance for method chaining
     */
    fun withUniversalLatentAnnotationField(ula: String) = withField(MinutiaeDataFields.ULA.id, TextField(ula))

    /**
     * Sets ANN (Annotation Information) – general annotation metadata/details.
     *
     * @param info The annotation information/notes
     * @return The builder instance for method chaining
     */
    fun withAnnotationInformationField(info: String) = withField(MinutiaeDataFields.ANN.id, TextField(info))

    /**
     * Sets DUI (Device Unique Identifier) – unique identifier of the device used.
     *
     * @param dui The device unique identifier value
     * @return The builder instance for method chaining
     */
    fun withDeviceUniqueIdentifierField(dui: String) = withField(MinutiaeDataFields.DUI.id, TextField(dui))

    /**
     * Sets MMS (Make/Model/Serial Number) – device make, model and serial number details.
     *
     * @param mms The make/model/serial number value
     * @return The builder instance for method chaining
     */
    fun withMakeModelSerialNumberField(mms: String) = withField(MinutiaeDataFields.MMS.id, TextField(mms))
}
