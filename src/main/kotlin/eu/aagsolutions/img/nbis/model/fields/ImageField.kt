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

import java.nio.ByteBuffer

/**
 * Represents an image field entity storing image data as a byte array.
 *
 * This class implements the `Field` interface to encapsulate image data,
 * providing mechanisms to retrieve the data, get its length, and create
 * a deep copy of the field.
 *
 * The image data is internally managed using a read-only `ByteBuffer`,
 * ensuring data integrity by preventing external modifications.
 */
class ImageField : Field<ByteArray> {
    private val imageData: ByteBuffer
    private val length: Int

    constructor(imageData: ByteArray) {
        this.imageData = ByteBuffer.wrap(imageData).asReadOnlyBuffer()
        this.length = imageData.size
    }

    override fun getData(): ByteArray {
        val clone = ByteBuffer.allocate(imageData.capacity())
        imageData.rewind()
        clone.put(imageData)
        return clone.array()
    }

    override fun getLength(): Int = length

    override fun deepCopy(): ImageField = ImageField(this.getData())
}
