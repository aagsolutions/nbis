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

package eu.aagsolutions.img.nbis.model.fields

/**
 * Represents a text-based field capable of storing and managing string data.
 *
 * This class implements the `Field` interface to encapsulate text data,
 * providing mechanisms to retrieve the data, its length, and create a deep copy
 * of the field. It also implements basic equality and hash code functionality
 * based on the text content.
 *
 * @constructor Initializes the `TextField` with the provided string data.
 * @param text the string data to be encapsulated by the `TextField`.
 */
class TextField(
    private val text: String,
) : Field<String> {
    override fun toString(): String = "TextData(text='$text', length=${text.length})"

    override fun getData(): String = text

    override fun getLength(): Int = text.length

    override fun deepCopy(): TextField = TextField(this.text)

    override fun hashCode(): Int {
        var result = text.hashCode()
        result = 31 * result + text.length
        result = 31 * result + text.hashCode()
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TextField

        return text == other.text
    }
}
