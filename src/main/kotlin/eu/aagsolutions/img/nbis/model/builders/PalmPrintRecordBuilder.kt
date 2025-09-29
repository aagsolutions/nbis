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
import eu.aagsolutions.img.nbis.model.enums.records.PalmPrintImageFields
import eu.aagsolutions.img.nbis.model.fields.TextField
import eu.aagsolutions.img.nbis.model.records.PalmPrintRecord

@Suppress("TooManyFunctions")
class PalmPrintRecordBuilder :
    TextRecordWithImageBuilder<PalmPrintRecord, PalmPrintRecordBuilder>(
        RecordType.RT15.id,
        RecordType.RT15.label,
        TextRecordLengthCalculator(),
    ) {
    override fun build(): PalmPrintRecord {
        val lengthField = calculator.calculate(this.id, this.fields)
        this.fields[LENGTH_FIELD_ID] = lengthField
        return PalmPrintRecord(this.fields)
    }

    fun withInformationDesignationCharField(designationChar: String) =
        withField(
            PalmPrintImageFields.IDC.id,
            TextField(designationChar),
        )

    fun withImpressionTypeField(impressionType: String) = withField(PalmPrintImageFields.IMP.id, TextField(impressionType))

    fun withSourceAgencyField(sourceAgency: String) = withField(PalmPrintImageFields.SRC.id, TextField(sourceAgency))

    fun withCaptureDateField(captureDate: String) = withField(PalmPrintImageFields.PCD.id, TextField(captureDate))

    override fun withHorizontalLineLengthField(horizontalLineLength: String) =
        withField(PalmPrintImageFields.HLL.id, TextField(horizontalLineLength))

    override fun withVerticalLineLengthField(verticalLineLength: String) =
        withField(PalmPrintImageFields.VLL.id, TextField(verticalLineLength))

    fun withScaleUnitsField(scaleUnits: String) = withField(PalmPrintImageFields.SLC.id, TextField(scaleUnits))

    override fun withTransmittedHorizontalPixelScaleField(horizontalPixelScale: String) =
        withField(PalmPrintImageFields.THPS.id, TextField(horizontalPixelScale))

    override fun withTransmittedVerticalPixelScaleField(verticalPixelScale: String) =
        withField(PalmPrintImageFields.TVPS.id, TextField(verticalPixelScale))

    override fun withCompressionAlgorithmField(compressionAlgorithm: String) =
        withField(PalmPrintImageFields.CGA.id, TextField(compressionAlgorithm))

    fun withBitPerPixelField(bitPerPixel: String) = withField(PalmPrintImageFields.BPX.id, TextField(bitPerPixel))

    fun withPalmPositionField(palmPosition: String) = withField(PalmPrintImageFields.FGP.id, TextField(palmPosition))

    fun withScanHorizontalPixelScaleField(scale: String) = withField(PalmPrintImageFields.SHPS.id, TextField(scale))

    fun withScanVerticalPixelScaleField(scale: String) = withField(PalmPrintImageFields.SVPS.id, TextField(scale))

    fun withAmputatedOrBandagedField(amputatedOrBandaged: String) = withField(PalmPrintImageFields.AMP.id, TextField(amputatedOrBandaged))

    fun withCommentField(comment: String) = withField(PalmPrintImageFields.COM.id, TextField(comment))

    fun withPalmSegmentationPositionField(segmentationPosition: String) =
        withField(PalmPrintImageFields.SEG.id, TextField(segmentationPosition))

    fun withPalmQualityMetricField(qualityMetric: String) = withField(PalmPrintImageFields.PQM.id, TextField(qualityMetric))

    fun withImageSubjectConditionField(condition: String) = withField(PalmPrintImageFields.SUB.id, TextField(condition))

    fun withCaptureOrganizationNameField(organizationName: String) = withField(PalmPrintImageFields.CON.id, TextField(organizationName))

    fun withFrictionRidgeCaptureTechnologyField(technology: String) = withField(PalmPrintImageFields.FCT.id, TextField(technology))

    fun withAnnotationInformationField(annotation: String) = withField(PalmPrintImageFields.ANN.id, TextField(annotation))

    fun withDeviceUniqueIdentifierField(deviceId: String) = withField(PalmPrintImageFields.DUI.id, TextField(deviceId))

    fun withMakeModelSerialNumberField(makeModel: String) = withField(PalmPrintImageFields.MMS.id, TextField(makeModel))

    fun withSourceAgencyNameField(agencyName: String) = withField(PalmPrintImageFields.SAN.id, TextField(agencyName))

    fun withExternalFileReferenceField(fileRef: String) = withField(PalmPrintImageFields.EFR.id, TextField(fileRef))

    fun withAssociatedContextField(context: String) = withField(PalmPrintImageFields.ASC.id, TextField(context))

    fun withHashField(hash: String) = withField(PalmPrintImageFields.HAS.id, TextField(hash))

    fun withSourceRepresentationField(sourceRep: String) = withField(PalmPrintImageFields.SOR.id, TextField(sourceRep))

    fun withGeographicSampleAcquisitionLocationField(location: String) = withField(PalmPrintImageFields.GEO.id, TextField(location))
}
