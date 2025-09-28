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

package eu.aagsolutions.img.nbis.io

import eu.aagsolutions.img.nbis.model.enums.records.HighResolutionGrayscaleFingerprintImageFields
import eu.aagsolutions.img.nbis.model.records.HighResolutionGrayscaleFingerprintRecord
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import java.io.File
import javax.imageio.ImageIO
import kotlin.test.Test

class NistFileReaderTest {
    @Test
    fun `it should read sample nist file and compare fingerprint images with references`() {
        val url = NistFileReaderTest::class.java.getResource("/sample/nist/sample.an2")

        val nistContent = NistFileReader(url!!.openStream()).use { reader -> reader.read() }

        nistContent.shouldNotBeNull()

        val fingerprints =
            nistContent.getHighResolutionGrayscaleFingerprintRecords().sortedBy {
                it.getFieldAsInt(HighResolutionGrayscaleFingerprintImageFields.IDC)
            }
        fingerprints.shouldHaveSize(14)

        fingerprints.forEachIndexed { index, record ->
            val referenceImage =
                ImageIO.read(File(NistFileReaderTest::class.java.getResource("/sample/nist/fp-${index + 1}.png")!!.file))
            val nistImage = record as HighResolutionGrayscaleFingerprintRecord

            nistImage.apply {
                nistImage.getFieldAsInt(HighResolutionGrayscaleFingerprintImageFields.HLL) shouldBe referenceImage.width
                nistImage.getFieldAsInt(HighResolutionGrayscaleFingerprintImageFields.VLL) shouldBe referenceImage.height
            }
        }
    }

    @Test
    fun `it should read successfully a nist file with variable resolution fingerprints`() {
        val url = NistFileReaderTest::class.java.getResource("/references/type-14-amp-nqm-utf8.an2")
        val nistContent = NistFileReader(url!!.openStream()).use { reader -> reader.read() }

        nistContent
            .shouldNotBeNull()
            .apply {
                getUserDefinedDescriptionTextRecords()[0]
                    .fields[3]!!
                    .getData()
                    .toString() shouldContain "two chinese characters: 華裔"
                getVariableResolutionFingerprintRecords().shouldHaveSize(3)
            }
    }

    @Test
    fun `it should read successfully a nist file with low resolution grayscale fingerprints`() {
        val url = NistFileReaderTest::class.java.getResource("/references/type-3.an2")
        val nistContent = NistFileReader(url!!.openStream()).use { reader -> reader.read() }
        nistContent
            .shouldNotBeNull()
            .apply {
                getUserDefinedDescriptionTextRecords()[0]
                    .fields[3]!!
                    .getData()
                    .toString() shouldContain "domain defined text place holder"
                getLowResolutionGrayscaleFingerprintRecords().shouldHaveSize(1)
            }
    }

    @Test
    fun `it should read successfully a nist file with high resolution grayscale fingerprints`() {
        val url = NistFileReaderTest::class.java.getResource("/references/type-4-slaps.an2")
        val nistContent = NistFileReader(url!!.openStream()).use { reader -> reader.read() }
        nistContent
            .shouldNotBeNull()
            .apply {
                getUserDefinedDescriptionTextRecords()[0]
                    .fields[3]!!
                    .getData()
                    .toString() shouldContain "domain defined text place holder"
                getHighResolutionGrayscaleFingerprintRecords().shouldHaveSize(4)
            }
    }

    @Test
    fun `it should read successfully a nist file with low resolution binary fingerprints`() {
        val url = NistFileReaderTest::class.java.getResource("/references/type-5.an2")
        val nistContent = NistFileReader(url!!.openStream()).use { reader -> reader.read() }
        nistContent
            .shouldNotBeNull()
            .apply {
                getUserDefinedDescriptionTextRecords()[0]
                    .fields[3]!!
                    .getData()
                    .toString() shouldContain "domain defined text place holder"
                getLowResolutionBinaryFingerprintRecords().shouldHaveSize(1)
            }
    }

