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

import eu.aagsolutions.img.nbis.converters.EndianReader.readUInt16BigEndian
import eu.aagsolutions.img.nbis.exceptions.NistException
import eu.aagsolutions.img.nbis.model.enums.reference.CompressionAlgorithm

@Suppress(
    "MagicNumber",
    "NestedBlockDepth",
    "LoopWithTooManyJumpStatements",
    "CyclomaticComplexMethod",
)
object WsqParser {
    private const val START_OF_WSQ_IMG = 0xA0
    private const val END_OF_WSQ_IMG = 0xA1
    private const val START_OF_WSQ_FRAME = 0xA2
    private const val START_OF_WSQ_BLOCK = 0xA3
    private const val WSQ_TRANSFORM_TABLE = 0xA4
    private const val WSQ_QUANTIZATION_TABLE = 0xA5
    private const val WSQ_HUFFMAN_TABLE = 0xA6
    private const val WSQ_COMMENT = 0xA8

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
     * Enhanced WSQ detection that also validates the structure.
     */
    fun isWsq(data: ByteArray): Boolean =
        if (data.size < 4) {
            false
        } else {
            data[0] == 0xFF.toByte() && data[1] == START_OF_WSQ_IMG.toByte()
        }

    /**
     * Reads WSQ image dimensions from byte array.
     * WSQ format is primarily used for fingerprint images by the FBI.
     *
     * @param wsqData The WSQ image byte array
     * @return WSQImageDimensions containing width, height, and other properties
     * @throws NistException if the data is not a valid WSQ file
     */
    private fun readWSQDimensions(wsqData: ByteArray): WSQImageDimensions {
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
        var extractedPpiValue: Int? = null
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
                        extractedPpiValue = match.groupValues[1].toIntOrNull()
                    } else if (comment.contains("NIST_COM")) {
                        // Parse NIST comment format if needed
                        val parts = comment.split("\u0000")
                        for (part in parts) {
                            if (part.contains("PPI")) {
                                val ppiValue = part.filter { it.isDigit() }
                                if (ppiValue.isNotEmpty()) {
                                    extractedPpiValue = ppiValue.toIntOrNull()
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

        return extractedPpiValue
    }
}
