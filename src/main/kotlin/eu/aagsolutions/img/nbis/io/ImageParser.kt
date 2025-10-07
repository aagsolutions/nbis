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

@Suppress(
    "MagicNumber",
    "CyclomaticComplexMethod",
    "ComplexCondition",
    "LongMethod",
    "NestedBlockDepth",
    "TooManyFunctions",
    "LoopWithTooManyJumpStatements",
    "ReturnCount",
)
object ImageParser {
    private const val JP2_SIGNATURE_SIZE = 12
    private const val PNG_SIGNATURE_SIZE = 8
    private const val START_OF_WSQ_IMG = 0xA0
    private const val END_OF_WSQ_IMG = 0xA1
    private const val START_OF_WSQ_FRAME = 0xA2
    private const val START_OF_WSQ_BLOCK = 0xA3
    private const val WSQ_TRANSFORM_TABLE = 0xA4
    private const val WSQ_QUANTIZATION_TABLE = 0xA5
    private const val WSQ_HUFFMAN_TABLE = 0xA6
    private const val WSQ_COMMENT = 0xA8

    /**
     * Reads comprehensive image information from byte array for JPEG, PNG, and WSQ formats.
     */
    fun readImageInfo(imageData: ByteArray): ImageInfo =
        when {
            isPng(imageData) -> readPngInfo(imageData)
            isJpegBaseline(imageData) || isJpegLossless(imageData) -> readJpegInfo(imageData)
            isWsq(imageData) -> readWSQInfo(imageData)
            else -> throw NistException("Unsupported image format")
        }

