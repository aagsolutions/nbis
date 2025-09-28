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
import javax.imageio.ImageIO

private const val JP2_SIGNATURE_SIZE = 12
private const val PNG_SIGNATURE_SIZE = 8

/**
 * Detects the compression algorithm from image byte data by examining file headers.
 * Returns the appropriate NIST CompressionAlgorithm enum value.
 *
 * @param imageData The byte array containing the image data
 * @return CompressionAlgorithm enum value, or NONE if uncompressed/unknown
 */
fun detectCompressionAlgorithm(imageData: ByteArray): CompressionAlgorithm {
    if (imageData.isEmpty()) {
        return CompressionAlgorithm.NONE
    }

    return when {
        isJpegBaseline(imageData) -> CompressionAlgorithm.JPEGB
        isJpegLossless(imageData) -> CompressionAlgorithm.JPEGL
        isJpeg2000(imageData) -> CompressionAlgorithm.JP2
        isJpeg2000Lossless(imageData) -> CompressionAlgorithm.JP2L
        isPng(imageData) -> CompressionAlgorithm.PNG
        isWsq(imageData) -> CompressionAlgorithm.WSQ20
        else -> CompressionAlgorithm.NONE
    }
}

/**
 * Checks if the byte array represents a baseline JPEG image.
 */
@Suppress("MagicNumber", "ComplexCondition")
private fun isJpegBaseline(data: ByteArray): Boolean {
    if (data.size < 4 || data[0] != 0xFF.toByte() || data[1] != 0xD8.toByte() || data[2] != 0xFF.toByte()) {
        return false
    }

    return containsMarker(data, byteArrayOf(0xFF.toByte(), 0xC0.toByte()))
}

/**
 * Checks if the byte array represents a lossless JPEG image.
 */
@Suppress("MagicNumber", "ComplexCondition")
private fun isJpegLossless(data: ByteArray): Boolean {
    if (data.size < 4 || data[0] != 0xFF.toByte() || data[1] != 0xD8.toByte() || data[2] != 0xFF.toByte()) {
        return false
    }

    return containsMarker(data, byteArrayOf(0xFF.toByte(), 0xC3.toByte())) ||
        containsMarker(data, byteArrayOf(0xFF.toByte(), 0xC7.toByte())) ||
        containsMarker(data, byteArrayOf(0xFF.toByte(), 0xCB.toByte())) ||
        containsMarker(data, byteArrayOf(0xFF.toByte(), 0xCF.toByte()))
}

/**
 * Checks if the byte array represents a JPEG 2000 image.
 */
@Suppress("MagicNumber")
private fun isJpeg2000(data: ByteArray): Boolean {
    if (data.size < JP2_SIGNATURE_SIZE) return false

    val jp2Signature =
        byteArrayOf(
            0x00,
            0x00,
            0x00,
            0x0C,
            0x6A,
            0x50,
            0x20,
            0x20,
            0x0D,
            0x0A,
            0x87.toByte(),
            0x0A,
        )

    return data.take(JP2_SIGNATURE_SIZE).toByteArray().contentEquals(jp2Signature)
}

/**
 * Checks if the byte array represents a lossless JPEG 2000 image.
 * This is more complex and typically requires examining the coding parameters.
 */
private fun isJpeg2000Lossless(data: ByteArray): Boolean {
    if (!isJpeg2000(data)) return false
    return containsMarker(data, byteArrayOf(0xFF.toByte(), 0x52.toByte()))
}

/**
 * Checks if the byte array represents a PNG image.
 */
@Suppress("MagicNumber")
private fun isPng(data: ByteArray): Boolean {
    if (data.size < PNG_SIGNATURE_SIZE) return false

    val pngSignature =
        byteArrayOf(
            0x89.toByte(),
            0x50,
            0x4E,
            0x47,
            0x0D,
            0x0A,
            0x1A,
            0x0A,
        )

    return data.take(PNG_SIGNATURE_SIZE).toByteArray().contentEquals(pngSignature)
}

/**
 * Checks if the byte array represents a WSQ (Wavelet Scalar Quantization) image.
 * WSQ is primarily used for FBI fingerprint images.
 */
@Suppress("MagicNumber")
private fun isWsq(data: ByteArray): Boolean {
    if (data.size < 4) return false

    return (data[0] == 0xFF.toByte() && data[1] == 0xA0.toByte()) ||
        containsMarker(data, byteArrayOf(0xFF.toByte(), 0xA0.toByte()))
}

/**
 * Helper function to search for a byte pattern within the data.
 */
private fun containsMarker(
    data: ByteArray,
    marker: ByteArray,
): Boolean =
    if (data.size < marker.size) {
        false
    } else {
        (0..data.size - marker.size).any { i ->
            marker.indices.all { j -> data[i + j] == marker[j] }
        }
    }

/**
 * Alternative method using Java's ImageIO for more robust detection.
 * This method is more reliable but requires the javax.imageio package.
 */
fun detectCompressionAlgorithmUsingImageIO(imageData: ByteArray): CompressionAlgorithm {
    imageData.inputStream().use { inputStream ->
        {
            ImageIO.createImageInputStream(inputStream).use { imageInputStream ->
                val readers = ImageIO.getImageReaders(imageInputStream)
                if (readers.hasNext()) {
                    val reader = readers.next()
                    val formatName = reader.formatName.uppercase()
                    when {
                        formatName.contains("JPEG") -> {
                            CompressionAlgorithm.JPEGB
                        }
                        formatName.contains("PNG") -> CompressionAlgorithm.PNG
                        formatName.contains("JP2") || formatName.contains("JPEG2000") -> CompressionAlgorithm.JP2
                        else -> CompressionAlgorithm.NONE
                    }
                }
            }
        }
    }
    return detectCompressionAlgorithm(imageData)
}
