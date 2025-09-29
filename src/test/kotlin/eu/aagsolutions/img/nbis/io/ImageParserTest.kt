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

import eu.aagsolutions.img.nbis.model.enums.reference.CompressionAlgorithm
import io.kotest.matchers.shouldBe
import org.junit.Test

class ImageParserTest {
    @Test
    fun `it should successful detect JPGB image properties`() {
        val url = ImageParserTest::class.java.getResource("/img/mugshot-1024x1024.jpg")
        val faceImage = url!!.openStream().use { inputStream -> inputStream.readAllBytes() }
        val imageInfo = ImageParser.readImageInfo(faceImage)
        imageInfo.apply {
            compressionAlgorithm shouldBe CompressionAlgorithm.JPEGB
            width shouldBe 1024
            height shouldBe 959
            pixelsPerInchX shouldBe 300
            pixelsPerInchY shouldBe 300
            colorSpace shouldBe "RGB"
            pixelDepth shouldBe 24
        }
    }

    @Test
    fun `it should successful detect PNG image properties`() {
        val url = ImageParserTest::class.java.getResource("/img/fp-1.png")
        val faceImage = url!!.openStream().use { inputStream -> inputStream.readAllBytes() }
        val imageInfo = ImageParser.readImageInfo(faceImage)
        imageInfo.apply {
            compressionAlgorithm shouldBe CompressionAlgorithm.PNG
            width shouldBe 804
            height shouldBe 752
            pixelsPerInchX shouldBe 72
            pixelsPerInchY shouldBe 72
            colorSpace shouldBe "RGB"
            pixelDepth shouldBe 24
        }
    }

    @Test
    fun `it should successful detect WSQ image properties`() {
        val url = ImageParserTest::class.java.getResource("/img/sample.wsq")
        val faceImage = url!!.openStream().use { inputStream -> inputStream.readAllBytes() }
        val imageInfo = ImageParser.readImageInfo(faceImage)
        imageInfo.apply {
            compressionAlgorithm shouldBe CompressionAlgorithm.WSQ20
            width shouldBe 545
            height shouldBe 622
            pixelsPerInchX shouldBe 24
            pixelsPerInchY shouldBe 24
            colorSpace shouldBe "GRAY"
            pixelDepth shouldBe 8
        }
    }
}
