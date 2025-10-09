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

package eu.aagsolutions.img.nbis.model

import eu.aagsolutions.img.nbis.calculators.Calculators.generateAgencyTCN
import eu.aagsolutions.img.nbis.io.NistFileReader
import eu.aagsolutions.img.nbis.io.NistFileReaderTest
import eu.aagsolutions.img.nbis.io.NistFileWriter
import eu.aagsolutions.img.nbis.model.builders.FacialAndSMTImageRecordBuilder
import eu.aagsolutions.img.nbis.model.builders.TransactionInformationRecordBuilder
import eu.aagsolutions.img.nbis.model.builders.UserDefinedTextRecordBuilder
import eu.aagsolutions.img.nbis.model.builders.VariableResolutionFingerprintRecordBuilder
import eu.aagsolutions.img.nbis.model.enums.records.FacialAndSMTImageFields
import eu.aagsolutions.img.nbis.model.enums.records.ImageFields
import eu.aagsolutions.img.nbis.model.enums.reference.CompressionAlgorithm
import eu.aagsolutions.img.nbis.model.records.FacialAndSMTImageRecord
import eu.aagsolutions.img.nbis.model.records.LowResolutionGrayscaleFingerprintRecord
import eu.aagsolutions.img.nbis.model.records.UserDefinedTextRecord
import eu.aagsolutions.img.nbis.model.records.VariableResolutionFingerprintRecord
import io.kotest.matchers.shouldBe
import java.io.FileOutputStream
import java.time.LocalDateTime
import java.time.ZoneOffset
import kotlin.test.Test

class NistFileBuilderTest {
    @Test
    fun `it should build successfully a new nist file`() {
        val url = NistFileReader::class.java.getResource("/references/type-14-amp-nqm-utf8.an2")
        val nistContent = NistFileReader(url!!.openStream()).use { reader -> reader.read() }
        val url2 = NistFileReader::class.java.getResource("/references/type-3.an2")
        val nistContent2 = NistFileReader(url2!!.openStream()).use { reader -> reader.read() }
        val userDefinedText =
            listOf(
                nistContent.getUserDefinedDescriptionTextRecords()[0],
                UserDefinedTextRecordBuilder()
                    .fromRecord(
                        nistContent2.getUserDefinedDescriptionTextRecords()[0] as UserDefinedTextRecord,
                    ).withInformationDesignationCharField("1")
                    .calculateFields(true)
                    .build(),
            )
        val variableResolutionFingerprintRecord1 =
            VariableResolutionFingerprintRecordBuilder()
                .fromRecord(nistContent.getVariableResolutionFingerprintRecords()[0] as VariableResolutionFingerprintRecord)
                .withImageDataField(nistContent.getVariableResolutionFingerprintRecords()[0].getFieldImage(ImageFields.DATA)!!)
                .calculateFields(true)
                .build()
        val variableResolutionFingerprintRecord2 =
            VariableResolutionFingerprintRecordBuilder()
                .fromRecord(nistContent.getVariableResolutionFingerprintRecords()[1] as VariableResolutionFingerprintRecord)
                .withImageDataField(nistContent.getVariableResolutionFingerprintRecords()[1].getFieldImage(ImageFields.DATA)!!)
                .calculateFields(true)
                .build()
        val variableResolutionFingerprintRecord3 =
            VariableResolutionFingerprintRecordBuilder()
                .fromRecord(nistContent.getVariableResolutionFingerprintRecords()[2] as VariableResolutionFingerprintRecord)
                .withImageDataField(nistContent.getVariableResolutionFingerprintRecords()[2].getFieldImage(ImageFields.DATA)!!)
                .calculateFields(true)
                .build()
        val nistFile =
            NistFileBuilder()
                .withTransactionInformationRecord(nistContent.getTransactionInformationRecord())
                .withUserDefinedDescriptionTextRecords(*userDefinedText.filterIsInstance<UserDefinedTextRecord>().toTypedArray())
                .withLowResolutionGrayscaleFingerprintRecords(
                    *nistContent2
                        .getLowResolutionGrayscaleFingerprintRecords()
                        .filterIsInstance<LowResolutionGrayscaleFingerprintRecord>()
                        .toTypedArray(),
                ).withVariableResolutionFingerprintRecords(
                    variableResolutionFingerprintRecord1,
                    variableResolutionFingerprintRecord2,
                    variableResolutionFingerprintRecord3,
                ).build()
        NistFileWriter(FileOutputStream("new-nist-combined.nist")).use { writer -> writer.write(nistFile) }
    }

