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

import eu.aagsolutions.img.nbis.converters.Converters.BYTE_MASK
import eu.aagsolutions.img.nbis.converters.EndianReader.readUInt16BigEndian
import eu.aagsolutions.img.nbis.model.enums.reference.CompressionAlgorithm

object JpegParser {
    private const val JP2_SIGNATURE_SIZE = 12
    private const val INCH_TO_CM_MULTIPLIER = 2.54
    private const val DEFAULT_DPI = 72

    /**
     * Reads JPEG image information including dimensions, bit depth, and DPI.
     */
    @Suppress("MagicNumber", "CyclomaticComplexMethod", "LongMethod", "NestedBlockDepth")
    fun readJpegInfo(jpegData: ByteArray): ImageInfo {
        var offset = 2 // Skip initial FF D8
        var width = 0
        var height = 0
        var bitsPerComponent = 8
        var numComponents = 1
        var pixelsPerInchX = DEFAULT_DPI // Default DPI
        var pixelsPerInchY = DEFAULT_DPI
        var densityUnit: Int
        var isLossless = false

        while (offset < jpegData.size - 2) {
            if (jpegData[offset] == 0xFF.toByte()) {
                val marker = jpegData[offset + 1].toInt() and BYTE_MASK

                when (marker) {
                    0xC0, 0xC1, 0xC2 -> { // SOF0, SOF1, SOF2 (Baseline, Extended, Progressive)
                        bitsPerComponent = jpegData[offset + 4].toInt() and BYTE_MASK
                        height = readUInt16BigEndian(jpegData, offset + 5)
                        width = readUInt16BigEndian(jpegData, offset + 7)
                        numComponents = jpegData[offset + 9].toInt() and BYTE_MASK
                        isLossless = false
                    }
                    0xC3, 0xC7, 0xCB, 0xCF -> { // Lossless JPEG markers
                        readUInt16BigEndian(jpegData, offset + 2)
                        bitsPerComponent = jpegData[offset + 4].toInt() and BYTE_MASK
                        height = readUInt16BigEndian(jpegData, offset + 5)
                        width = readUInt16BigEndian(jpegData, offset + 7)
                        numComponents = jpegData[offset + 9].toInt() and BYTE_MASK
                        isLossless = true
                    }
                    0xE0 -> { // APP0 segment (JFIF)
                        val segmentLength = readUInt16BigEndian(jpegData, offset + 2)
                        if (segmentLength >= 16) {
                            // Check for JFIF identifier
                            val jfifId = String(jpegData.sliceArray(offset + 4 until offset + 9), Charsets.US_ASCII)
                            if (jfifId == "JFIF\u0000") {
                                densityUnit = jpegData[offset + 11].toInt() and 0xFF
                                val xDensity = readUInt16BigEndian(jpegData, offset + 12)
                                val yDensity = readUInt16BigEndian(jpegData, offset + 14)

                                when (densityUnit) {
                                    1 -> { // Dots per inch
                                        pixelsPerInchX = xDensity
                                        pixelsPerInchY = yDensity
                                    }
                                    2 -> { // Dots per cm
                                        pixelsPerInchX = (xDensity * INCH_TO_CM_MULTIPLIER).toInt()
                                        pixelsPerInchY = (yDensity * INCH_TO_CM_MULTIPLIER).toInt()
                                    }
                                }
                            }
                        }
                    }
                    0xE1 -> { // APP1 segment (EXIF)
                        val segmentLength = readUInt16BigEndian(jpegData, offset + 2)
                        // Could parse EXIF data for more detailed DPI information
                        // This is more complex and optional
                    }
                    0xD9 -> { // EOI (End of Image)
                        break
                    }
                }

                if (marker in 0xC0..0xCF || marker in 0xE0..0xEF) {
                    val segmentLength = readUInt16BigEndian(jpegData, offset + 2)
                    offset += 2 + segmentLength
                } else {
                    offset += 2
                }
            } else {
                offset++
            }
        }

        val colorSpace = extractJpegColorType(numComponents)

        val totalBitDepth = bitsPerComponent * numComponents

        return ImageInfo(
            width = width,
            height = height,
            pixelDepth = totalBitDepth,
            pixelsPerInchX = pixelsPerInchX,
            pixelsPerInchY = pixelsPerInchY,
            colorSpace = colorSpace,
            compressionAlgorithm = if (isLossless) CompressionAlgorithm.JPEGL else CompressionAlgorithm.JPEGB,
        )
    }

    @Suppress("MagicNumber")
    private fun extractJpegColorType(colorType: Int): String =
        mapOf(
            1 to "GRAY",
            3 to "RGB",
            4 to "CMYK",
        )[colorType] ?: "UNKNOWN"

    fun isJpegBaseline(data: ByteArray): Boolean = containsMarker(data, byteArrayOf(0xFF.toByte(), 0xC0.toByte()))

    /**
     * Checks if the byte array represents a lossless JPEG image.
     */
    @Suppress("MagicNumber", "ComplexCondition")
    fun isJpegLossless(data: ByteArray): Boolean {
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
    fun isJpeg2000(data: ByteArray): Boolean {
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
}
