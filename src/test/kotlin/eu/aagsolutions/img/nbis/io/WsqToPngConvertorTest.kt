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

import eu.aagsolutions.img.nbis.exceptions.NistException
import eu.aagsolutions.img.nbis.model.enums.reference.CompressionAlgorithm
import io.kotest.matchers.shouldBe
import java.security.MessageDigest
import java.util.HexFormat
import javax.imageio.ImageIO
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull

class WsqToPngConvertorTest {
    private val convertor = WsqToPngConvertor()

    @Test
    fun `it converts sample WSQ to PNG without changing decoded pixels`() {
        val wsqData = sampleWsq()
        val pngData = convertor.convert(wsqData)
        val imageInfo = ImageParser.readImageInfo(pngData)
        imageInfo.compressionAlgorithm shouldBe CompressionAlgorithm.PNG
        imageInfo.width shouldBe 545
        imageInfo.height shouldBe 622
        imageInfo.colorSpace shouldBe "GRAY"
        imageInfo.pixelDepth shouldBe 8

        val pngImage = pngData.inputStream().use { assertNotNull(ImageIO.read(it)) }
        val pngPixels =
            pngImage.raster.getDataElements(0, 0, pngImage.width, pngImage.height, null) as ByteArray
        // Reference checksum of raw grayscale pixels decoded by JNBIS (not a test dependency).
        HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(pngPixels)) shouldBe
            "9a264e44ae0d89aae020810e0fce755c69baa9c7eb8e3d3e67f256adb99eb1df"
    }

    @Test
    fun `it rejects empty and non WSQ data`() {
        assertFailsWith<NistException> { convertor.convert(byteArrayOf()) }
        assertFailsWith<NistException> { convertor.convert("not a WSQ image".toByteArray()) }
    }

    @Test
    fun `it reports truncated WSQ data with the decoding cause`() {
        val truncatedData = sampleWsq().copyOf(20)
        val exception = assertFailsWith<NistException> { convertor.convert(truncatedData) }
        assertNotNull(exception.cause)
    }

    @Test
    fun `it reconstructs even image dimensions from a second WSQ fixture`() {
        val wsq = assertNotNull(javaClass.getResourceAsStream("/img/sample2.wsq")).use { it.readAllBytes() }
        val image = convertor.convert(wsq).inputStream().use { assertNotNull(ImageIO.read(it)) }
        val dimensions = WsqParser.readWSQInfo(wsq)
        image.width shouldBe dimensions.width
        image.height shouldBe dimensions.height
        val pixels = image.raster.getDataElements(0, 0, image.width, image.height, null) as ByteArray
        HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(pixels)) shouldBe
            "8f3f791986607fc95e3c29872a877e4fd84da8ec48a1fe37f0b7fe67162dee12"
    }

    @Test
    fun `it rejects truncation in tables and compressed image data`() {
        val wsq = sampleWsq()
        for (length in listOf(4, 100, wsq.size / 2, wsq.size - 2, wsq.size - 1)) {
            val exception = assertFailsWith<NistException> { convertor.convert(wsq.copyOf(length)) }
            assertNotNull(exception.cause)
        }
    }

    @Test
    fun `it rejects invalid segment lengths and excessive dimensions`() {
        val wsq = sampleWsq()
        // First segment follows the two-byte SOI marker.
        val invalidLength =
            wsq.copyOf().also {
                it[4] = 0
                it[5] = 1
            }
        assertFailsWith<NistException> { convertor.convert(invalidLength) }
        val frame =
            (0 until wsq.size - 1).first {
                wsq[it] == 0xff.toByte() && wsq[it + 1] == 0xa2.toByte()
            }
        val oversized =
            wsq.copyOf().also {
                for (index in frame + 6..frame + 9) it[index] = 0xff.toByte()
            }
        val exception = assertFailsWith<NistException> { convertor.convert(oversized) }
        assertNotNull(exception.cause)
    }

    private fun sampleWsq(): ByteArray = assertNotNull(javaClass.getResourceAsStream("/img/sample.wsq")).use { it.readAllBytes() }
}
