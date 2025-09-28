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

import eu.aagsolutions.img.nbis.model.enums.AcceptedCharset
import java.nio.charset.CharsetDecoder

const val UNIT_SEPARATOR = 31.toChar()

const val RECORD_SEPARATOR = 30.toChar()

const val GROUP_SEPARATOR = 29.toChar()

const val FILE_SEPARATOR = 28.toChar()

const val FIELD_MAX_LENGTH = 300000

val TAG_SEPARATOR_DOT = charArrayOf('.', '.')
val TAG_SEPARATOR_COLON = charArrayOf(':', ':')
val TAG_SEPARATOR_GROUP_FIELD = charArrayOf(GROUP_SEPARATOR, FILE_SEPARATOR)

data class Tag(
    val type: Int,
    val field: Int,
)

class Token(
    val buffer: ByteArray,
) {
    var position: Int = 0

    var header: String? = null
    var crt: Int = 0

    var charsetDecoder: CharsetDecoder?

    init {
        this.charsetDecoder = AcceptedCharset.CP1256.charset.newDecoder()
    }

    fun setCharSetDecoder(document: String?) {
        when {
            document?.startsWith("002") ?: true -> this.charsetDecoder = AcceptedCharset.UTF_16.charset.newDecoder()
            document.startsWith("003") -> this.charsetDecoder = AcceptedCharset.UTF_8.charset.newDecoder()
            else -> this.charsetDecoder = AcceptedCharset.CP1256.charset.newDecoder()
        }
    }
}
