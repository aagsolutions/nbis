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
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO

/** Converts WSQ fingerprint images to lossless, eight-bit grayscale PNG images. */
class WsqToPngConvertor {
    /**
     * Decodes [wsqData] using WSQ Huffman, quantization and wavelet tables.
     * Malformed tables retain their validation or array-access error as the cause.
     * @throws NistException when the input is invalid or truncated.
     */
    @Suppress("TooGenericExceptionCaught")
    fun convert(wsqData: ByteArray): ByteArray {
        if (!WsqParser.isWsq(wsqData)) {
            throw NistException("Invalid WSQ data: missing start-of-image marker")
        }
        try {
            val decoded = WsqDecoder().decode(wsqData)
            val image = BufferedImage(decoded.width, decoded.height, BufferedImage.TYPE_BYTE_GRAY)
            image.raster.setDataElements(0, 0, decoded.width, decoded.height, decoded.pixels)
            return ByteArrayOutputStream().use { output ->
                check(ImageIO.write(image, "png", output)) { "No PNG writer available" }
                output.toByteArray()
            }
        } catch (exception: Exception) {
            throw NistException("Unable to convert WSQ image to PNG", exception)
        }
    }
}