    /**
     * Reads PNG image information including dimensions, bit depth, and DPI.
     */
    fun readPngInfo(pngData: ByteArray): ImageInfo {
        if (!isPng(pngData)) {
            throw NistException("Not a valid PNG file")
        }

        var offset = 8 // Skip PNG signature
        var width = 0
        var height = 0
        var bitDepth = 8
        var colorType = 0
        var pixelsPerInchX = 72 // Default DPI
        var pixelsPerInchY = 72
        var pixelAspectRatio = 1.0

        while (offset < pngData.size - 8) {
            // Read chunk length
            val chunkLength = readUInt32BigEndian(pngData, offset)
            val chunkType = String(pngData.sliceArray(offset + 4 until offset + 8), Charsets.US_ASCII)

            when (chunkType) {
                "IHDR" -> {
                    // Image Header chunk
                    width = readUInt32BigEndian(pngData, offset + 8)
                    height = readUInt32BigEndian(pngData, offset + 12)
                    bitDepth = pngData[offset + 16].toInt() and 0xFF
                    colorType = pngData[offset + 17].toInt() and 0xFF
                }
                "pHYs" -> {
                    // Physical pixel dimensions chunk
                    val pixelsPerUnitX = readUInt32BigEndian(pngData, offset + 8)
                    val pixelsPerUnitY = readUInt32BigEndian(pngData, offset + 12)
                    val unitSpecifier = pngData[offset + 16].toInt() and 0xFF

                    if (unitSpecifier == 1) { // Meters
                        pixelsPerInchX = (pixelsPerUnitX * 0.0254).toInt() // Convert to inches
                        pixelsPerInchY = (pixelsPerUnitY * 0.0254).toInt()
                    }
                    pixelAspectRatio = pixelsPerUnitX.toDouble() / pixelsPerUnitY.toDouble()
                }
                "IEND" -> {
                    break // End of PNG
                }
            }

            offset += 12 + chunkLength // Move to next chunk (4 + 4 + length + 4 CRC)
        }

        val colorSpace =
            when (colorType) {
                0 -> "GRAY" // Grayscale
                2 -> "RGB" // RGB
                3 -> "INDEXED" // Palette
                4 -> "GRAYA" // Grayscale + Alpha
                6 -> "RGBA" // RGB + Alpha
                else -> "UNKNOWN"
            }

        val totalBitDepth =
            when (colorType) {
                0 -> bitDepth // Grayscale
                2 -> bitDepth * 3 // RGB
                3 -> bitDepth // Indexed (palette)
                4 -> bitDepth * 2 // Grayscale + Alpha
                6 -> bitDepth * 4 // RGBA
                else -> bitDepth
            }

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

    /**
     * Reads JPEG image information including dimensions, bit depth, and DPI.
     */
    fun readJpegInfo(jpegData: ByteArray): ImageInfo {
        if (!isJpegBaseline(jpegData) && !isJpegLossless(jpegData)) {
            throw NistException("Not a valid JPEG file")
        }

        var offset = 2 // Skip initial FF D8
        var width = 0
        var height = 0
        var bitsPerComponent = 8
        var numComponents = 1
        var pixelsPerInchX = 72 // Default DPI
        var pixelsPerInchY = 72
        var densityUnit = 0
        var isLossless = false

        while (offset < jpegData.size - 2) {
            if (jpegData[offset] == 0xFF.toByte()) {
                val marker = jpegData[offset + 1].toInt() and 0xFF

                when (marker) {
                    0xC0, 0xC1, 0xC2 -> { // SOF0, SOF1, SOF2 (Baseline, Extended, Progressive)
                        val segmentLength = readUInt16BigEndian(jpegData, offset + 2)
                        bitsPerComponent = jpegData[offset + 4].toInt() and 0xFF
                        height = readUInt16BigEndian(jpegData, offset + 5)
                        width = readUInt16BigEndian(jpegData, offset + 7)
                        numComponents = jpegData[offset + 9].toInt() and 0xFF
                        isLossless = false
                    }
                    0xC3, 0xC7, 0xCB, 0xCF -> { // Lossless JPEG markers
                        readUInt16BigEndian(jpegData, offset + 2)
                        bitsPerComponent = jpegData[offset + 4].toInt() and 0xFF
                        height = readUInt16BigEndian(jpegData, offset + 5)
                        width = readUInt16BigEndian(jpegData, offset + 7)
                        numComponents = jpegData[offset + 9].toInt() and 0xFF
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
                                        pixelsPerInchX = (xDensity * 2.54).toInt()
                                        pixelsPerInchY = (yDensity * 2.54).toInt()
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

        val colorSpace =
            when (numComponents) {
                1 -> "GRAY"
                3 -> "RGB"
                4 -> "CMYK"
                else -> "UNKNOWN"
            }

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

    /**
     * Enhanced WSQ info reader with complete metadata
     */
    fun readWSQInfo(wsqData: ByteArray): ImageInfo {
        val dimensions = readWSQDimensions(wsqData)

        return ImageInfo(
            width = dimensions.width,
            height = dimensions.height,
            pixelDepth = dimensions.pixelDepth,
            pixelsPerInchX = dimensions.pixelsPerInch,
            pixelsPerInchY = dimensions.pixelsPerInch,
            colorSpace = "GRAY",
            compressionAlgorithm = CompressionAlgorithm.WSQ20,
        )
    }

    /**
     * Helper function to read 32-bit unsigned integer in big-endian format.
     */
    private fun readUInt32BigEndian(
        data: ByteArray,
        offset: Int,
    ): Int {
        if (offset + 3 >= data.size) {
            throw IndexOutOfBoundsException("Cannot read 32-bit value at offset $offset")
        }
        return ((data[offset].toInt() and 0xFF) shl 24) or
            ((data[offset + 1].toInt() and 0xFF) shl 16) or
            ((data[offset + 2].toInt() and 0xFF) shl 8) or
            (data[offset + 3].toInt() and 0xFF)
    }

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
     * Reads WSQ image dimensions from byte array.
     * WSQ format is primarily used for fingerprint images by the FBI.
     *
     * @param wsqData The WSQ image byte array
     * @return WSQImageDimensions containing width, height, and other properties
     * @throws NistException if the data is not a valid WSQ file
     */
    fun readWSQDimensions(wsqData: ByteArray): WSQImageDimensions {
        (wsqData.size > 4 && isWsq(wsqData)).let {
            var offset = 0

            // Parse WSQ segments to find the Frame Header (FRH) segment
            while (offset < wsqData.size - 4) {
                // Check for segment marker (0xFF followed by segment type)
                if (wsqData[offset] == 0xFF.toByte()) {
                    val segmentType = wsqData[offset + 1].toInt() and 0xFF

                    when (segmentType) {
                        START_OF_WSQ_IMG -> { // SOI (Start of Image)
                            offset += 2
                            continue
                        }

                        END_OF_WSQ_IMG -> { // EOI (End of Image)
                            break
                        }

                        START_OF_WSQ_FRAME -> { // SOF (Start of Frame) - This contains dimensions
                            return parseWSQFrameHeader(wsqData, offset)
                        }

                        START_OF_WSQ_BLOCK -> { // SOB (Start of Block)
                            // Skip this segment
                            val segmentLength = readUInt16BigEndian(wsqData, offset + 2)
                            offset += 2 + segmentLength
                        }

                        WSQ_TRANSFORM_TABLE -> { // DTT (Define Transform Table)
                            val segmentLength = readUInt16BigEndian(wsqData, offset + 2)
                            offset += 2 + segmentLength
                        }

                        WSQ_QUANTIZATION_TABLE -> { // DQT (Define Quantization Table)
                            val segmentLength = readUInt16BigEndian(wsqData, offset + 2)
                            offset += 2 + segmentLength
                        }

                        WSQ_HUFFMAN_TABLE -> { // DHT (Define Huffman Table)
                            val segmentLength = readUInt16BigEndian(wsqData, offset + 2)
                            offset += 2 + segmentLength
                        }

                        WSQ_COMMENT -> { // COM (Comment)
                            val segmentLength = readUInt16BigEndian(wsqData, offset + 2)
                            offset += 2 + segmentLength
                        }

                        else -> {
                            // Unknown segment, try to skip
                            if (offset + 4 < wsqData.size) {
                                val segmentLength = readUInt16BigEndian(wsqData, offset + 2)
                                offset += 2 + segmentLength
                            } else {
                                offset++
                            }
                        }
                    }
                } else {
                    offset++
                }
            }
        }
        throw NistException("Invalid WSQ data")
    }

    /**
     * Parses the WSQ Start of Frame (SOF) segment to extract image dimensions.
     *
     * @param data The WSQ byte array
     * @param offset The offset to the SOF segment
     */
    private fun parseWSQFrameHeader(
        data: ByteArray,
        offset: Int,
    ): WSQImageDimensions {
        if (offset + 12 >= data.size) {
            throw NistException("Invalid WSQ Frame Header: insufficient data")
        }

        // WSQ SOF structure:
        // 0-1: Marker (0xFF 0xA2)
        // 2-3: Length of segment
        // 4: Black value (0)
        // 5: Precision (must be 8)
        // 6-7: Height (big-endian)
        // 8-9: Width (big-endian)
        // 10: Number of components (must be 1 for grayscale)
        // 11+: Component specifications

        val segmentLength = readUInt16BigEndian(data, offset + 2)

        val height = readUInt16BigEndian(data, offset + 6)
        val width = readUInt16BigEndian(data, offset + 8)
        val numComponents = data[offset + 10].toInt() and 0xFF

        // Try to find PPI in comments, default to 500 if not found
        val ppi = findWSQPixelsPerInch(data) ?: 500

        return WSQImageDimensions(
            width = width,
            height = height,
            pixelDepth = 8,
            pixelsPerInch = ppi,
        )
    }

    /**
     * Attempts to find PPI information from WSQ comment segments.
     *
     * @param data The WSQ byte array
     */
    private fun findWSQPixelsPerInch(data: ByteArray): Int? {
        var offset = 0

        while (offset < data.size - 4) {
            if (data[offset] == 0xFF.toByte() && data[offset + 1] == 0xA8.toByte()) { // COM segment
                val segmentLength = readUInt16BigEndian(data, offset + 2)
                if (offset + 4 + segmentLength <= data.size) {
                    val commentBytes = data.sliceArray(offset + 4 until offset + 4 + segmentLength - 2)
                    val comment = String(commentBytes, Charsets.US_ASCII)

                    // Look for PPI information in the comment
                    val ppiRegex = Regex("""\bPPI\s+(\d+)""", RegexOption.IGNORE_CASE)
                    val match = ppiRegex.find(comment)
                    if (match != null) {
                        return match.groupValues[1].toIntOrNull()
                    }

                    // Alternative format: "NIST_COM" followed by PPI
                    if (comment.contains("NIST_COM")) {
                        // Parse NIST comment format if needed
                        val parts = comment.split("\u0000") // Null-separated values
                        for (part in parts) {
                            if (part.contains("PPI")) {
                                val ppiValue = part.filter { it.isDigit() }
                                if (ppiValue.isNotEmpty()) {
                                    return ppiValue.toIntOrNull()
                                }
                            }
                        }
                    }
                }
                offset += 4 + segmentLength - 2
            } else {
                offset++
            }
        }

        return null
    }

    /**
     * Reads a 16-bit unsigned integer in big-endian format.
     */
    private fun readUInt16BigEndian(
        data: ByteArray,
        offset: Int,
    ): Int {
        if (offset + 1 >= data.size) {
            throw IndexOutOfBoundsException("Cannot read 16-bit value at offset $offset")
        }
        return ((data[offset].toInt() and 0xFF) shl 8) or (data[offset + 1].toInt() and 0xFF)
    }

    /**
     * Enhanced WSQ detection that also validates the structure.
     */
    private fun isWsq(data: ByteArray): Boolean {
        if (data.size < 4) return false

        // WSQ starts with SOI marker: 0xFF 0xA0
        return data[0] == 0xFF.toByte() && data[1] == START_OF_WSQ_IMG.toByte()
    }
}
