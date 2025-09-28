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

import eu.aagsolutions.img.nbis.io.NistFileReader
import eu.aagsolutions.img.nbis.io.NistFileWriter
import eu.aagsolutions.img.nbis.model.builders.FacialAndSMTImageRecordBuilder
import eu.aagsolutions.img.nbis.model.builders.UserDefinedTextRecordBuilder
import eu.aagsolutions.img.nbis.model.records.FacialAndSMTImageRecord
import eu.aagsolutions.img.nbis.model.records.LowResolutionGrayscaleFingerprintRecord
import eu.aagsolutions.img.nbis.model.records.UserDefinedTextRecord
import eu.aagsolutions.img.nbis.model.records.VariableResolutionFingerprintRecord
import java.io.FileOutputStream
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
                    .build(),
            )
        val nistFile =
            NistFileBuilder()
                .withTransactionInformationRecord(nistContent.getTransactionInformationRecord())
                .withUserDefinedDescriptionTextRecords(userDefinedText as List<UserDefinedTextRecord>)
                .withLowResolutionGrayscaleFingerprintRecords(
                    nistContent2.getLowResolutionGrayscaleFingerprintRecords() as List<LowResolutionGrayscaleFingerprintRecord>,
                ).withVariableResolutionFingerprintRecords(
                    records = nistContent.getVariableResolutionFingerprintRecords() as List<VariableResolutionFingerprintRecord>,
                ).build()
        NistFileWriter(FileOutputStream("output.nist")).use { writer -> writer.write(nistFile) }
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
                    ).build()
            }
        val nistFile =
            NistFileBuilder()
                .withTransactionInformationRecord(nistContent.getTransactionInformationRecord())
                .withUserDefinedDescriptionTextRecords(userDefinedText as List<UserDefinedTextRecord>)
                .withFacialAndSmtImageRecords(imageRecords)
                .build()
        NistFileWriter(FileOutputStream("output.nist")).use { writer -> writer.write(nistFile) }
    }

    @Test
    fun `it should successful create a new nist file based on provided image`() {

    }
}