    @Test
    fun `it should read successfully a nist file with high resolution binary fingerprints`() {
        val url = NistFileReaderTest::class.java.getResource("/references/type-6.an2")
        val nistContent = NistFileReader(url!!.openStream()).use { reader -> reader.read() }
        nistContent
            .shouldNotBeNull()
            .apply {
                getUserDefinedDescriptionTextRecords()[0]
                    .fields[3]!!
                    .getData()
                    .toString() shouldContain "domain defined text place holder"
                getHighResolutionBinaryFingerprintRecords().shouldHaveSize(1)
            }
    }

    @Test
    fun `it should read successfully a nist file with user defined image records`() {
        val url = NistFileReaderTest::class.java.getResource("/references/type-7-latent.an2")
        val nistContent = NistFileReader(url!!.openStream()).use { reader -> reader.read() }
        nistContent
            .shouldNotBeNull()
            .apply {
                getUserDefinedDescriptionTextRecords()[0]
                    .fields[3]!!
                    .getData()
                    .toString() shouldContain "domain defined text place holder"
                getUserDefinedImageRecords().shouldHaveSize(1)
            }
    }

    @Test
    fun `it should read successfully a nist file with signature image records`() {
        val url = NistFileReaderTest::class.java.getResource("/references/type-8-sig.an2")
        val nistContent = NistFileReader(url!!.openStream()).use { reader -> reader.read() }
        nistContent
            .shouldNotBeNull()
            .apply {
                getUserDefinedDescriptionTextRecords()[0]
                    .fields[3]!!
                    .getData()
                    .toString() shouldContain "domain defined text place holder"
                getSignatureImageRecords().shouldHaveSize(1)
            }
    }

    @Test
    fun `it should read successfully a nist file with minutiae data records`() {
        val url = NistFileReaderTest::class.java.getResource("/references/type-9-4-iafis.an2")
        val nistContent = NistFileReader(url!!.openStream()).use { reader -> reader.read() }
        nistContent
            .shouldNotBeNull()
            .apply {
                getUserDefinedDescriptionTextRecords()[0]
                    .fields[3]!!
                    .getData()
                    .toString() shouldContain "domain defined text place holder"
                getMinutiaeDataRecords().shouldHaveSize(1)
            }
    }

    @Test
    fun `it should read successfully a nist file with facial, scars, marks and tattoos image data records`() {
        val url = NistFileReaderTest::class.java.getResource("/references/type-10-sap10.an2")
        val nistContent = NistFileReader(url!!.openStream()).use { reader -> reader.read() }
        nistContent
            .shouldNotBeNull()
            .apply {
                getUserDefinedDescriptionTextRecords()[0]
                    .fields[3]!!
                    .getData()
                    .toString() shouldContain "domain defined text place holder"
                getFacialAndSmtImageRecords().shouldHaveSize(1)
            }
    }

    @Test
    fun `it should read successfully a nist file with latent image data`() {
        val url = NistFileReaderTest::class.java.getResource("/references/type-13-tip-eji-j2l.an2")
        val nistContent = NistFileReader(url!!.openStream()).use { reader -> reader.read() }
        nistContent
            .shouldNotBeNull()
            .apply {
                getUserDefinedDescriptionTextRecords()[0]
                    .fields[3]!!
                    .getData()
                    .toString() shouldContain "domain defined text place holder"
                getLatentImageRecords().shouldHaveSize(5)
            }
    }

    @Test
    fun `it should read successfully a nist file with palm print image data`() {
        val url = NistFileReaderTest::class.java.getResource("/references/type-15-palms.an2")
        val nistContent = NistFileReader(url!!.openStream()).use { reader -> reader.read() }
        nistContent
            .shouldNotBeNull()
            .apply {
                getUserDefinedDescriptionTextRecords()[0]
                    .fields[3]!!
                    .getData()
                    .toString() shouldContain "domain defined text place holder"
                getVariableResolutionPalmPrintRecords().shouldHaveSize(2)
            }
    }

    @Test
    fun `it should read successfully a nist file with iris image data`() {
        val url = NistFileReaderTest::class.java.getResource("/references/type-17-iris.an2")
        val nistContent = NistFileReader(url!!.openStream()).use { reader -> reader.read() }
        nistContent
            .shouldNotBeNull()
            .apply {
                getUserDefinedDescriptionTextRecords()[0]
                    .fields[3]!!
                    .getData()
                    .toString() shouldContain "domain defined text place holder"
                getIrisImageRecords().shouldHaveSize(1)
            }
    }
}
