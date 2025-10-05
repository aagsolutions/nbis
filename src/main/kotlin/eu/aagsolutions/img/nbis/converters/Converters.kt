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

package eu.aagsolutions.img.nbis.converters

import eu.aagsolutions.img.nbis.io.RECORD_SEPARATOR
import eu.aagsolutions.img.nbis.io.UNIT_SEPARATOR
import eu.aagsolutions.img.nbis.model.NistEntry

object Converters {
    const val SHIFT_WITH_EIGHT_BYTES = 8
    const val SHIFT_WITH_SIXTEEN_BYTES = 16
    const val SHIFT_WITH_TWENTYFOUR_BYTES = 24
    const val MAX_NR_OF_BYTES = 8
    const val BYTE_MASK = 0xFF

    /**
     * Converts a given byte value to its string representation.
     *
     * @param byteValue The byte value to be converted to a string.
     */
    fun byteToString(byteValue: Byte) = byteValue.toUByte().toString()

    /**
     * Convert a byte array to a hexadecimal string.
     * @param bytes
     * @return string representation
     */
    fun bytesToHex(bytes: ByteArray): String =
        bytes.joinToString("") { byte ->
            val hex = byte.toInt() and BYTE_MASK
            String.format("%02x", hex)
        }

    fun twoBytesToLong(
        buffer: ByteArray,
        offset: Int,
    ): Long {
        val byte1 = buffer[offset]
        val byte2 = buffer[offset + 1]
        return ((byte1.toInt() shl SHIFT_WITH_EIGHT_BYTES) or (byte2.toInt() and BYTE_MASK)).toLong()
    }

    @Suppress("MagicNumber")
    fun fourBytesToLong(
        buffer: ByteArray,
        offset: Int,
    ): Long {
        val byte1 = buffer[offset]
        val byte2 = buffer[offset + 1]
        val byte3 = buffer[offset + 2]
        val byte4 = buffer[offset + 3]

        return (
            (byte1.toInt() shl SHIFT_WITH_TWENTYFOUR_BYTES) or
                ((byte2.toInt() and BYTE_MASK) shl SHIFT_WITH_SIXTEEN_BYTES) or
                ((byte3.toInt() and BYTE_MASK) shl SHIFT_WITH_EIGHT_BYTES) or
                (byte4.toInt() and BYTE_MASK)
        ).toLong()
    }

    /**
     * Converts a long value into a byte array representation using a specified number of bytes.
     *
     * @param value The long value to be converted into bytes.
     * @param numBytes The number of bytes to use in the conversion. Must be between 1 and 8.
     * @return A byte array representing the given long value in the specified number of bytes.
     * @throws IllegalArgumentException If the number of bytes is not between 1 and 8.
     */
    fun longToBytes(
        value: Long,
        numBytes: Int,
    ): ByteArray {
        require(numBytes in 1..MAX_NR_OF_BYTES) { "Number of bytes must be between 1 and $MAX_NR_OF_BYTES." }
        val byteArray = ByteArray(numBytes)
        for (i in 0 until numBytes) {
            val shift = SHIFT_WITH_EIGHT_BYTES * (numBytes - 1 - i)
            byteArray[i] = (value shr shift).toByte()
        }
        return byteArray
    }

    /**
     * Converts a list of strings into a single string using a specific separator.
     *
     * The method joins all the elements of the provided list into a single string,
     * separated by the `UNIT_SEPARATOR` character. If the list is empty, an empty
     * string is returned.
     *
     * @param items The list of strings to be concatenated.
     * @return The concatenated string formed by joining the list elements with the separator,
     * or an empty string if the list is empty.
     */
    fun listToString(items: List<String>): String =
        when {
            items.isEmpty() -> ""
            else -> items.joinToString(separator = UNIT_SEPARATOR.toString())
        }

    /**
     * Converts a delimited string into a mutable list of strings.
     *
     * This method takes a string of items separated by ASCII unit separator,
     * splits the string into individual elements, and returns them as a mutable list.
     * If the input string is null, an empty mutable list is returned.
     *
     * @param items The input string containing items separated by a delimiter. It may be null.
     * @return A mutable list of strings containing the split items. Returns an empty list if the input is null.
     */
    fun stringToList(items: String?): MutableList<String?> {
        if (items == null) {
            return mutableListOf()
        }
        return mutableListOf(
            *items
                .split(
                    UNIT_SEPARATOR.toString().toRegex(),
                ).dropLastWhile { it.isEmpty() }
                .toTypedArray(),
        )
    }

    /**
     * Converts the FILE CONTENT string from Transaction information record to a list of NistEntry objects.
     *
     * @param items A string containing multiple entries separated by record separators. Each entry represents
     *              a key-value pair, potentially separated by a unit separator.
     * @return A list of `NistEntry` objects, where each entry in the input string is converted into an object
     *         with a key and value.
     */
    fun stringToListOfNistEntries(items: String): List<NistEntry> =
        splitStringByRecordSeparator(items)
            .map { stringToNistEntry(it) }
            .toList()

    private fun splitStringByRecordSeparator(subfieldString: String): Array<String> =
        subfieldString
            .split(RECORD_SEPARATOR.toString().toRegex())
            .dropLastWhile { it.isEmpty() }
            .toTypedArray()

    private fun stringToNistEntry(value: String): NistEntry {
        val parts = value.split(UNIT_SEPARATOR.toString().toRegex())
        return if (parts.size > 1) {
            NistEntry(parts[0], parts[1])
        } else {
            NistEntry(parts[0], "")
        }
    }
}