    @Test
    fun `it should build successfully a new type 10 nist file`() {
        val url = NistFileReader::class.java.getResource("/references/type-10-sap10.an2")
        val nistContent = NistFileReader(url!!.openStream()).use { reader -> reader.read() }
        val userDefinedText =
            listOf(nistContent.getUserDefinedDescriptionTextRecords()[0])
        val imageRecords =
            nistContent.getFacialAndSmtImageRecords().map { r ->
                FacialAndSMTImageRecordBuilder()
                    .fromRecord(
                        r as FacialAndSMTImageRecord,
                    ).calculateFields(true)
                    .build()
            }
        val nistFile =
            NistFileBuilder()
                .withTransactionInformationRecord(nistContent.getTransactionInformationRecord())
                .withUserDefinedDescriptionTextRecords(*userDefinedText.filterIsInstance<UserDefinedTextRecord>().toTypedArray())
                .withFacialAndSmtImageRecords(*imageRecords.toTypedArray())
                .build()
        NistFileWriter(FileOutputStream("type-10-sap10-copy.nist")).use { writer -> writer.write(nistFile) }
    }

    @Test
    fun `it should successful create a new nist file based on provided jpg image`() {
        val currentGmtDate = LocalDateTime.now(ZoneOffset.UTC)
        val transactionInformationRecord =
            TransactionInformationRecordBuilder()
                .withDateField(currentGmtDate.toLocalDate())
                .withGmtDateTimeField(currentGmtDate)
                .withVersionNumberField("0100")
                .withAgencyNamesField("0101")
                .withTransactionControlNumberField(generateAgencyTCN("INTERPOOL", 1000))
                .calculateFields(true)
                .build()
        val userDefinedTextRecord =
            UserDefinedTextRecordBuilder()
                .withInformationDesignationCharField("01")
                .withMiscellaneousIdentificationNumber1("RO109323243")
                .calculateFields(true)
                .build()
        val url = NistFileReaderTest::class.java.getResource("/img/mugshot-1024x1024.jpg")

        val faceImage = url!!.openStream().use { inputStream -> inputStream.readAllBytes() }
        val facialAndSMTImageRecord =
            FacialAndSMTImageRecordBuilder()
                .withInformationDesignationCharField("01")
                .withImageDataField(faceImage)
                .withScaleUnitsField("1")
                .calculateFields(true)
                .build()
        val nistFile =
            NistFileBuilder()
                .withTransactionInformationRecord(transactionInformationRecord)
                .withUserDefinedDescriptionTextRecords(userDefinedTextRecord)
                .withFacialAndSmtImageRecords(facialAndSMTImageRecord)
                .build()
        NistFileWriter(FileOutputStream("output_jpeg.nist")).use { writer -> writer.write(nistFile) }
        nistFile.getFacialAndSmtImageRecords()[0].getFieldText(FacialAndSMTImageFields.CGA) shouldBe CompressionAlgorithm.JPEGB.code
        nistFile.getFacialAndSmtImageRecords()[0].getFieldText(FacialAndSMTImageFields.CSP) shouldBe "RGB"
    }

    @Test
    fun `it should successful create a new nist file based on provided png image`() {
        val currentGmtDate = LocalDateTime.now(ZoneOffset.UTC)
        val transactionInformationRecord =
            TransactionInformationRecordBuilder()
                .withDateField(currentGmtDate.toLocalDate())
                .withGmtDateTimeField(currentGmtDate)
                .withVersionNumberField("0100")
                .withAgencyNamesField("0101")
                .withTransactionControlNumberField(generateAgencyTCN("INTERPOOL", 1000))
                .calculateFields(true)
                .build()
        val userDefinedTextRecord =
            UserDefinedTextRecordBuilder()
                .withInformationDesignationCharField("01")
                .withMiscellaneousIdentificationNumber1("RO109323243")
                .calculateFields(true)
                .build()
        val url = NistFileReaderTest::class.java.getResource("/img/fp-1.png")

        val faceImage = url!!.openStream().use { inputStream -> inputStream.readAllBytes() }
        val facialAndSMTImageRecord =
            FacialAndSMTImageRecordBuilder()
                .withInformationDesignationCharField("01")
                .withImageDataField(faceImage)
                .withScaleUnitsField("1")
                .calculateFields(true)
                .build()
        val nistFile =
            NistFileBuilder()
                .withTransactionInformationRecord(transactionInformationRecord)
                .withUserDefinedDescriptionTextRecords(userDefinedTextRecord)
                .withFacialAndSmtImageRecords(facialAndSMTImageRecord)
                .build()
        NistFileWriter(FileOutputStream("output_png.nist")).use { writer -> writer.write(nistFile) }
        nistFile.getFacialAndSmtImageRecords()[0].getFieldText(FacialAndSMTImageFields.CGA) shouldBe CompressionAlgorithm.PNG.code
        nistFile.getFacialAndSmtImageRecords()[0].getFieldText(FacialAndSMTImageFields.CSP) shouldBe "RGB"
    }
}
