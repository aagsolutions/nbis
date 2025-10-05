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

package eu.aagsolutions.img.nbis.checksum

import eu.aagsolutions.img.nbis.converters.Converters.bytesToHex
import java.security.MessageDigest

/**
 * Computes the SHA-256 hash of the provided byte array.
 *
 * @param data the input byte array to be hashed; may be null
 * @return the SHA-256 hash of the input data as a byte array
 */
fun calculate(data: ByteArray?): ByteArray {
    val digest = MessageDigest.getInstance("SHA-256")
    return digest.digest(data?.clone())
}

/**
 * Computes the SHA-256 hash of the provided byte array and converts it to a hexadecimal string.
 *
 * @param data the input byte array to be hashed and converted to hexadecimal
 * @return the SHA-256 hash of the input data as a hexadecimal string
 */
fun calculateToHex(data: ByteArray): String {
    val hash = calculate(data)
    return bytesToHex(hash)
}
