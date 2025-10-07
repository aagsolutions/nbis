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

package eu.aagsolutions.img.nbis.io.record

import eu.aagsolutions.img.nbis.exceptions.NistException
import eu.aagsolutions.img.nbis.io.AsciiHelper.TAG_SEPARATOR_COLON
import eu.aagsolutions.img.nbis.io.AsciiHelper.TAG_SEPARATOR_DOT
import eu.aagsolutions.img.nbis.io.Tag
import eu.aagsolutions.img.nbis.io.Token
import eu.aagsolutions.img.nbis.model.enums.RecordType
import eu.aagsolutions.img.nbis.model.records.BaseRecord
import java.io.OutputStream
import java.nio.ByteBuffer

abstract class RecordHandler(
    open val recordType: RecordType,
) {
    abstract fun read(token: Token): BaseRecord

    abstract fun write(
        outputStream: OutputStream,
        record: BaseRecord,
    )

    /**
     * Extracts tag information based on the provided token input.
     *
     * This method parses the `type` and `field` components of a tag from the token's buffer,
     * separating them using predefined separators and converting the extracted contents to integers.
     *
     * @param token The token containing the input buffer, position, and decoding details.
     * @return A `Tag` object containing the extracted `type` and `field` as integers.
     */
    protected fun getTagInfo(token: Token): Tag {
        val type = nextWord(token, TAG_SEPARATOR_DOT, MAX_TYPE_SIZE)
        token.position++
        val field = nextWord(token, TAG_SEPARATOR_COLON, MAX_FIELD_SIZE)
        token.position++
        return Tag(type.replace(",", "").toInt(), field.toInt())
    }

    /**
     * Extracts the next word from the input buffer based on the specified separators and maximum length.
     *
     * This method reads from the provided token's buffer until it encounters one of the specified separator
     * characters or reaches the maximum allowed length. It then decodes the extracted bytes into a string
     * based on the token's charset decoder.
     *
     * @param token The token containing the input buffer and relevant decoding information.
     * @param sepList A character array specifying the separator characters.
     * @param maxLen The maximum length of the word to extract.
     * @return The next word extracted and decoded as a string.
     * @throws NistException If the token's buffer is null, the charset decoder is uninitialized, or a
     * character decoding error occurs during string conversion.
     */
    protected fun nextWord(
        token: Token,
        sepList: CharArray,
        maxLen: Int,
    ): String {
        val buffer = token.buffer
        var i = 0
        while (
            i < maxLen &&
            token.position < buffer.size &&
            !sepList.contains(buffer[token.position].toInt().toChar())
        ) {
            token.position++
            i++
        }
        val data = buffer.copyOfRange(token.position - i, token.position)
        return try {
            val decoded =
                token.charsetDecoder?.decode(ByteBuffer.wrap(data))
                    ?: throw NistException("CharsetDecoder is not initialized")
            decoded.toString()
        } catch (e: CharacterCodingException) {
            throw NistException("Exception when reading the record: ${e.message}", e)
        }
    }

    protected fun isPositionInIndex(token: Token): Boolean = token.position < token.buffer.size

    protected fun isTypeInFieldName(
        tag: Tag,
        recordId: Int,
    ): Boolean = tag.type == recordId

    protected fun checkRecordSizeLength(
        token: Token,
        offset: Int,
    ): Boolean = token.position + offset < token.buffer.size

    companion object {
        const val EMPTY_INT: Int = 255
        const val MAX_TYPE_SIZE: Int = 2
        const val MAX_FIELD_SIZE: Int = 10
        val EMPTY_TWO_BYTES: ByteArray = byteArrayOf(0, 0)
        val EMPTY_FOUR_BYTES: ByteArray = byteArrayOf(0, 0, 0, 0)
    }
}
