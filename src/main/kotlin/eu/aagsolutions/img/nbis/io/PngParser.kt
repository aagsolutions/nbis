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
import eu.aagsolutions.img.nbis.converters.EndianReader.readUInt32BigEndian
import eu.aagsolutions.img.nbis.model.enums.reference.CompressionAlgorithm

object PngParser {
    private const val PNG_SIGNATURE_SIZE = 8
    private const val DEFAULT_DPI = 72
    private const val DEFAULT_CHUNK_LENGTH = 12
    private const val METER_TO_INCH_DIVISOR = 0.0254
    private const val PNG_UNIT_SPECIFIER_OFFSET = 16
    private const val PNG_PIXEL_PER_UNIT_X_OFFSET = 8
    private const val PNG_PIXEL_PER_UNIT_Y_OFFSET = 12
    private const val PNG_PIXEL_DEPTH_OFFSET = 16
    private const val PNG_COLOR_TYPE_OFFSET = 17
    private const val PNG_WIDTH_OFFSET = 8
    private const val PNG_HEIGHT_OFFSET = 12
    private const val PNG_CHUNK_START_OFFSET = 4
    private const val PNG_CHUNK_END_OFFSET = 8
    private const val BIT_STEP = 8
    private const val DEFAULT_BIT_DEPTH = 8
    private const val START_OFFSET = 8

    @Suppress("MagicNumber")
    private val PNG_SIGNATURE: ByteArray =
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

    fun isPng(data: ByteArray): Boolean =
        if (data.size < PNG_SIGNATURE_SIZE) {
            false
        } else {
            data.take(PNG_SIGNATURE_SIZE).toByteArray().contentEquals(PNG_SIGNATURE)
        }

    /**
     * Reads PNG image information including dimensions, bit depth, and DPI.
     */
    fun readPngInfo(pngData: ByteArray): ImageInfo {
        var offset = START_OFFSET // Skip PNG signature
        var width = 0
        var height = 0
        var bitDepth = DEFAULT_BIT_DEPTH
        var colorType = 0
        var pixelsPerInchX = DEFAULT_DPI // Default DPI
        var pixelsPerInchY = DEFAULT_DPI
        var pixelAspectRatio = 1.0

        while (offset < pngData.size - BIT_STEP) {
            // Read chunk length
            val chunkLength = readUInt32BigEndian(pngData, offset)
            val chunkType =
                String(pngData.sliceArray(offset + PNG_CHUNK_START_OFFSET until offset + PNG_CHUNK_END_OFFSET), Charsets.US_ASCII)

            when (chunkType) {
                "IHDR" -> {
                    // Image Header chunk
                    width = readUInt32BigEndian(pngData, offset + PNG_WIDTH_OFFSET)
                    height = readUInt32BigEndian(pngData, offset + PNG_HEIGHT_OFFSET)
                    bitDepth = pngData[offset + PNG_PIXEL_DEPTH_OFFSET].toInt() and BYTE_MASK
                    colorType = pngData[offset + PNG_COLOR_TYPE_OFFSET].toInt() and BYTE_MASK
                }
                "pHYs" -> {
                    // Physical pixel dimensions chunk
                    val pixelsPerUnitX = readUInt32BigEndian(pngData, offset + PNG_PIXEL_PER_UNIT_X_OFFSET)
                    val pixelsPerUnitY = readUInt32BigEndian(pngData, offset + PNG_PIXEL_PER_UNIT_Y_OFFSET)
                    val unitSpecifier = pngData[offset + PNG_UNIT_SPECIFIER_OFFSET].toInt() and BYTE_MASK

                    if (unitSpecifier == 1) { // Meters
                        pixelsPerInchX = (pixelsPerUnitX * METER_TO_INCH_DIVISOR).toInt() // Convert to inches
                        pixelsPerInchY = (pixelsPerUnitY * METER_TO_INCH_DIVISOR).toInt()
                    }
                    pixelAspectRatio = pixelsPerUnitX.toDouble() / pixelsPerUnitY.toDouble()
                }
                "IEND" -> {
                    break // End of PNG
                }
            }

            offset += DEFAULT_CHUNK_LENGTH + chunkLength // Move to next chunk (4 + 4 + length + 4 CRC)
        }

        val colorSpace = extractPngColorType(colorType)

        val totalBitDepth = extractPngBitDepth(colorType, bitDepth)

        return ImageInfo(
            width = width,
            height = height,
            pixelDepth = totalBitDepth,
            pixelsPerInchX = pixelsPerInchX,
            pixelsPerInchY = pixelsPerInchY,
            colorSpace = colorSpace,
            compressionAlgorithm = CompressionAlgorithm.PNG,
        )
    }

    @Suppress("MagicNumber")
    private fun extractPngColorType(colorType: Int): String =
        mapOf(
            0 to "GRAY", // Grayscale
            2 to "RGB", // RGB
            3 to "INDEXED", // Palette
            4 to "GRAYA", // Grayscale + Alpha
            6 to "RGBA",
        )[colorType] ?: "UNKNOWN"

    @Suppress("MagicNumber")
    private fun extractPngBitDepth(
        colorType: Int,
        bitDepth: Int,
    ): Int =
        mapOf(
            0 to bitDepth, // Grayscale
            2 to bitDepth * 3, // RGB
            3 to bitDepth, // Indexed (palette)
            4 to bitDepth * 2, // Grayscale + Alpha
            6 to bitDepth * 4,
        )[colorType] ?: bitDepth
}
