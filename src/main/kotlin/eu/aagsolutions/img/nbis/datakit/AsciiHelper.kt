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

package eu.aagsolutions.img.nbis.datakit

import eu.aagsolutions.img.nbis.model.enums.AcceptedCharset
import java.nio.charset.CharsetDecoder

/**
 * ASCII Unit Separator character (US) - used to separate subfields within a field
 */
object AsciiHelper {
    const val UNIT_SEPARATOR = 31.toChar()

    /**
     * ASCII Record Separator character (RS) - used to separate records
     */
    const val RECORD_SEPARATOR = 30.toChar()

    /**
     * ASCII Group Separator character (GS) - used to separate groups of data
     */
    const val GROUP_SEPARATOR = 29.toChar()

    /**
     * ASCII Field Separator character (FS) - used to separate fields
     */
    const val FIELD_SEPARATOR = 28.toChar()

    const val FIELD_MAX_LENGTH = 300000

    val TAG_SEPARATOR_DOT = charArrayOf('.', '.')
    val TAG_SEPARATOR_COLON = charArrayOf(':', ':')
    val TAG_SEPARATOR_GROUP_FIELD = charArrayOf(GROUP_SEPARATOR, FIELD_SEPARATOR)
}

/**
 * Represents a tag structure containing type and field identifiers
 *
 * @property type The type identifier
 * @property field The field identifier
 */
data class Tag(
    val type: Int,
    val field: Int,
)

/**
 * Token class for processing byte buffers with charset decoding capabilities
 *
 * @property buffer The byte array to be processed
 */
class Token(
    val buffer: ByteArray,
) {
    /** Current position in the buffer */
    var position: Int = 0

    /** Header information */
    var header: String? = null

    /** Current record type */
    var crt: Int = 0

    /** Decoder for character set conversion */
    var charsetDecoder: CharsetDecoder?

    init {
        this.charsetDecoder = AcceptedCharset.CP1256.charset.newDecoder()
    }

    /**
     * Sets the character set decoder for the current instance based on the provided document type.
     *
     * The method selects a `CharsetDecoder` corresponding to the initial prefix of the provided document string.
     * - If the document string starts with "002" or is null, the UTF-16 decoder is set.
     * - If the document string starts with "003", the UTF-8 decoder is set.
     * - For all other cases, the CP1256 decoder is set.
     *
     * @param document The document string from which the character set is determined. Can be null.
     */
    fun setCharSetDecoder(document: String?) {
        when {
            document?.startsWith("002") ?: true -> this.charsetDecoder = AcceptedCharset.UTF_16.charset.newDecoder()
            document.startsWith("003") -> this.charsetDecoder = AcceptedCharset.UTF_8.charset.newDecoder()
            else -> this.charsetDecoder = AcceptedCharset.CP1256.charset.newDecoder()
        }
    }
}
